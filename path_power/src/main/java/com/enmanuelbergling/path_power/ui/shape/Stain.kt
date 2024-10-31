package com.enmanuelbergling.path_power.ui.shape

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class Stain(
    private val roundPercentage: Float = .2f,
    private val innerRoundPercentage: Float = .08f,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ) = Outline.Generic(
        getStainPath(
            roundPercentage = roundPercentage,
            innerRoundPercentage = innerRoundPercentage,
            size = size,
        )
    )
}


@Preview
@Composable
private fun StainShape() {
    Canvas(modifier = Modifier.size(200.dp)) {
        val roundPercentage = .2f
        val innerRoundPercentage = .08f

        val stainPath = getStainPath(roundPercentage, innerRoundPercentage, size)

        drawPath(stainPath, Color.Magenta)
    }
}


private fun getStainPath(
    roundPercentage: Float,
    innerRoundPercentage: Float,
    size: Size,
) = Path().apply {

    moveTo(0f, size.height.times(roundPercentage))

    //top
    quadraticTo(
        0f,
        0f,
        size.width.times(roundPercentage),
        0f
    )

    cubicTo(
        size.width.times(roundPercentage * 1.5f),
        0f,
        size.width.times(.4f),
        size.height.times(innerRoundPercentage),
        size.width.times(.5f),
        size.height.times(innerRoundPercentage),
    )

    cubicTo(
        size.width.times(.6f),
        size.height.times(innerRoundPercentage),
        size.width.times(1 - (roundPercentage * 1.5f)),
        0f,
        size.width.times(1 - roundPercentage),
        0f
    )

    quadraticTo(
        size.width,
        0f,
        size.width,
        size.height.times(roundPercentage)
    )

    //end
    cubicTo(
        x1 = size.width,
        y1 = size.height.times(roundPercentage * 1.5f),
        x2 = size.width.times(1 - innerRoundPercentage),
        y2 = size.height.times(.4f),
        x3 = size.width.times(1 - innerRoundPercentage),
        y3 = size.height.times(.5f),
    )

    cubicTo(
        x1 = size.width.times(1 - innerRoundPercentage),
        y1 = size.height.times(.6f),
        x2 = size.width,
        y2 = size.height.times(1 - roundPercentage * 1.5f),
        x3 = size.width,
        y3 = size.height.times(1 - roundPercentage)
    )

    //bottom
    quadraticTo(
        size.width,
        size.height,
        size.width.times(1 - roundPercentage),
        size.height
    )

    cubicTo(
        size.width.times(1 - (roundPercentage * 1.5f)),
        size.height,
        size.width.times(.6f),
        size.height.times(1 - innerRoundPercentage),
        size.width.times(.5f),
        size.height.times(1 - innerRoundPercentage),
    )

    cubicTo(
        size.width.times(.4f),
        size.height.times(1 - innerRoundPercentage),
        size.width.times(roundPercentage * 1.5f),
        size.height,
        size.width.times(roundPercentage),
        size.height
    )

    quadraticTo(
        0f,
        size.height,
        0f,
        size.height.times(1 - roundPercentage)
    )

    //start
    cubicTo(
        x1 = 0f,
        y1 = size.height.times(1 - roundPercentage * 1.5f),
        x2 = size.width.times(innerRoundPercentage),
        y2 = size.height.times(.6f),
        x3 = size.width.times(innerRoundPercentage),
        y3 = size.height.times(.5f),
    )

    cubicTo(
        x1 = size.width.times(innerRoundPercentage),
        y1 = size.height.times(.4f),
        x2 = 0f,
        y2 = size.height.times(roundPercentage * 1.5f),
        x3 = 0f,
        y3 = size.height.times(roundPercentage)
    )

    close()
}