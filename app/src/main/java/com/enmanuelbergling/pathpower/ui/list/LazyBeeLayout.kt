package com.enmanuelbergling.pathpower.ui.list

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.foundation.lazy.layout.LazyLayoutPrefetchState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.pathpower.TAG
import com.enmanuelbergling.pathpower.ui.shape.LayDownHexagon
import com.enmanuelbergling.pathpower.ui.theme.Honey
import kotlin.math.roundToInt

//@Preview
@Composable
fun SimpleLazyBeeLayout() {

    Layout(
        content = {
            repeat(10) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(LayDownHexagon)
                        .background(Honey)
                )
            }
        },
        measurePolicy = { measurables, constraints ->
            val placeables = measurables.map { it.measure(constraints) }

            layout(
                constraints.maxWidth,
                constraints.maxHeight
            ) {
                placeables.forEachIndexed { index, placeable ->
                    placeable.place(
                        y = index * placeable.height / 2,
                        x = if (index % 2 == 0) 0
                        else placeable.width.times(.8).roundToInt()
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun LazyBeeLayoutPrev() {
    val columns = 4

    val itemWidthWeight = 1f / (1f + (columns - 1).times(.75f))

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
    ) {

        LazyBeeLazyLayout(
            columns = columns,
            modifier = Modifier
        ) {
            beeItems(28) {
                Box(
                    modifier = Modifier
                        .width(maxWidth.times(itemWidthWeight))
                        .aspectRatio(13f.div(12))
                        .clip(LayDownHexagon)
                        .background(Honey)
                )
            }
        }
    }
}

@Composable
fun LazyBeeLayout(
    columns: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {

    Layout(
        content = content,
        modifier = modifier,
        measurePolicy = { measurableList, constraints ->

            val placeableList = measurableList.map { it.measure(constraints) }

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

            Log.d(
                TAG,
                "LazyBeeLayout: ${groupedPlaceableList.count()} \n ${groupedPlaceableList.map { it.count() }}"
            )

            layout(
                constraints.maxWidth,
                constraints.maxHeight
            ) {
                groupedPlaceableList.forEachIndexed { groupIndex, placeableList ->
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
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyBeeLazyLayout(
    columns: Int,
    modifier: Modifier = Modifier,
    itemCount: Int = 28,
    content: () -> LazyLayoutItemProvider,
) {

    val scrollState = rememberScrollState()

    LazyLayout(
        itemProvider = content(),
        prefetchState = LazyLayoutPrefetchState(),
        modifier = modifier.verticalScroll(scrollState),
        measurePolicy = { constraints ->

            val placeableList = (1..itemCount).map { measure(it, constraints) }.flatten()

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
                groupedPlaceableList.forEachIndexed { groupIndex, placeableList ->
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
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
private fun beeItems(count: Int, content: @Composable (index: Int) -> Unit) =
    object : LazyLayoutItemProvider {
        override val itemCount: Int
            get() = count

        @Composable
        override fun Item(index: Int, key: Any) {
            content(index)
        }
    }