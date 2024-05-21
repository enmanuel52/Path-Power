package com.enmanuelbergling.path_power.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.path_power.ui.shape.Hexagon
import com.enmanuelbergling.path_power.util.Honey
import com.enmanuelbergling.path_power.util.LighterHoney
import kotlin.math.roundToInt

@Preview
@Composable
fun LazyBeehiveGridLayoutPrev(
    modifier: Modifier = Modifier,
) {
    val columns = 7

    val itemWidthWeight by rememberWidthWeight(columns)

    BoxWithConstraints(modifier) {

        LazyBeehiveGridLayout(
            columns,
            startTopLeft = false,
        ) {
            repeat(15) {
                Box(
                    modifier = Modifier
                        .size(maxWidth.times(itemWidthWeight))
                        .background(Honey, Hexagon),
                )
            }
        }
    }

}

@Composable
fun LazyBeehiveGridLayout(
    columns: Int,
    modifier: Modifier = Modifier,
    startTopLeft: Boolean = true,
    content: @Composable @UiComposable () -> Unit,
) {
    Layout(
        modifier = modifier,
        content = content,
        measurePolicy = { measurableList, constraints ->
            val placeableList = measurableList.map { it.measure(constraints) }

            val groupedPlaceableList = groupBeehiveItems(
                originalItems = placeableList,
                columns = columns,
                startAtLeft = startTopLeft
            )

            layout(
                constraints.maxWidth,
                constraints.maxHeight
            ) {
                groupedPlaceableList.forEachIndexed { rowIndex, placeables ->
                    val isEvenRow = rowIndex % 2 == 0

                    val placelableHeight = placeables.first().height

                    placeBeehiveRow(
                        placeableList = placeables,
                        offsetY = placelableHeight * rowIndex / 2,
                        startLeftEdge = if (startTopLeft) isEvenRow else !isEvenRow,
                    )
                }
            }
        }
    )
}

@Composable
internal fun LazyBeehiveRowLayout(
    modifier: Modifier = Modifier,
    startLeftEdge: Boolean = true,
    content: @Composable @UiComposable () -> Unit,
) {
    Layout(
        modifier = modifier,
        content = content,
        measurePolicy = { measurableList, constraints ->
            val placeableList = measurableList.map { it.measure(constraints) }

            layout(
                constraints.maxWidth,
                constraints.maxHeight
            ) {
                placeBeehiveRow(
                    placeableList = placeableList,
                    offsetY = 0,
                    startLeftEdge = startLeftEdge,
                )
            }
        }
    )
}

@Preview
@Composable
private fun LazyBeehiveRowLayoutPrev() {
    val columns = 7

    val itemWidthWeight by rememberWidthWeight(columns)

    BoxWithConstraints(Modifier.heightIn(max = 80.dp)) {
        LazyBeehiveRowLayout(startLeftEdge = true) {
            repeat(columns / 2) {
                Box(
                    modifier = Modifier
                        .size(maxWidth.times(itemWidthWeight))
                        .background(LighterHoney, Hexagon),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "item $it")
                }
            }
        }

    }
}

private fun Placeable.PlacementScope.placeBeehiveRow(
    placeableList: List<Placeable>,
    offsetY: Int,
    startLeftEdge: Boolean,
) = placeableList.forEachIndexed { index, placeable ->
    val placeableWidth = placeable.width.times(1.5).roundToInt()
    //stick to left bound, larger row when there a odd amount of rows
    val placeableXPosition = placeableWidth * index
    if (startLeftEdge) {
        placeable.place(
            y = offsetY,
            x = placeableXPosition
        )
    } else {
        placeable.place(
            y = offsetY,
            x = placeableXPosition + placeableWidth.times(.5).roundToInt()
        )
    }
}