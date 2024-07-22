package com.enmanuelbergling.path_power.ui.shape

import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun HoleRectPrev() {


    Canvas(modifier = Modifier.fillMaxWidth().height(90.dp)) {
        drawPath(
            getHoleRectPath(
                size = size,
                holeSizePx = 56.dp.toPx(),
                holesCount = 5,
                hole = 3,
                holeProgress = 1f,
                previousHole = -1,
                previousHoleProgress = 0f
            ),
            color = Color.Green,
            style = Fill// Stroke(1.dp.toPx())
        )
    }
}

/**
 * @param holeSizePx self explanatory :)
 * @param holesCount how many of they are
 * @param hole the selected one, 0-based index, must be minor than spots
 * @param holeProgress to control how deep the hole is
 * @param previousHole to previous selected one
 * @param previousHoleProgress to control how deep the previous hole is
 * */
fun getHoleRectPath(
    size: Size,
    holeSizePx: Float,
    holesCount: Int,
    hole: Int,
    @FloatRange(.0, 1.0) holeProgress: Float,
    previousHole: Int,
    @FloatRange(.0, 1.0) previousHoleProgress: Float,
) =
    if (hole >= holesCount) {
        throw IllegalArgumentException("holeSpot $hole must be minor that stops: $holesCount")
    } else Path().apply {
        moveTo(0f, 0f)

        //here is where the fun begins
        repeat(holesCount) { index ->
            val currentWidth = size.width * index / holesCount
            val nextWidthStep = size.width * (index + 1 ) / holesCount
            val middleXPoint = (currentWidth + nextWidthStep) / 2

            when (index) {
                hole -> {
                    getSingleHolePath(
                        holeSizePx = holeSizePx,
                        middleXPoint = middleXPoint,
                        deepProgress = holeProgress
                    )
                }
                previousHole -> {
                    getSingleHolePath(
                        holeSizePx = holeSizePx,
                        middleXPoint = middleXPoint,
                        deepProgress = previousHoleProgress
                    )
                }
                else -> {
                    if (index == holesCount-1){
                        lineTo(size.width, 0f)
                    }else{
                        val twoStepsAhead = size.width * (index + 2 ) / holesCount
                        val nextXPoint = (nextWidthStep + twoStepsAhead) / 2 - holeSizePx.times(1.2f)
                        lineTo(nextXPoint, 0f)
                    }
                }
            }
        }

        lineTo(size.width, 0f)
        lineTo(size.width, size.height)
        lineTo(0f, size.height)
        lineTo(0f, 0f)
    }

/**
 * @param deepProgress to control how deep the hole is
 * */
fun Path.getSingleHolePath(
    holeSizePx: Float,
    middleXPoint: Float,
    @FloatRange(.0, 1.0) deepProgress: Float,
){
    moveTo(x = middleXPoint - holeSizePx.times(1.2f), y = 0f)

    cubicTo(
        x3 = middleXPoint - holeSizePx.times(.2f),
        y3 = holeSizePx * 3 / 4 * deepProgress,
        x1 = middleXPoint - holeSizePx.times(.33f) - holeSizePx.times(.2f),
        y1 = holeSizePx.times(.1f) * deepProgress,
        x2 = middleXPoint - holeSizePx.times(.66f) - holeSizePx.times(.2f),
        y2 = holeSizePx * 2 / 4 * deepProgress,
    )

    quadraticBezierTo(
        x2 = middleXPoint + holeSizePx.times(.2f),
        y2 = holeSizePx * 3 / 4 * deepProgress,
        x1 = middleXPoint,
        y1 = holeSizePx.times(.8f) * deepProgress
    )

    cubicTo(
        x3 = middleXPoint + holeSizePx.times(1.2f),
        y3 = 0f,
        x2 = middleXPoint + holeSizePx.times(.33f) + holeSizePx.times(.2f),
        y2 = holeSizePx.times(.1f) * deepProgress,
        x1 = middleXPoint + holeSizePx.times(.66f) + holeSizePx.times(.2f),
        y1 = holeSizePx / 2 * deepProgress,
    )
}