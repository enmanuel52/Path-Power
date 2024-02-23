package com.enmanuelbergling.pathpower.ui.shape

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.pathpower.ui.theme.Honey

@Preview
@Composable
fun LayDownHexagonPrev() {
    Box(modifier = Modifier
        .size(200.dp)
        .clip(LayDownHexagon)
        .background(Honey))
}