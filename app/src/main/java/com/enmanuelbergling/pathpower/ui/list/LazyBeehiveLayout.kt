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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.pathpower.ui.shape.LayDownHexagon
import com.enmanuelbergling.pathpower.ui.theme.Honey
import kotlin.math.roundToInt

@Preview
@Composable
internal fun SimpleLazyBeehiveLayout() {
    val columns = 3

    val spaceBetween = 12.dp

    val itemWidthWeight = 1f / (1f + (columns - 1).times(.75f))

    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Layout(
            content = {
                beeItems(18) {
                    Box(
                        modifier = Modifier
                            .size(maxWidth.times(itemWidthWeight).minus(spaceBetween))
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

                val spaceBetweenPx = with(density) { spaceBetween.toPx().roundToInt() }
                layout(
                    constraints.maxWidth,
                    constraints.maxHeight
                ) {
                    groupedPlaceableList.forEachIndexed { index, placeables ->
                        placeBeehiveRow(
                            placeableList = placeables,
                            offsetY = index * (placeables.first().height / 2.1f + spaceBetweenPx).roundToInt(),
                            isEvenRow = index % 2 == 0,
                            spaceBetweenPx = spaceBetweenPx
                        )
                    }
                }
            }
        )
    }
}

private fun Placeable.PlacementScope.placeBeehiveRow(
    placeableList: List<Placeable>,
    offsetY: Int,
    isEvenRow: Boolean,
    spaceBetweenPx: Int = 0,
) =
    placeableList.forEachIndexed { index, placeable ->
        //stick to left bound, larger row when there a odd amount of rows
        if (isEvenRow) {
            val placeableWidth = if (index == 0) placeable.width
            else placeable.width.times(1.5).roundToInt() + (index * 2 * spaceBetweenPx)

            placeable.place(
                y = offsetY,
                x = placeableWidth * index
            )
        } else {
            val placeableWidth =
                placeable.width.times(1.5).roundToInt() + ((index * 2 + 1) * spaceBetweenPx)

            placeable.place(
                y = offsetY,
                x = if (index == 0) spaceBetweenPx + placeable.width.times(.75).roundToInt()
                else placeableWidth * index + placeable.width.times(.75).roundToInt()
            )
        }
    }