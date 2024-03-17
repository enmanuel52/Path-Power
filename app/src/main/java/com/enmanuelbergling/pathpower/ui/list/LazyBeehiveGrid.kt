package com.enmanuelbergling.pathpower.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.pathpower.ui.shape.LayDownHexagon


@Preview
@Composable
internal fun LazyBeehiveGridPreview() {
    LazyBeehiveVerticalGrid(
        items = (1..40).toList(),
        gridCells = BeehiveGridCells.Adaptive(70.dp),
        spaceEvenly = 6.dp,
        modifier = Modifier.fillMaxSize()
    ) { item ->
        ElevatedCard(
            modifier = Modifier.fillMaxSize(),
            shape = LayDownHexagon,
            colors = CardDefaults.elevatedCardColors(MaterialTheme.colorScheme.secondary)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Item: $item")
            }
        }
    }
}

@Composable
internal fun <T : Any> LazyBeehiveVerticalGrid(
    items: List<T>,
    gridCells: BeehiveGridCells,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    key: ((rowIndex: Int, List<T>) -> Any)? = null,
    spaceEvenly: Dp = 4.dp,
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
            spaceEvenly = spaceEvenly,
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
    spaceEvenly: Dp = 4.dp,
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

    val groupedList by remember {
        derivedStateOf {
            groupBeehiveItems(items, columns)
        }
    }

    BoxWithConstraints {
        val itemSize=maxWidth * itemWidthWeight

        LazyColumn(
            modifier = modifier,
            state = state,
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(
                -itemSize.div(2.07f), verticalAlignment
            ),
            horizontalAlignment = horizontalAlignment,
            userScrollEnabled = userScrollEnabled,
        ) {
            itemsIndexed(groupedList, key = key) { index, rowItems ->
                LazyBeehiveRowLayout(
                    isEvenRow = index % 2 == 0,
                    modifier = Modifier.height(itemSize)
                ) {
                    rowItems.forEach { item ->
                        Column(
                            modifier = Modifier
                                .size(itemSize)
                                .padding(spaceEvenly)
                                .clip(LayDownHexagon),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            itemContent(item)
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