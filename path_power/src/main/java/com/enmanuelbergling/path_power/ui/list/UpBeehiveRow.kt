package com.enmanuelbergling.path_power.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.path_power.ui.shape.HexagonUp
import com.enmanuelbergling.path_power.util.Honey

@Composable
fun <T> UpBeehiveRow(
    items: List<T>,
    itemsMaxCount: Int,
    modifier: Modifier = Modifier,
    spaceBetween: Dp = 0.dp,
    startsOnZero: Boolean = true,
    goThrough: Boolean = false,
    aspectRatio: Float = 1f,
    itemContent: @Composable ColumnScope.(T) -> Unit,
) {

    val basicModifier = Modifier
        .clip(HexagonUp)
        .aspectRatio(aspectRatio)

    Row(horizontalArrangement = Arrangement.spacedBy(spaceBetween), modifier = modifier) {
        val hiveModifier = basicModifier then Modifier
            .weight(1f)

        repeat(itemsMaxCount) { index ->
            if (index == 0) {
                if (!startsOnZero) {
                    Spacer(
                        modifier = Modifier
                            .weight(.5f)
                    )
                }

                Column(
                    modifier = hiveModifier,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items.getOrNull(0)?.let { itemContent(it) }
                }
            } else {
                val item by remember(items) {
                    mutableStateOf(
                        items.getOrNull(index)
                    )
                }

                item?.let {
                    Column(
                        modifier = hiveModifier,
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        itemContent(it)
                    }
                } ?: run {
                    Spacer(
                        modifier = basicModifier
                    )
                }
            }

            if (!goThrough && index == itemsMaxCount - 1) {
                Spacer(
                    modifier = Modifier.weight(.5f)
                )
            }
        }
    }
}

@Preview
@Composable
fun UpBeehiveRowPrev() {
    UpBeehiveRow(
        items = (1..4).toList(),
        itemsMaxCount = 4,
        modifier = Modifier.fillMaxWidth(),
        spaceBetween = 4.dp,
        goThrough = true,
        aspectRatio = .85f
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Honey)) {
            Text(text = "item $it", modifier = Modifier.align(Alignment.Center))
        }
    }
}