package com.enmanuelbergling.path_power.ui.shape

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
import com.enmanuelbergling.path_power.util.Honey

data object HexagonUp : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline = Outline.Generic(getHexagonPath(size))
}

@Preview
@Composable
private fun Hexagon() {
    Canvas(modifier = Modifier.size(180.dp, 210.dp)) {
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
    ) round: Float = .015f,
) =
    Path().apply {
        moveTo(y = size.height * round, x = size.width * (.5f + round * 2))

        quadraticTo(
            x1 = size.width * .5f,
            y1 = 0f,
            x2 = size.width * (.5f - round * 2),
            y2 = size.height * round
        )

        lineTo(y = size.height * (.25f - round), x = size.width * round)

        quadraticTo(
            x1 = 0f,
            y1 = size.height * .25f,
            x2 = 0f,
            y2 = size.height * (.25f + round)
        )

        lineTo(y = size.height * (.75f - round), x = 0f)

        quadraticTo(
            x1 = 0f,
            y1 = size.height * .75f,
            x2 = size.width * round,
            y2 = size.height * (.75f + round)
        )

        lineTo(
            y = size.height * (1f - round),
            x = size.width * (.5f - round * 2)
        )

        quadraticTo(
            x1 = size.width * .5f,
            y1 = size.height,
            x2 = size.width * (.5f + round * 2),
            y2 = size.height * (1f - round)
        )

        lineTo(
            y = size.height * (.75f + round),
            x = size.width * (1f - round)
        )

        quadraticTo(
            x1 = size.width,
            y1 = size.height * .75f,
            x2 = size.width,
            y2 = size.height * (.75f - round)
        )

        lineTo(
            y = size.height * (.25f + round),
            x = size.width
        )

        quadraticTo(
            x1 = size.width,
            y1 = size.height * .25f,
            x2 = size.width * (1f - round),
            y2 = size.height * (.25f - round)
        )

        close()


    }

private fun getRectHexagonPath(size: Size) =
    Path().apply {

        moveTo(0f, size.height * .25f)

        lineTo(size.width * .5f, 0f)

        lineTo(size.width, size.height * .25f)
        lineTo(size.width, size.height * .75f)


        lineTo(size.width * .5f, size.height)

        lineTo(0f, size.height * .75f)

        lineTo(0f, size.height * .25f)

        close()
    }
