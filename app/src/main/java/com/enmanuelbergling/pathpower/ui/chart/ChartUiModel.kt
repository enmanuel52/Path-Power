package com.enmanuelbergling.pathpower.ui.chart

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * @param steps in the Y axis
 * */
@Stable
data class ChartUiModel(
    val steps: Int,
    val values: List<ChartValue> = listOf(),
)

data class ChartValue(
    val value: Float,
    val label: String,
)

data class ChartColors(
    val containerColor: Color,
    val contentBrush: Brush,
    val gridColor: Color,
)

object ChartDefaults {
    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
        contentBrush: Brush = Brush.verticalGradient(
            colors = listOf(
                contentColorFor(containerColor),
                Color.Transparent
            )
        ),
        gridColor: Color = contentColorFor(backgroundColor = containerColor),
    ) = ChartColors(
        containerColor = containerColor,
        contentBrush = contentBrush,
        gridColor = gridColor
    )
}