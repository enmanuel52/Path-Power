package com.enmanuelbergling.path_power.ui.canvas

import androidx.annotation.FloatRange
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.path_power.ui.shape.Heart
import com.enmanuelbergling.path_power.util.DarkBlue40

internal const val WAVES = 2

/**
 * Indicate progress by drawing waves in Canvas
 * @param progress of the waves
 * @param waveHeightPercent says how larger the wave is
 * @param offsetX just for animation purposes
 * */
internal fun DrawScope.wavesIndicator(
    progress: Float,
    offsetX: Float = 0f,
    waveHeightPercent: Float = .06f,
    color: Color = DarkBlue40,
) {
    val waveHeight = size.height * waveHeightPercent

    val waveWidth = size.width / 2

    val startOffsetY = (1f - progress) * size.height

    val path = Path().apply {
        moveTo(0f, startOffsetY)
        repeat(WAVES) {
            val startOffsetX = it * size.width + offsetX

            cubicTo(
                x1 = 3f / 8 * waveWidth + startOffsetX,
                y1 = startOffsetY + waveHeight,
                x2 = 5f / 8 * waveWidth + startOffsetX,
                y2 = startOffsetY + waveHeight,
                x3 = waveWidth + startOffsetX,
                y3 = startOffsetY
            )

            cubicTo(
                x1 = 3f / 8 * waveWidth + waveWidth + startOffsetX,
                y1 = startOffsetY - waveHeight,
                x2 = 5f / 8 * waveWidth + waveWidth + startOffsetX,
                y2 = startOffsetY - waveHeight,
                x3 = waveWidth + waveWidth + startOffsetX,
                y3 = startOffsetY
            )

        }
        lineTo(size.width, size.height)
        lineTo(0f, size.height)
        close()
    }

    drawPath(
        path = path,
        color = color
    )
}

/**
 * To determine how angry or higher the waves are
 * */
sealed class WaveForce(@FloatRange(.0, .6) val upPercent: Float, val durationMillis: Int) {
    data object Quiet : WaveForce(upPercent = .03f, durationMillis = 2000)
    data object Normal : WaveForce(upPercent = .06f, durationMillis = 1500)
    data object Angry : WaveForce(upPercent = .12f, durationMillis = 1000)

    data class Custom(@FloatRange(.0, .6) val percent: Float, val duration: Int) :
        WaveForce(upPercent = percent, durationMillis = duration) {
        init {
            require(percent in 0.0..0.6) { "Provided percent $percent should be between 0 and 0.6." }
        }
    }
}

@Preview
@Composable
private fun WavesPreview() {
    Box(
        modifier = Modifier
            .size(300.dp)
            .border(1.dp, DarkBlue40)
            .drawBehind {
                wavesIndicator(
                    progress = .4f
                )
            }
    )
}

/**
 *  Indicate progress by animating drawing waves in Canvas
 * @param progress the progress of this indicator in the 0.0 to 1.0 range
 * @param waveForce says how angry waves are
 * @param goForward to move in one direction
 * @param content will be placed on the center
 * */
@Composable
fun AnimatedWavesIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    waveForce: WaveForce = WaveForce.Normal,
    color: Color = DarkBlue40,
    goForward: Boolean = true,
    content: @Composable () -> Unit = {},
) {

    val animatedWaveHeightPercent by animateFloatAsState(
        targetValue = waveForce.upPercent,
        label = "wave height percent",
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    val animatedWaveDuration by animateIntAsState(
        targetValue = waveForce.durationMillis,
        label = "wave duration",
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    val density = LocalDensity.current

    val infiniteTransition = rememberInfiniteTransition(label = "infinite waves transition")

    var pxWidth by remember {
        mutableIntStateOf(0)
    }

    val offsetXAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = with(density) { -pxWidth.toDp().toPx() },
        animationSpec = infiniteRepeatable(
            tween(
                durationMillis = animatedWaveDuration,
                easing = LinearEasing
            ),
            repeatMode = if (goForward) RepeatMode.Restart else RepeatMode.Reverse
        ),
        label = "offset animation"
    )

    Box {
        Canvas(
            modifier = modifier.onSizeChanged {
                pxWidth = it.width
            }
        ) {
            wavesIndicator(
                progress = progress,
                offsetX = offsetXAnimation,
                waveHeightPercent = animatedWaveHeightPercent,
                color = color
            )
        }
        Box(modifier = Modifier.align(Alignment.Center)) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AnimatedWavesPreview() {
    var progress by remember {
        mutableFloatStateOf(0f)
    }
    var waveHeightPercent by remember {
        mutableFloatStateOf(0f)
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        label = "progress animation",
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        AnimatedWavesIndicator(
            progress = animatedProgress,
            modifier = Modifier
                .size(300.dp, 270.dp)
                .clip(Heart)
                .border(4.dp, MaterialTheme.colorScheme.surfaceVariant, Heart),
            color = MaterialTheme.colorScheme.primary,
            waveForce = WaveForce.Custom(waveHeightPercent, 1100)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Progress")
            Spacer(modifier = Modifier.width(8.dp))
            Slider(
                value = progress,
                onValueChange = { progress = it },
                valueRange = 0f..1f,
                modifier = Modifier.fillMaxWidth(.6f),
                thumb = {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "progress thumb icon"
                    )
                }
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Height")
            Spacer(modifier = Modifier.width(8.dp))
            Slider(
                value = waveHeightPercent,
                onValueChange = { waveHeightPercent = it },
                valueRange = 0f..0.15f,
                modifier = Modifier.fillMaxWidth(.6f),
                thumb = {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "progress thumb icon"
                    )
                }
            )
        }
    }
}