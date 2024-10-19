package com.enmanuelbergling.path_power.ui.modifiers

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.graphics.Shader
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun Modifier.shader(
    shader: String,
    uniformsBlock: (ShaderUniformProvider.() -> Unit)? = null,
): Modifier = this then composed {
    val runtimeShader = remember { RuntimeShader(shader).also { Log.d("AGSL", "create RuntimeShader") } }
    val shaderUniformProvider = remember { ShaderUniformProviderImpl(runtimeShader).also { Log.d("AGSL", "create ShaderUniformProviderImpl") } }
    graphicsLayer {
        this.clip = true
        this.renderEffect = RenderEffect
            .createShaderEffect(
                runtimeShader.apply {
                    uniformsBlock?.invoke(shaderUniformProvider)
                    shaderUniformProvider.updateResolution(size)
                    Log.d("AGSL", "create ShaderEffect")
                },
            ).asComposeRenderEffect().also {
                renderEffect = it
            }
    }
}

fun Modifier.runtimeShader(
    shader: String,
    uniformName: String = "content",
    uniformsBlock: (ShaderUniformProvider.() -> Unit)? = null,
): Modifier = this then composed {
    val runtimeShader = remember { RuntimeShader(shader) }
    val shaderUniformProvider = remember { ShaderUniformProviderImpl(runtimeShader) }
    graphicsLayer {
        clip = true
        renderEffect = RenderEffect
            .createRuntimeShaderEffect(
                runtimeShader.apply {
                    uniformsBlock?.invoke(shaderUniformProvider)
                    shaderUniformProvider.updateResolution(size)
                },
                uniformName,
            ).asComposeRenderEffect()
    }
}

@Stable
@Immutable
interface ShaderUniformProvider {
    fun uniform(name: String, value: Int)
    fun uniform(name: String, value: Float)
    fun uniform(name: String, value1: Float, value2: Float)
    fun uniform(name: String, value: Shader)
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private class ShaderUniformProviderImpl(
    private val runtimeShader: RuntimeShader,
) : ShaderUniformProvider {

    fun updateResolution(size: Size) {
        uniform("resolution", size.width, size.height)
    }

    override fun uniform(name: String, value: Int) {
        runtimeShader.setIntUniform(name, value)
    }

    override fun uniform(name: String, value: Float) {
        runtimeShader.setFloatUniform(name, value)
    }

    override fun uniform(name: String, value1: Float, value2: Float) {
        runtimeShader.setFloatUniform(name, value1, value2)
    }

    override fun uniform(name: String, value: Shader) {
        runtimeShader.setInputShader(name, value)
    }
}
