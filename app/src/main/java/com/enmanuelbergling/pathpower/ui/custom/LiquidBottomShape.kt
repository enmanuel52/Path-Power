package com.enmanuelbergling.pathpower.ui.custom

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class LiquidBottomShape(
    private val fabSize: Dp = 58.dp,
    private val roundX: Dp = 8.dp,
    private val roundY: Dp = 8.dp,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline = Outline.Generic(
        getLiquidBottomPath(
            size = size,
            density = density,
            fabSize = fabSize,
            roundX = roundX,
            roundY = roundY
        )
    )
}

private fun getLiquidBottomPath(
    size: Size,
    density: Density,
    fabSize: Dp = 58.dp,
    roundX: Dp = 8.dp,
    roundY: Dp = 8.dp,
) = Path().apply {
    val fabSizePx = with(density) { fabSize.toPx() }
    val roundYPx = with(density) { roundY.toPx() }
    val roundXPx = with(density) { roundX.toPx() }

    moveTo(0f, 0f)

    lineTo(x = size.width / 2 - fabSizePx / 2 - roundXPx, y = 0f)

    quadraticBezierTo(
        x1 = size.width / 2 - fabSizePx / 2,
        y1 = 0f,
        x2 = size.width / 2 - fabSizePx / 2,
        y2 = roundYPx
    )

    addArc(
        oval = Rect(center = Offset(size.width / 2, roundYPx), radius = fabSizePx / 2),
        startAngleDegrees = 180f,
        sweepAngleDegrees = -180f
    )

    quadraticBezierTo(
        x1 = size.width / 2 + fabSizePx / 2,
        y1 = 0f,
        x2 = size.width / 2 + fabSizePx / 2 + roundXPx,
        y2 = 0f
    )

    lineTo(size.width, 0f)
    lineTo(size.width, size.height)
    lineTo(0f, size.height)

    lineTo(0f, 0f)
}

@Preview
@Composable
private fun LiquidBottomBarCanvas() {

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
    ) {

        val bottomPath = getLiquidBottomPath(
            size = size,
            density = Density(density, fontScale)
        )

        drawPath(
            path = bottomPath,
            color = Color.Green,
            style = Stroke(1.dp.toPx())
        )
    }
}