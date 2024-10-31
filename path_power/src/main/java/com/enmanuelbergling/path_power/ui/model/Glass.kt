package com.enmanuelbergling.path_power.ui.model

import androidx.compose.ui.graphics.Shape
import com.enmanuelbergling.path_power.ui.canvas.WaveColor
import com.enmanuelbergling.path_power.ui.canvas.WaveColor.Companion.DefaultBackWaveColor
import com.enmanuelbergling.path_power.ui.canvas.WaveColor.Companion.DefaultFrontWaveColor
import com.enmanuelbergling.path_power.ui.shaders.artisticPlasma
import com.enmanuelbergling.path_power.ui.shaders.goldenShader
import com.enmanuelbergling.path_power.ui.shaders.hotPlasma
import com.enmanuelbergling.path_power.ui.shaders.plasmaShader
import com.enmanuelbergling.path_power.ui.shape.Heart
import com.enmanuelbergling.path_power.ui.shape.Hexagon
import com.enmanuelbergling.path_power.ui.shape.Stain
import com.enmanuelbergling.path_power.ui.shape.Star
import com.enmanuelbergling.path_power.ui.shape.WaterDrop

/**
 * Defines the waves container
 * */
enum class Glass(
    val shape: Shape,
    val backWaveColor: WaveColor,
    val frontWaveColor: WaveColor,
) {
    SimpleWaterDrop(
        WaterDrop,
        DefaultBackWaveColor,
        DefaultFrontWaveColor,
    ),
    LavaHexagon(
        Hexagon,
        WaveColor.Shader.Animated(goldenShader(0.45f)),
        WaveColor.Shader.Animated(goldenShader()),
    ),
    ColorfulStain(
        Stain(),
        WaveColor.Shader.Animated(artisticPlasma(0.45f)),
        WaveColor.Shader.Animated(artisticPlasma()),
    ),
    ColorfulHeart(
        Heart,
        WaveColor.Shader.Animated(plasmaShader(0.45f)),
        WaveColor.Shader.Animated(plasmaShader()),
    ),
    HotStar(
        Star,
        WaveColor.Shader.Animated(hotPlasma(0.45f)),
        WaveColor.Shader.Animated(hotPlasma()),
    )
}