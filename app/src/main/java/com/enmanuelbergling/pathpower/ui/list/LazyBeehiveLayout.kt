package com.enmanuelbergling.pathpower.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.tooling.preview.Preview
import com.enmanuelbergling.pathpower.ui.shape.LayDownHexagon
import com.enmanuelbergling.pathpower.ui.theme.Honey
import kotlin.math.roundToInt

@Preview
@Composable
internal fun SimpleLazyBeehiveLayout() {
    val columns = 3

    val itemWidthWeight = 1f / (1f + (columns - 1).times(.75f))

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Layout(
            content = {
                repeat(18) {
                    Box(
                        modifier = Modifier
                            .size(maxWidth.times(itemWidthWeight))
                            .clip(LayDownHexagon)
                            .background(Honey)
                    )
                }
            },
            measurePolicy = { measurables, constraints ->
                val placeableList = measurables.map { it.measure(constraints) }

                val groupedPlaceableList = mutableListOf<List<Placeable>>()

                val placeableQueue = placeableList.toMutableList()

                fun takeFirst(n: Int) {
                    val taken = placeableQueue.take(n)
                    groupedPlaceableList.add(
                        taken
                    )
                    placeableQueue.removeAll(taken)
                }

                val areColumnsEven = columns % 2 == 0

                while (placeableQueue.isNotEmpty()) {
                    if (areColumnsEven) {
                        takeFirst(columns / 2)
                    } else {
                        val isEvenRow = groupedPlaceableList.count() % 2 == 0
                        val takenCount = if (isEvenRow) columns.div(2).inc()
                        else columns.div(2)

                        takeFirst(takenCount)
                    }
                }

                layout(
                    constraints.maxWidth,
                    constraints.maxHeight
                ) {
                    placeBeehive(groupedPlaceableList)
                }
            }
        )
    }
}

private fun Placeable.PlacementScope.placeBeehive(items: List<List<Placeable>>) =
    items.forEachIndexed { groupIndex, placeableList ->
        placeableList.forEachIndexed { index, placeable ->
            //stick to left bound
            if (groupIndex % 2 == 0) {

                val placeableWidth = if (index == 0) placeable.width
                else placeable.width.times(1.5).roundToInt()

                placeable.place(
                    y = groupIndex * placeable.height / 2,
                    x = placeableWidth * index
                )
            } else {
                val placeableWidth = placeable.width.times(1.5).roundToInt()

                placeable.place(
                    y = groupIndex * placeable.height / 2,
                    x = placeableWidth * index + placeable.width.times(.75)
                        .roundToInt()
                )
            }
        }
    }