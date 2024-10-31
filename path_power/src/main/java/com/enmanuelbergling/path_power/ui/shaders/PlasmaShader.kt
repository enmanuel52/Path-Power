package com.enmanuelbergling.path_power.ui.shaders

import android.graphics.RuntimeShader
import org.intellij.lang.annotations.Language

@Language("AGSL")
private val shader = """
    uniform float2 resolution;
    uniform float time;
    uniform float dimmer;
    
    half4 main(vec2 fragCoord) { 
        vec2 uv = fragCoord / resolution.xy;
        vec3 colGradient = 0.5
                + 0.9 * sin(-time + (uv.x * uv.x) + (uv.y * uv.y) + vec3(8, 1, 3))
                      * cos(time + (uv.y * uv.x) + (uv.y * uv.y) + vec3(5, 1, 6))
                + 0.5 * cos(time + (uv.y * uv.x) + (uv.y * uv.y) + vec3(1, 2, 3))
                      * cos(time + (uv.y * uv.x) + (uv.y * uv.y) + vec3(13, 5, 8))
                      * cos(time + (uv.y * uv.x) + (uv.y * uv.y) + vec3(1, 2, 1)) 
                + 0.5 * cos(time + (uv.y * uv.x) + (uv.y * uv.y) + vec3(1, 2, 3));
        return half4(colGradient * dimmer, dimmer);
    }    
""".trimIndent()

fun plasmaShader(
    dimmer: Float = 1f,
) = RuntimeShader(shader).apply {
    setFloatUniform("dimmer", dimmer)
}