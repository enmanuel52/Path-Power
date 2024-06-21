package com.enmanuelbergling.path_power.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import kotlin.random.Random

data class BeehiveCells(
    val label: String,
    val gridCells: BeehiveGridCells,
) {
    companion object {
        val BEEHIVE_CELLS_MOCK = listOf(
            BeehiveCells("2 columns", BeehiveGridCells.Fixed(2)),
            BeehiveCells("3 columns", BeehiveGridCells.Fixed(3)),
            BeehiveCells("5 columns", BeehiveGridCells.Fixed(5)),
            BeehiveCells("60 dp", BeehiveGridCells.Adaptive(60.dp)),
            BeehiveCells("90 dp", BeehiveGridCells.Adaptive(90.dp)),
            BeehiveCells("120 dp", BeehiveGridCells.Adaptive(120.dp)),
        )
    }
}

@Composable
fun BasicBeehiveExample() {
    var selectedGridCellsIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    Scaffold { paddingValues ->
        Column {

            LazyRow(
                contentPadding = PaddingValues(6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                itemsIndexed(BeehiveCells.BEEHIVE_CELLS_MOCK) { index, item ->
                    FilterChip(
                        selected = selectedGridCellsIndex == index,
                        onClick = { selectedGridCellsIndex = index },
                        label = { Text(item.label) },
                    )
                }
            }

            LazyBeehiveVerticalGrid(
                items = (1..120).toList(),
                gridCells = BeehiveCells.BEEHIVE_CELLS_MOCK[selectedGridCellsIndex].gridCells,
                spaceBetween = 8.dp,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                aspectRatio = 1.05f,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawBehind {
                            drawRect(Color(Random.nextLong(0xFFFFFFFF)))
                        }
                ) {
                    Text(text = "Item $it", modifier = Modifier.align(Alignment.Center))
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun SimplestBeehiveExample() {
    var columns by remember {
        mutableIntStateOf(2)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "$columns columns") },
                actions = {
                    TextButton(onClick = { columns-- }, enabled = columns > 1) {
                        Text(text = "Decrease")
                    }
                    IconButton(onClick = { columns++ }, enabled = columns < 6) {
                        Icon(imageVector = Icons.Rounded.Add, contentDescription = "add icon")
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyBeehive(
            items = (1..120).toList(),
            columns = columns,
            spaceBetween = 8.dp,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            aspectRatio = 1.05f,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawRect(Color(Random.nextLong(0xFFFFFFFF)))
                    }
            ) {
                Text(text = "Item $it", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

/**
 * Extending LazyColumn where two row are placed as Beehive
 * @param aspectRatio the desired width/height positive ratio
 * */
@Composable
fun <T : Any> LazyBeehiveVerticalGrid(
    items: List<T>,
    gridCells: BeehiveGridCells,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    key: ((rowIndex: Int, List<T>) -> Any)? = null,
    spaceBetween: Dp = 4.dp,
    aspectRatio: Float = 1f,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    centerHorizontal: Boolean = false,
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
            aspectRatio = aspectRatio,
            contentPadding = contentPadding,
            verticalAlignment = verticalAlignment,
            centerHorizontal = centerHorizontal,
            userScrollEnabled = userScrollEnabled,
            isUp = false,
            itemContent = itemContent
        )
    }
}

/**
 * It is like [LazyColumn] where two row are placed as Beehive
 * @param aspectRatio the desired width/height positive ratio
 * */
@Composable
internal fun <T : Any> LazyBeehive(
    items: List<T>,
    columns: Int,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    key: ((rowIndex: Int, List<T>) -> Any)? = null,
    spaceBetween: Dp = 4.dp,
    aspectRatio: Float = 1f,
    isUp: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(4.dp),
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    centerHorizontal: Boolean = false,
    userScrollEnabled: Boolean = true,
    itemContent: @Composable ColumnScope.(T) -> Unit,
) {
    require(columns > 1) { "Provided count $columns should be larger than one" }

    val itemWidthWeight: Float by rememberWidthWeight(columns)

    val groupedList by remember(columns, items, centerHorizontal) {
        derivedStateOf {
            //duplicate fist row items and ignore the first one because start at left
            val itemsToSplit = if (centerHorizontal) {
                val skippableItems = items.take((columns / 2f).roundToInt())
                skippableItems + items
            } else items
            groupBeehiveItems(itemsToSplit, columns)
        }
    }

    BoxWithConstraints {
        val itemWidth by remember(maxWidth, itemWidthWeight) {
            mutableStateOf(
                maxWidth * itemWidthWeight
            )
        }

        val itemHeight by remember(itemWidth, aspectRatio) {
            derivedStateOf {
                itemWidth / aspectRatio
            }
        }

        LazyColumn(
            modifier = modifier,
            state = state,
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(
                space =
                if (isUp) -itemHeight * .3f + spaceBetween
                else -itemHeight / 2 + spaceBetween,
                alignment = verticalAlignment
            ),
            userScrollEnabled = userScrollEnabled,
        ) {
            itemsIndexed(groupedList, key = key) { index, rowItems ->
                //ignore items previously duplicated
                if (centerHorizontal && index == 0) {
                    return@itemsIndexed
                }

                val isEvenRow by remember {
                    derivedStateOf {
                        index % 2 == 0
                    }
                }

                val rowMaxCount by remember(isEvenRow, columns) {
                    mutableIntStateOf(
                        if (isEvenRow) columns.div(2.0).roundToInt()
                        else columns / 2
                    )
                }

                val goThrough by remember(columns) {
                    derivedStateOf {
                        val columnsOdd = columns % 2 == 1
                        //(isEvenRow && columnsOdd) || (!isEvenRow && !columnsOdd)
                        isEvenRow == columnsOdd
                    }
                }

                if (isUp) {
                    UpBeehiveRow(
                        items = rowItems,
                        modifier = Modifier.fillMaxWidth(),
                        startsOnZero = isEvenRow,
                        itemsMaxCount = rowMaxCount,
                        spaceBetween = spaceBetween,
                        goThrough = goThrough,
                        aspectRatio = aspectRatio,
                        itemContent = itemContent
                    )
                } else {
                    BeehiveRow(
                        items = rowItems,
                        modifier = Modifier.fillMaxWidth(),
                        startsOnZero = isEvenRow,
                        itemsMaxCount = rowMaxCount,
                        spaceBetween = spaceBetween,
                        goThrough = goThrough,
                        aspectRatio = aspectRatio,
                        itemContent = itemContent
                    )
                }
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
            require(count > 1) { "Provided count $count should be larger than one" }
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