package com.enmanuelbergling.pathpower.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.pathpower.ui.theme.LighterHoney
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Preview
@Composable
internal fun LazyBeehiveGridExample() {

    val scope = rememberCoroutineScope()

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    var beehiveCells: BeehiveGridCells by remember {
        mutableStateOf(
            BeehiveGridCells.Fixed(3)
        )
    }

    var isCellSettingsSheetOpen by remember {
        mutableStateOf(false)
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        floatingActionButton = {
            SmallFloatingActionButton(onClick = { isCellSettingsSheetOpen = true }) {
                Icon(imageVector = Icons.Rounded.Settings, contentDescription = "settings icon")
            }
        },
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
        }
    ) {

        LazyBeehiveVerticalGrid(
            items = (1..40).toList(),
            gridCells = beehiveCells,
            key = { rowIndex, _ -> rowIndex },
            spaceBetween = 6.dp,
            modifier = Modifier
                .testTag("beehive")
                .fillMaxSize()
                .padding(it),
            contentPadding = PaddingValues(4.dp)
        ) { item ->
            ElevatedCard(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            "Bee number $item clicked",
                            withDismissAction = true
                        )
                    }
                },
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.elevatedCardColors(LighterHoney)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.bee),
//                        contentDescription = "bee image",
//                        Modifier.size(50.dp)
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Item $item")
                }
            }
        }
    }

    if (isCellSettingsSheetOpen) {
        CellSheetSettings(
            onDismiss = { isCellSettingsSheetOpen = false },
            cells = beehiveCells,
            onCellsChange = { cells: BeehiveGridCells -> beehiveCells = cells }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CellSheetSettings(
    onDismiss: () -> Unit,
    cells: BeehiveGridCells,
    onCellsChange: (BeehiveGridCells) -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(horizontal = 12.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                InputChip(
                    selected = cells is BeehiveGridCells.Fixed,
                    onClick = { onCellsChange(BeehiveGridCells.Fixed(2)) },
                    label = { Text(text = "Fixed") }
                )

                if (cells is BeehiveGridCells.Fixed) {
                    (2..4).forEach { count ->
                        InputChip(
                            selected = cells == BeehiveGridCells.Fixed(count),
                            onClick = { onCellsChange(BeehiveGridCells.Fixed(count)) },
                            label = { Text(text = "$count columns") }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                InputChip(
                    selected = cells is BeehiveGridCells.Adaptive,
                    onClick = { onCellsChange(BeehiveGridCells.Adaptive(90.dp)) },
                    label = { Text(text = "Adaptive") }
                )
                if (cells is BeehiveGridCells.Adaptive) {
                    listOf(90.dp, 120.dp, 160.dp).forEach { minSize ->
                        InputChip(
                            selected = cells == BeehiveGridCells.Adaptive(minSize),
                            onClick = { onCellsChange(BeehiveGridCells.Adaptive(minSize)) },
                            label = { Text(text = "${minSize.value}.dp") }
                        )
                    }
                }
            }
        }

    }
}

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
    @Stable
    data class Adaptive(val minSize: Dp) : BeehiveGridCells {
        init {
            require(minSize > 0.dp) { "Provided min size $minSize should be larger than zero." }
        }
    }
}