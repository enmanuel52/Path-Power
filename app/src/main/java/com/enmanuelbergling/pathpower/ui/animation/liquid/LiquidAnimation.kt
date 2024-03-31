package com.enmanuelbergling.pathpower.ui.animation.liquid

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.RenderEffect
import android.graphics.Shader
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
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
fun LiquidCircles() {
    val primaryColor = MaterialTheme.colorScheme.primary

    val liquidEffect =
        rememberLiquidEffect()

    /*Box(
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
            .drawBehind { drawCircle(primaryColor) }
//            .blur(4.dp)
//            .blur(4.dp, 6.dp)
            .graphicsLayer {
                renderEffect = blurEffect.asComposeRenderEffect()
            },
    )*/
    val density = LocalDensity.current
    val heightPx = with(density) { 150.dp.toPx() }

    var isExpanded by remember {
        mutableStateOf(true)
    }

    val animationProgress by animateFloatAsState(
        targetValue = if (isExpanded) 1f else 0f,
        animationSpec = tween(2_000), label = "animation progress"
    )

    Box(
        Modifier
            .fillMaxHeight()
            .padding(24.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxHeight()
                .graphicsLayer {
                renderEffect = liquidEffect
            }
        ) {

            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(primaryColor)
            )
            Box(modifier = Modifier
                .graphicsLayer {
                    translationY = animationProgress * heightPx * 1.3f
                }
                .size(150.dp)
                .clip(CircleShape)
                .background(primaryColor)
            )
            Box(modifier = Modifier
                .graphicsLayer {
                    translationY = animationProgress * heightPx * 2.6f
                }
                .size(150.dp)
                .clip(CircleShape)
                .background(primaryColor)
            )
        }

        Button(
            onClick = { isExpanded = !isExpanded },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(text = if (isExpanded) "Shrink" else "Expand")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
private fun rememberLiquidEffect(): androidx.compose.ui.graphics.RenderEffect {
    val radius = 60f
    val blurEffect = RenderEffect
        .createBlurEffect(radius, radius, Shader.TileMode.DECAL)

    val matrixArray = floatArrayOf(
        1f, 0f, 0f, 0f, 0f, //Red
        0f, 1f, 0f, 0f, 0f, //Green
        0f, 0f, 1f, 0f, 0f, //Blue
        0f, 0f, 0f, 50f, -5_000f, //Alpha
    )

    val colorFilter = RenderEffect.createColorFilterEffect(
        ColorMatrixColorFilter(
            ColorMatrix(matrixArray)
        )
    )

    val liquidEffect =
        RenderEffect.createChainEffect(colorFilter, blurEffect).asComposeRenderEffect()

    return remember { liquidEffect }
}