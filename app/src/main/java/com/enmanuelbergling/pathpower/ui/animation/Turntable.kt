package com.enmanuelbergling.pathpower.ui.animation

import android.content.ContentResolver
import android.content.Context
import android.media.session.PlaybackState
import android.net.Uri
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.enmanuelbergling.pathpower.R

private const val PlayingStickAngle = -5f

@Preview
@Composable
fun Turntable() {
    val context = LocalContext.current

    val player = remember {
        buildPlayer(context)
    }

    var isPlaying by remember {
        mutableStateOf(false)
    }

    var cachedDiscDegrees by remember {
        mutableFloatStateOf(0f)
    }

    val stickDegreesAnimation by animateFloatAsState(
        targetValue = if (isPlaying) PlayingStickAngle else -20f,
        label = "stick animation",
        animationSpec = tween(easing = LinearEasing)
    )

    val isStickOverTransition = updateTransition(
        targetState = stickDegreesAnimation == PlayingStickAngle, "is stick over disc"
    )

    val discDegreesAnimation by isStickOverTransition.animateFloat(label = "stick animation",
        transitionSpec = {
            repeatable(
                iterations = Int.MAX_VALUE,
                animation = tween(easing = LinearEasing, durationMillis = 2500)
            )
        }
    ) { playing ->
        if (playing) {
            if (isPlaying) cachedDiscDegrees + 360f
            else cachedDiscDegrees //to make it stop
        } else cachedDiscDegrees
    }

    LaunchedEffect(key1 = stickDegreesAnimation == PlayingStickAngle) {
        if (isPlaying) {
            player.play()
        } else player.pause()
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.Center)
        ) {
            Disc(onClick = {
                cachedDiscDegrees = discDegreesAnimation
                isPlaying = !isPlaying
            }, modifier = Modifier
                .fillMaxSize()
                .padding(40.dp)
                .graphicsLayer {
                    rotationZ = discDegreesAnimation
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.avicii_cover),
                    contentDescription = "avicii cover",
                    Modifier.fillMaxSize()
                )
            }

            Stick(
                Modifier
                    .size(240.dp)
                    .align(Alignment.TopEnd)
                    .padding(bottom = 20.dp)
                    .graphicsLayer {
                        rotationZ = stickDegreesAnimation
                        transformOrigin = TransformOrigin(1f, 0f)
                    }
            )
        }
    }
}

private fun buildPlayer(context: Context) =
    ExoPlayer.Builder(context).build().apply {
        val songUri = Uri.Builder().scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .path(R.raw.heaven_avicii.toString()).build()
        // Build the media item.
        val mediaItem = MediaItem.fromUri(songUri)
        // Set the media item to be played.
        setMediaItem(mediaItem)
        // Prepare the player.
        prepare()
    }

@Composable
fun Stick(
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(id = R.drawable.turntable_stick),
        contentDescription = "stick",
        modifier,
        contentScale = ContentScale.Crop
    )
}

@Preview
@Composable
private fun StickIdle() {
    Stick(
        Modifier
            .padding(24.dp)
            .size(100.dp)
            .graphicsLayer(rotationZ = -20f, transformOrigin = TransformOrigin(1f, 0f))
    )
}

@Preview
@Composable
private fun StickPlaying() {
    Stick(
        Modifier
            .padding(8.dp)
            .size(100.dp)
    )
}

@Composable
fun Disc(
    onClick: () -> Unit,
    modifier: Modifier,
    coverImage: @Composable () -> Unit,
) {

    Box(modifier = modifier
        .clip(CircleShape)
        .clickable {
            onClick()
        }) {
        coverImage()
    }
}

@Preview
@Composable
private fun DiscPrev() {
    Disc(
        {},
        Modifier
            .size(180.dp)
            .rotate(50f)
    ) {
        Image(
            painter = painterResource(id = R.drawable.coolest_avicii_cover),
            contentDescription = "avicii cover"
        )
    }
}

