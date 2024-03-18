package com.enmanuelbergling.pathpower.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.pathpower.R
import com.enmanuelbergling.pathpower.ui.shape.LayDownHexagon
import kotlin.random.Random

@Preview
@Composable
internal fun OverlappingList() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(
            (-100).dp
        )
    ) {
        items(40) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(LayDownHexagon)
                    .background(
                        Color(
                            red = Random.nextInt(255),
                            green = Random.nextInt(255),
                            blue = Random.nextInt(255),
                        )
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bee),
                    contentDescription = "bee image",
                    Modifier.size(50.dp).align(Alignment.TopCenter)
                )
            }
        }
    }
}