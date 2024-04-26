package com.enmanuelbergling.pathpower.ui.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class LiquidMetrics(
    val startDegrees: Float = 30f,
    val sweepDegrees: Float = 120f,
    val expandedDistance: Dp = 80.dp,
)
