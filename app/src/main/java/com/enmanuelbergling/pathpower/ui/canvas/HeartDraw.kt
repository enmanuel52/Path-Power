package com.enmanuelbergling.pathpower.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun Heart(
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier.size(500.dp, 380.dp)) {
        val path = heartPath(size)
        drawPath(
            path, Brush.verticalGradient(
                listOf(Color.Red, Color.Red, Color.Red, Color.Magenta)
            )
        )
    }
}


fun heartPath(size: Size) =
    Path().apply {
        moveTo(size.width / 2, size.height.times(.15f))
        cubicTo(
            size.width.times(.3f), 0f,
            size.width.times(.1f), 0f,
            0f, size.height.times(.25f)
        )
        cubicTo(
            0f, size.height.times(.4f),
            0f, size.height.times(.6f),
            size.width / 2, size.height
        )
        cubicTo(
            size.width, size.height.times(.6f),
            size.width, size.height.times(.4f),
            size.width, size.height.times(.25f)
        )
        cubicTo(
            size.width.times(.9f), 0f,
            size.width.times(.7f), 0f,
            size.width / 2, size.height.times(.15f)
        )

    }