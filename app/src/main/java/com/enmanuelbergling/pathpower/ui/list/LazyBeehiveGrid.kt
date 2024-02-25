package com.enmanuelbergling.pathpower.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Preview
@Composable
fun LazyBeeListAdaptive() {

    val spaceBetween = 12.dp
    val contentPadding = 4.dp
    val itemWidth = 100.dp

}

@Composable
fun <T: Any> beeItems(items: List<T>, content: @Composable (T) ->Unit){
    items.forEach{
        content(it)
    }
}

@Composable
fun  beeItems(count: Int, content: @Composable (index: Int) ->Unit){
    repeat(count){
        content(it)
    }
}

@Composable
private fun BeeItemList(index: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondary)
        ) {
            Text(
                text = "Hi $index",
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.titleMedium,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * Describes the count and sizes for columns
 * */
sealed interface BeeGridCells {
    data class Fixed(val count: Int) : BeeGridCells
    data class Adaptive(val minSize: Dp) : BeeGridCells
}