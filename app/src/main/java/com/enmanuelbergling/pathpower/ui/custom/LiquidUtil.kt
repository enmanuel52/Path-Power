package com.enmanuelbergling.pathpower.ui.custom

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Shader
import android.os.Build
import androidx.annotation.FloatRange
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import kotlin.math.cos
import kotlin.math.sin


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun rememberLiquidEffect(): RenderEffect {
    val radius = 60f
    val blurEffect = android.graphics.RenderEffect.createBlurEffect(radius, radius, Shader.TileMode.DECAL)

    val matrixArray = floatArrayOf(
        1f, 0f, 0f, 0f, 0f, //Red
        0f, 1f, 0f, 0f, 0f, //Green
        0f, 0f, 1f, 0f, 0f, //Blue
        0f, 0f, 0f, 50f, -5_000f, //Alpha
    )

    val colorFilter = android.graphics.RenderEffect.createColorFilterEffect(
        ColorMatrixColorFilter(
            ColorMatrix(matrixArray)
        )
    )

    val liquidEffect =
        android.graphics.RenderEffect.createChainEffect(colorFilter, blurEffect).asComposeRenderEffect()

    return remember { liquidEffect }
}

/**
 * Fully based on Trigonometry, starting on 9:00 clockwise
 * @param radius stands for hypotenuse
 * @param degrees stands for alpha
 * */
@Composable
fun getOffsetAround(
    @FloatRange(
        from = 0.0,
        to = 360.0,
        fromInclusive = true,
        toInclusive = false,
    ) degrees: Float,
    radius: Dp,
): DpOffset {
    val newAngle = degrees % 90
    val radians = Math.toRadians(newAngle.toDouble()).toFloat()

    val oppositeSide = radius * sin(radians) //sin
    val adjacentSide = radius * cos(radians) //cos

    return when (degrees) {
        in 0f..<90f -> DpOffset(
            x = -adjacentSide, y = -oppositeSide
        )

        in 90f..<180f -> DpOffset(
            x = oppositeSide, y = -adjacentSide
        )

        in 180f..<270f -> DpOffset(
            x = adjacentSide, y = oppositeSide
        )

        in 270f..<360f -> DpOffset(
            x = -oppositeSide, y = adjacentSide
        )

        else -> DpOffset.Zero
    }
}

const val DefaultAnimationDuration = 2_500