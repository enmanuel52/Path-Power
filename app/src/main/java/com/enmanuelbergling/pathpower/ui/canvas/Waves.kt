package com.enmanuelbergling.pathpower.ui.canvas

import androidx.annotation.FloatRange
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.pathpower.ui.shape.Heart
import com.enmanuelbergling.pathpower.ui.theme.DarkBlue40

internal const val WAVES = 2

/**
 * Draw waves in Canvas
 * @param filledPercent of the waves
 * @param waveForce says how angry it is
 * @param offsetX just for animation purposes
 * */
internal fun DrawScope.waves(
    filledPercent: Float,
    offsetX: Float = 0f,
    waveForce: WaveForce = WaveForce.Normal,
    color: Color = DarkBlue40,
) {
    val waveHeight = size.height * waveForce.upPercent

    val waveWidth = size.width / 2

    val startOffsetY = (1f - filledPercent) * size.height

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

enum class WaveForce(@FloatRange(.01, .6) val upPercent: Float, val durationMillis: Int) {
    Quiet(.03f, 2000),
    Normal(.06f, 1500),
    Angry(.12f, 1000),
}

@Preview
@Composable
private fun WavesPreview() {
    Box(
        modifier = Modifier
            .size(300.dp)
            .border(1.dp, DarkBlue40)
            .drawBehind {
                waves(
                    filledPercent = .4f,
                    waveForce = WaveForce.Normal
                )
            }
    )
}

/**
 * Waves will be as width as the [Composable] itself
 * @param filledPercent of the waves
 * @param widthDp for calculations
 * @param waveForce says how angry it is
 * @param goForward to move in one direction
 * */
@Composable
fun AnimatedWaves(
    filledPercent: Float,
    widthDp: Dp,
    modifier: Modifier = Modifier,
    waveForce: WaveForce = WaveForce.Normal,
    color: Color = DarkBlue40,
    goForward: Boolean = true,
) {
    val density = LocalDensity.current

    val infiniteTransition = rememberInfiniteTransition(label = "infinite waves transition")

    val offsetXAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = with(density) { -widthDp.toPx() },
        animationSpec = infiniteRepeatable(
            tween(
                durationMillis = waveForce.durationMillis,
                easing = LinearEasing
            ),
            repeatMode = if (goForward) RepeatMode.Restart else RepeatMode.Reverse
        ),
        label = "offset animation"
    )

    Box {
        Canvas(modifier = modifier) {
            waves(
                filledPercent = filledPercent,
                offsetX = offsetXAnimation,
                waveForce = waveForce,
                color = color
            )
        }
    }
}

@Preview
@Composable
internal fun AnimatedWavesPreview() {
    AnimatedWaves(
        filledPercent = .51f,
        widthDp = 300.dp,
        modifier = Modifier
            .size(300.dp, 350.dp)
            .clip(Heart)
            .border(1.dp, MaterialTheme.colorScheme.primary, Heart),
        color = MaterialTheme.colorScheme.primary,
        waveForce = WaveForce.Angry,
    )
}