package com.enmanuelbergling.path_power.ui.modifiers

import android.graphics.RuntimeShader
import androidx.compose.ui.Modifier
import org.intellij.lang.annotations.Language

@Language("AGSL")
private val shader = """
    uniform float2 resolution;
    uniform float progress;
    uniform float time;
    uniform float height;
    uniform float frequency;
    uniform float speed;
    uniform shader backWaveShader;
    uniform shader frontWaveShader;
    
    half4 main(vec2 fragCoord) { 
        // Normalized pixel coordinates (from 0 to 1)
        vec2 uv = fragCoord / resolution.xy;
        
        float smoothness = 0.002;
        float wave1height1 = 0.01 * height;
        float wave1height2 = 0.005 * height;
        float wave2height1 = 0.02 * height;
        float wave2height2 = 0.005 * height;
            
        float maxWaveHeight = max(wave1height1 + wave1height2, wave2height1 + wave2height2);
        float height = (1.0 - progress) * (1.0 + maxWaveHeight) - maxWaveHeight / 2.0;
    
        vec4 colBackground = vec4(0.0);
        vec4 colBackWave = backWaveShader.eval(fragCoord); //vec4(0.2039215682, 0.5960784302, 0.8588235278, 1.0);
        vec4 colFrontWave = frontWaveShader.eval(fragCoord); //vec4(0.05490196068, 0.4784313716, 0.6274509792, 1.0);
        
        float newTimne = time * speed;
    
        // Time varying pixel color
//        vec3 colGradient = 0.5
//                + 0.9 * sin(-time + (uv.x * uv.x) + (uv.y * uv.y) + vec3(8, 1, 3))
//                      * cos(time + (uv.y * uv.x) + (uv.y * uv.y) + vec3(5, 1, 6))
//                + 0.5 * cos(time + (uv.y * uv.x) + (uv.y * uv.y) + vec3(1, 2, 3))
//                      * cos(time + (uv.y * uv.x) + (uv.y * uv.y) + vec3(13, 5, 8))
//                      * cos(time + (uv.y * uv.x) + (uv.y * uv.y) + vec3(1, 2, 1)) 
//                + 0.5 * cos(time + (uv.y * uv.x) + (uv.y * uv.y) + vec3(1, 2, 3));
//    
//        colBackWave = 0.45 * vec4(colGradient, 1.0);
//        colFrontWave = vec4(colGradient, 1.0);
    
        // Back wave
        float waveFunc = height 
            + (wave1height1 * sin(uv.x * 35.0 * frequency + newTimne * 2.0)) 
            + (wave1height2 * sin(uv.x * 20.0 * frequency + newTimne * 0.5));
        float waveShape1 = smoothstep(waveFunc, waveFunc + smoothness, uv.y);
        vec4 col = mix(colBackground, colBackWave, waveShape1);
    
        // Front wave
        waveFunc = height 
            + (wave2height1 * sin(uv.x * 20.0 * frequency + newTimne * 2.0)) 
            + (wave2height2 * sin(uv.x * 30.0 * frequency + newTimne * 0.7));
        float waveShape2 = smoothstep(waveFunc, waveFunc + smoothness, uv.y);
        float waveTop = smoothstep(waveFunc, waveFunc + 0.005, uv.y);
        vec4 col4 = mix(colBackWave, colFrontWave, pow(waveTop, 3.0));
        col = mix(col, col4, waveShape2);
    
        // Output to screen
        return half4(col);
    }    
""".trimIndent()

internal fun Modifier.wavesShader(
    progress: Float,
    time: Float,
    height: Float = 0.6f,
    frequency: Float = 0.6f,
    speed: Float = 1.0f,
    backWaveShader: RuntimeShader,
    frontWaveShader: RuntimeShader,
) = this then shader(shader) {
    uniform("progress", progress)
    uniform("time", time)
    uniform("height", height)
    uniform("frequency", frequency)
    uniform("speed", speed)
    uniform("backWaveShader", backWaveShader)
    uniform("frontWaveShader", frontWaveShader)
}
