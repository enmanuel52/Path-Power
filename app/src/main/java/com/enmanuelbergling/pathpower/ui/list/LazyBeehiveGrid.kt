package com.enmanuelbergling.pathpower.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * Extending LazyColumn where two row are placed as Beehive
 * */
@Composable
fun <T : Any> LazyBeehiveVerticalGrid(
    items: List<T>,
    gridCells: BeehiveGridCells,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    key: ((rowIndex: Int, List<T>) -> Any)? = null,
    spaceBetween: Dp = 4.dp,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    userScrollEnabled: Boolean = true,
    itemContent: @Composable ColumnScope.(T) -> Unit,
) {
    BoxWithConstraints {
        val columns by remember(gridCells, maxWidth) {
            mutableIntStateOf(
                when (gridCells) {
                    is BeehiveGridCells.Adaptive -> (maxWidth / gridCells.minSize).toInt()
                    is BeehiveGridCells.Fixed -> gridCells.count
                }
            )
        }

        LazyBeehive(
            items = items,
            columns = columns,
            modifier = modifier,
            state = state,
            key = key,
            spaceBetween = spaceBetween,
            contentPadding = contentPadding,
            verticalAlignment = verticalAlignment,
            horizontalAlignment = horizontalAlignment,
            userScrollEnabled = userScrollEnabled,
            itemContent = itemContent
        )
    }
}

/**
 * It is like [LazyColumn] where two row are placed as Beehive
 * */
@Composable
internal fun <T : Any> LazyBeehive(
    items: List<T>,
    columns: Int,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    key: ((rowIndex: Int, List<T>) -> Any)? = null,
    spaceBetween: Dp = 4.dp,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    userScrollEnabled: Boolean = true,
    itemContent: @Composable ColumnScope.(T) -> Unit,
) {
    val itemWidthWeight: Float by remember(columns) {
        mutableFloatStateOf(
            1f / (1f + (columns - 1).times(.75f))
        )
    }

    val groupedList by remember(columns, items) {
        derivedStateOf {
            groupBeehiveItems(items, columns)
        }
    }

    BoxWithConstraints {
        val itemSize by remember(maxWidth, itemWidthWeight) {
            mutableStateOf(
                maxWidth * itemWidthWeight
            )
        }
        val evenRowMaxCount by remember(columns) {
            mutableIntStateOf(
                columns.div(2.0).roundToInt()
            )
        }

        LazyColumn(
            modifier = modifier,
            state = state,
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(
                -itemSize / 2 + spaceBetween, verticalAlignment
            ),
            horizontalAlignment = horizontalAlignment,
            userScrollEnabled = userScrollEnabled,
        ) {
            itemsIndexed(groupedList, key = key) { index, rowItems ->
                val isEvenRow by remember(columns) {
                    derivedStateOf {
                        index % 2 == 0 || columns == 1
                    }
                }

                val rowMaxCount by remember(isEvenRow, columns) {
                    mutableIntStateOf(
                        if (isEvenRow) evenRowMaxCount
                        else columns / 2
                    )
                }

                val goThrough by remember(columns) {
                    derivedStateOf {
                        (isEvenRow && columns % 2 == 1) || (!isEvenRow && columns % 2 == 0)
                    }
                }

                BeehiveRow(
                    rowItems,
                    modifier = Modifier.fillMaxWidth(),
                    startsOnZero = isEvenRow,
                    evenRowMaxCount = evenRowMaxCount,
                    itemsMaxCount = rowMaxCount,
                    spaceBetween = spaceBetween,
                    goThrough = goThrough,
                    itemContent = itemContent
                )
            }
        }
    }
}

/**
 * It is like [GridCells]
 * */
sealed interface BeehiveGridCells {
    /**
     * It is like [GridCells.Fixed]
     * */
    data class Fixed(val count: Int) : BeehiveGridCells {
        init {
            require(count > 0) { "Provided count $count should be larger than zero" }
        }
    }

    /**
     * It is like [GridCells.Adaptive]
     * */
    data class Adaptive(val minSize: Dp) : BeehiveGridCells {
        init {
            require(minSize > 0.dp) { "Provided min size $minSize should be larger than zero." }
        }
    }
}