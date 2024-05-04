package com.enmanuelbergling.path_power.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.path_power.ui.shape.Hexagon
import com.enmanuelbergling.path_power.util.Honey
import kotlin.math.roundToInt

@Preview
@Composable
internal fun LazyBeehiveLayout() {
    val columns = 7

    val spaceEvenly = 2.dp

    val itemWidthWeight by remember(columns) {
        mutableFloatStateOf(
            1f / (1f + (columns - 1).times(.75f))
        )
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Layout(
            content = {
                repeat(12) {
                    Box(
                        modifier = Modifier
                            .size(
                                maxWidth
                                    .times(itemWidthWeight)
                            )
                            .padding(spaceEvenly)
                            .clip(Hexagon)
                            .background(Honey)
                    )
                }
            },
            measurePolicy = { measurableList, constraints ->
                val placeableList = measurableList.map { it.measure(constraints) }

                val groupedPlaceableList = groupBeehiveItems(placeableList, columns)

                layout(
                    constraints.maxWidth,
                    constraints.maxHeight
                ) {
                    groupedPlaceableList.forEachIndexed { index, placeableList1 ->
                        placeBeehiveRow(
                            placeableList = placeableList1,
                            offsetY = index * (placeableList1.first().height / 2.07f).roundToInt(),
                            isEvenRow = index % 2 == 0,
                        )
                    }
                }
            }
        )
    }
}

@Composable
internal fun LazyBeehiveRowLayout(
    modifier: Modifier = Modifier,
    isEvenRow: Boolean = true,
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
                    isEvenRow = isEvenRow,
                )
            }
        }
    )
}

private fun Placeable.PlacementScope.placeBeehiveRow(
    placeableList: List<Placeable>,
    offsetY: Int,
    isEvenRow: Boolean,
) =
    placeableList.forEachIndexed { index, placeable ->
        val placeableWidth = placeable.width.times(1.5).roundToInt()
        //stick to left bound, larger row when there a odd amount of rows
        val placeableXPosition = placeableWidth * index
        if (isEvenRow) {
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

/**
 * split a list of items into smaller lists for each row
 * */
fun <T : Any> groupBeehiveItems(originalItems: List<T>, columns: Int): List<List<T>> {
    val groupedList = mutableListOf<List<T>>()

    val listQueue = originalItems.toMutableList()

    fun takeFirst(n: Int) {
        val taken = listQueue.take(n)
        groupedList.add(
            taken
        )
        taken.forEach(listQueue::remove)
    }

    val areColumnsEven = columns % 2 == 0

    while (listQueue.isNotEmpty()) {
        if (areColumnsEven) {
            takeFirst(columns / 2)
        } else {
            val isEvenRow = groupedList.count() % 2 == 0
            val takenCount = if (isEvenRow) columns.div(2).inc()
            else columns.div(2)

            takeFirst(takenCount)
        }
    }

    return groupedList.toList()
}