package com.enmanuelbergling.pathpower.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.pathpower.ui.shape.Hexagon
import kotlin.math.roundToInt
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
internal fun SimpleExample() {
    var columns by remember {
        mutableIntStateOf(6)
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

        LazyBeehiveLayout(
            items = (1..120).toList(),
            columns = columns,
            spaceBetween = 8.dp,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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

//@Preview
@Composable
fun SimpleLazyListExample() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy((-50).dp)
    ) {
        items(120) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(8.dp))
                .drawBehind {
                    drawRect(Color(Random.nextLong(0xFFFFFFFF)))
                }
            )
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
            aspectRatio = aspectRatio,
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
                    items = rowItems,
                    modifier = Modifier.fillMaxWidth(),
                    startsOnZero = isEvenRow,
                    evenRowMaxCount = evenRowMaxCount,
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

/**
 * It is like [LazyColumn] where two row are placed as Beehive
 * */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun <T : Any> LazyBeehiveLayout(
    items: List<T>,
    columns: Int,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    key: ((rowIndex: Int, List<T>) -> Any)? = null,
    spaceBetween: Dp = 4.dp,
    aspectRatio: Float = 1f,
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

    BoxWithConstraints(
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
        }
    ) {
        val itemWidth by remember(maxWidth, itemWidthWeight) {
            mutableStateOf(
                maxWidth * itemWidthWeight
            )
        }

        LazyColumn(
            modifier = modifier.testTag("beehive"),
            state = state,
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(
                -itemWidth / 2 + spaceBetween / 2, verticalAlignment
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

                LazyBeehiveRowLayout(
                    modifier = Modifier
                        .height(itemWidth / aspectRatio),
                    isEvenRow = isEvenRow,
                ) {
                    rowItems.forEach {
                        Column(
                            modifier = Modifier
                                .width(itemWidth)
                                .padding(horizontal = spaceBetween / 2)
                                .clip(Hexagon)
                        ) {
                            itemContent(it)
                        }
                    }
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