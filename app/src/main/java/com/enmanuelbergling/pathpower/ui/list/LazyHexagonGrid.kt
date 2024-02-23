package com.enmanuelbergling.pathpower.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.pathpower.ui.shape.LayDownHexagon
import com.enmanuelbergling.pathpower.ui.theme.Honey
import kotlin.math.roundToInt

@Preview
@Composable
fun LazyHexagonalGrid() {

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

@Preview
@Composable
fun LazyHexagonalList() {
    val density = LocalDensity.current

    var itemHeight by remember {
        mutableIntStateOf(0)
    }

    LazyColumn(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(with(density) { -itemHeight.toDp() })
    ) {
        items(20) { index ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (index % 2 == 0) Arrangement.Start
                else Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(.55f)
                        .aspectRatio(1f)
                        .onSizeChanged {
                            if (itemHeight == 0) {
                                itemHeight = it.height / 2
                            }
                        }
                        .clip(LayDownHexagon)
                        .background(Honey)
                )
            }
        }
    }
}