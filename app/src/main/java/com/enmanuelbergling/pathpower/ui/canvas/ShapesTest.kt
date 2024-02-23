package com.enmanuelbergling.pathpower.ui.canvas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.pathpower.ui.shape.Star
import com.enmanuelbergling.pathpower.ui.shape.Tooltip

@Preview
@Composable
fun HexagonPreview() {
    Box(
        modifier = Modifier
            .size(200.dp)
            .clip(Tooltip)
            .background(MaterialTheme.colorScheme.primary)
//            .padding(2.dp)
    ) {

    }
}