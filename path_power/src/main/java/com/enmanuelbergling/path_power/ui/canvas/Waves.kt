package com.enmanuelbergling.path_power.ui.canvas

import android.graphics.RuntimeShader
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
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.path_power.ui.modifiers.wavesShader
import com.enmanuelbergling.path_power.ui.shaders.plasmaShader
import com.enmanuelbergling.path_power.ui.shape.Heart
import com.enmanuelbergling.path_power.util.DarkBlue40
import kotlinx.coroutines.isActive

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
sealed interface WaveForce {
    sealed interface CanvasBased : WaveForce {
        val upPercent: Float
        val durationMillis: Int

        data class Custom(
            @FloatRange(.0, .6) override val upPercent: Float,
            override val durationMillis: Int,
        ) : CanvasBased {
            init {
                require(upPercent in 0.0..0.6) { "Provided percent $upPercent should be between 0 and 0.6." }
            }
        }

        companion object {
            val Quiet = Custom(upPercent = .03f, durationMillis = 2000)
            val Normal = Custom(upPercent = .06f, durationMillis = 1500)
            val Angry = Custom(upPercent = .12f, durationMillis = 1000)
        }
    }

    sealed interface AGSLBased : WaveForce {
        val height: Float
        val frequency: Float
        val speed: Float
        val backWaveColor: WaveColor
        val frontWaveColor: WaveColor

        data class Custom(
            override val height: Float,
            override val frequency: Float,
            override val speed: Float,
            override val backWaveColor: WaveColor = WaveColor.DefaultBackWaveColor,
            override val frontWaveColor: WaveColor = WaveColor.DefaultFrontWaveColor,
        ) : AGSLBased

        companion object {
            val Quiet = Custom(height = 0.5f, frequency = 0.5f, speed = 0.75f)
            val Normal = Custom(height = 1.5f, frequency = 0.6f, speed = 1.0f)
            val Angry = Custom(height = 2.5f, frequency = 0.7f, speed = 3.0f)
        }
    }
}

sealed interface WaveColor {
    data class Regular(val color: Color) : WaveColor

    sealed interface Shader : WaveColor {
        val shader: RuntimeShader

        fun updateResolution(width: Float, height: Float) {
            shader.setFloatUniform("resolution", width, height)
        }

        data class Regular(override val shader: RuntimeShader) : Shader
        data class Animated(override val shader: RuntimeShader) : Shader {
            fun updateTime(time: Float) {
                shader.setFloatUniform("time", time)
            }
        }
    }

    companion object {
        val DefaultBackWaveColor = Regular(Color(0xFF3498DB))
        val DefaultFrontWaveColor = Regular(Color(0xFF0E7AA0))
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
    waveForce: WaveForce = WaveForce.CanvasBased.Normal,
    color: Color = DarkBlue40,
    goForward: Boolean = true,
    content: @Composable () -> Unit = {},
) {
    when (waveForce) {
        is WaveForce.CanvasBased ->
            CanvasBasedWaveIndicator(
                progress = progress,
                modifier = modifier,
                waveForce = waveForce,
                color = color,
                goForward = goForward,
                content = content,
            )

        is WaveForce.AGSLBased ->
            AGSLBasedWaveIndicator(
                progress = progress,
                modifier = modifier,
                waveForce = waveForce,
                color = color,
                goForward = goForward,
                content = content,
            )
    }
}

@Composable
private fun CanvasBasedWaveIndicator(
    progress: Float,
    waveForce: WaveForce.CanvasBased,
    modifier: Modifier = Modifier,
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

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { pxWidth = it.width }
        ) {
            wavesIndicator(
                progress = progress,
                offsetX = offsetXAnimation,
                waveHeightPercent = animatedWaveHeightPercent,
                color = color
            )
        }
        CompositionLocalProvider(value = LocalContentColor provides contentColorFor(color)) {
            content()
        }
    }
}

@Composable
private fun AGSLBasedWaveIndicator(
    progress: Float,
    waveForce: WaveForce.AGSLBased,
    modifier: Modifier = Modifier,
    color: Color = DarkBlue40,
    goForward: Boolean = true,
    content: @Composable () -> Unit = {},
) {
    val time by produceState(0f) {
        while (isActive) {
            withInfiniteAnimationFrameMillis {
                value = if (goForward) it / 1000f else -it / 1000f
            }
        }
    }
    val backWaveShader = remember(waveForce.backWaveColor) { waveForce.backWaveColor.toRuntimeShader() }
    val frontWaveShader = remember(waveForce.frontWaveColor) { waveForce.frontWaveColor.toRuntimeShader() }
    (waveForce.backWaveColor as? WaveColor.Shader.Animated)?.apply {
        updateTime(time)
    }
    (waveForce.frontWaveColor as? WaveColor.Shader.Animated)?.apply {
        updateTime(time)
    }
    Surface(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged {
                (waveForce.backWaveColor as? WaveColor.Shader)?.apply { updateResolution(it.width.toFloat(), it.height.toFloat()) }
                (waveForce.frontWaveColor as? WaveColor.Shader)?.apply { updateResolution(it.width.toFloat(), it.height.toFloat()) }
            }
            .wavesShader(
                progress = progress,
                time = time,
                height = waveForce.height,
                frequency = waveForce.frequency,
                speed = waveForce.speed,
                backWaveShader = backWaveShader,
                frontWaveShader = frontWaveShader,
            ),
    ) {
        CompositionLocalProvider(value = LocalContentColor provides contentColorFor(color)) {
            content()
        }
    }
}

private fun WaveColor.toRuntimeShader(): RuntimeShader =
    when (this) {
        is WaveColor.Regular -> RuntimeShader(
            """
                half4 main(vec2 fragCoord) { return half4(${this.color.red}, ${this.color.green}, ${this.color.blue}, 1.0); } 
            """.trimIndent()
        )

        is WaveColor.Shader -> shader
    }

@OptIn(ExperimentalMaterial3Api::class)
@Preview
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
        verticalArrangement = Arrangement.spacedBy(12.dp, CenterVertically),
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        AnimatedWavesIndicator(
            progress = animatedProgress,
            modifier = Modifier
                .size(300.dp, 270.dp)
                .clip(Heart)
                .border(4.dp, MaterialTheme.colorScheme.surfaceVariant, Heart),
            color = MaterialTheme.colorScheme.primary,
            waveForce = WaveForce.CanvasBased.Custom(waveHeightPercent, 1100)
        )

        Row(verticalAlignment = CenterVertically) {
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

        Row(verticalAlignment = CenterVertically) {
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

@Composable
@Preview
internal fun AnimatedWavesWithAGSLPreview() {
    var progress by remember { mutableFloatStateOf(0f) }
    val animatedProgress by animateFloatAsState(progress, label = "progress animation")
    var height by remember { mutableFloatStateOf(0.6f) }
    val animatedHeight by animateFloatAsState(height, label = "scale animation")
    var frequency by remember { mutableFloatStateOf(0.6f) }
    val animatedFrequency by animateFloatAsState(frequency, label = "frequency animation")
    var speed by remember { mutableFloatStateOf(0.6f) }
    val animatedSpeed by animateFloatAsState(speed, label = "speed animation")
    // Remembering shader wave color avoids glitches when progress and other params are updated
    val backWaveColor = remember { WaveColor.Shader.Animated(plasmaShader(0.45f)) }
    val frontWaveColor = remember { WaveColor.Shader.Animated(plasmaShader()) }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = CenterHorizontally,
    ) {
        AnimatedWavesIndicator(
            progress = animatedProgress,
            modifier = Modifier
                .size(300.dp, 270.dp)
                .clip(Heart)
                .border(4.dp, MaterialTheme.colorScheme.surfaceVariant, Heart),
            color = MaterialTheme.colorScheme.primary,
            waveForce = WaveForce.AGSLBased.Custom(
                frequency = animatedFrequency,
                speed = animatedSpeed,
                height = animatedHeight,
                backWaveColor = backWaveColor,
                frontWaveColor = frontWaveColor,
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Slider(
            value = progress,
            onValueChange = { newValue -> progress = newValue },
            valueRange = 0f..1f
        )
        Spacer(modifier = Modifier.height(16.dp))
        Slider(
            value = height,
            onValueChange = { newValue -> height = newValue },
            valueRange = 0.1f..4.0f
        )
        Spacer(modifier = Modifier.height(16.dp))
        Slider(
            value = frequency,
            onValueChange = { newValue -> frequency = newValue },
            valueRange = 0.1f..4.0f
        )
        Spacer(modifier = Modifier.height(16.dp))
        Slider(
            value = speed,
            onValueChange = { newValue -> speed = newValue },
            valueRange = 0.1f..4.0f
        )
    }
}
