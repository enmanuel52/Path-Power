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
internal fun WaterDrop(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(400.dp, 600.dp)) {
        val path = waterDropPath(size)
        drawPath(
            path, Brush.verticalGradient(
                listOf(Color.White, Color.Blue, Color.Blue, Color.Blue)
            )
        )
    }
}

internal fun waterDropPath(size: Size) =
    Path().apply {
        moveTo(size.width / 2, 0f)
        cubicTo(
            x1 = size.width.times(.4f), y1 = size.height.times(.35f),
            x2 = 0f, y2 = size.height.times(.45f),
            x3 = 0f, y3 = size.height.times(.75f)
        )
        cubicTo(
            x1 = 0f, y1 = size.height.times(.8f),
            x2 = size.width.times(.1f), y2 = size.height,
            x3 = size.width.times(.5f), y3 = size.height
        )
        cubicTo(
            x1 = size.width.times(.8f), y1 = size.height,
            x2 = size.width, y2 = size.height.times(.9f),
            x3 = size.width, y3 = size.height.times(.75f)
        )
        cubicTo(
            x1 = size.width, y1 = size.height.times(.45f),
            x2 = size.width.times(.6f), y2 = size.height.times(.35f),
            x3 = size.width / 2, y3 = 0f
        )
    }