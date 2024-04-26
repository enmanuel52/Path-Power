package com.enmanuelbergling.pathpower.ui.model

import androidx.compose.ui.graphics.vector.ImageVector

data class LiquidFABUi(
    val icon: ImageVector,
    val onClick: () -> Unit = {},
)
