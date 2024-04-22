package com.enmanuelbergling.path_power.ui.chart

sealed interface ChartStyle {
    data object Line : ChartStyle
    data class Bar(val widthPercent: Float = .5f, val roundPercent: Float = .25f) : ChartStyle
}