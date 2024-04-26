package com.enmanuelbergling.pathpower.ui.animation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.pathpower.ui.custom.rememberLiquidEffect


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun LiquidCircles() {
    val primaryColor = MaterialTheme.colorScheme.primary

    val liquidEffect = rememberLiquidEffect()

    val density = LocalDensity.current
    val heightPx = with(density) { 150.dp.toPx() }

    var isExpanded by remember {
        mutableStateOf(false)
    }

    val animationProgress by animateFloatAsState(
        targetValue = if (isExpanded) 1f else 0f,
        animationSpec = tween(2_000),
        label = "animation progress"
    )

    Box(
        Modifier
            .fillMaxHeight()
            .padding(24.dp)
    ) {
        BoxWithIconGroup(liquidEffect, animationProgress, heightPx, primaryColor)

        BoxWithIconGroup(null, animationProgress, heightPx, primaryColor)

        Button(
            onClick = { isExpanded = !isExpanded },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(text = if (isExpanded) "Shrink" else "Expand")
        }
    }
}

@Composable
private fun BoxWithIconGroup(
    liquidEffect: RenderEffect?,
    animationProgress: Float,
    heightPx: Float,
    primaryColor: Color,
) {
    Box(modifier = Modifier
        .fillMaxHeight()
        .graphicsLayer {
            renderEffect = liquidEffect
        }) {

        BoxWithIcon(animationProgress, heightPx, primaryColor, 0f)

        BoxWithIcon(animationProgress, heightPx, primaryColor, 1.3f)

        BoxWithIcon(animationProgress, heightPx, primaryColor, 2.6f)
    }
}

@Composable
private fun BoxWithIcon(
    animationProgress: Float,
    heightPx: Float,
    primaryColor: Color,
    translation: Float,
) {
    Box(modifier = Modifier
        .graphicsLayer {
            translationY = animationProgress * heightPx * translation
        }
        .size(150.dp)
        .clip(CircleShape)
        .background(primaryColor)) {
        Icon(
            imageVector = Icons.Rounded.Build,
            contentDescription = "some icon",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.align(
                Alignment.Center
            )
        )
    }
}