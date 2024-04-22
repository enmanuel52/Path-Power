package com.enmanuelbergling.pathpower.ui.shape

import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.pathpower.ui.theme.Honey

data object Hexagon : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline = Outline.Generic(getHexagonPath(size))
}

@Preview
@Composable
private fun Hexagon() {
    Canvas(modifier = Modifier.size(200.dp, 180.dp)) {
        val hexagon = getHexagonPath(size)

        drawPath(hexagon, Honey)
    }
}

/**
 * @param round for corners
 * */
private fun getHexagonPath(
    size: Size,
    @FloatRange(
        from = .009,
        to = .031,
    ) round: Float = .03f,
) =
    Path().apply {
        moveTo(size.width * round, size.height * (.5f + round * 2))

        quadraticBezierTo(
            x1 = 0f,
            y1 = size.height * .5f,
            x2 = size.width * round,
            y2 = size.height * (.5f - round * 2)
        )

        lineTo(x = size.width * (.25f - round), y = size.height * round)

        quadraticBezierTo(
            x1 = size.width * .25f,
            y1 = 0f,
            x2 = size.width * (.25f + round),
            y2 = 0f
        )

        lineTo(size.width * (.75f - round), 0f)

        quadraticBezierTo(
            x1 = size.width * .75f,
            y1 = 0f,
            x2 = size.width * (.75f + round),
            y2 = size.height * round
        )

        lineTo(
            x = size.width * (1f - round),
            y = size.height * (.5f - round * 2)
        )

        quadraticBezierTo(
            x1 = size.width,
            y1 = size.height * .5f,
            x2 = size.width * (1f - round),
            y2 = size.height * (.5f + round * 2)
        )

        lineTo(
            x = size.width * (.75f + round),
            y = size.height * (1f - round)
        )

        quadraticBezierTo(
            x1 = size.width * .75f,
            y1 = size.height,
            x2 = size.width * (.75f - round),
            y2 = size.height
        )

        lineTo(
            x = size.width * (.25f + round),
            y = size.height
        )

        quadraticBezierTo(
            x1 = size.width * .25f,
            y1 = size.height,
            x2 = size.width * (.25f - round),
            y2 = size.height * (1f - round)
        )

        close()
    }

private fun getRectHexagonPath(size: Size) =
    Path().apply {

        moveTo(size.width * .25f, 0f)

        lineTo(size.width * .75f, 0f)

        lineTo(size.width, size.height * .5f)


        lineTo(size.width * .75f, size.height)

        lineTo(size.width * .25f, size.height)


        lineTo(0f, size.height * .5f)

        close()
    }
