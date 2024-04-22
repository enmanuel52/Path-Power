package com.enmanuelbergling.pathpower.ui.shape

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.pathpower.ui.theme.Honey
import com.enmanuelbergling.pathpower.util.roundTo

@Preview
@Composable
internal fun LayDownHexagonPrev() {
    Box(
        modifier = Modifier
            .size(200.dp)
            .clip(Hexagon)
            .background(Honey)
    )
}

@Preview
@Composable
fun AnimatedRatingsPrev() {
    var rating by remember {
        mutableFloatStateOf(0f)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(5) { index ->
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(Star)
                        .border(1.dp, Honey, Star)
                ) {
                    val percentFilled by animateFloatAsState(
                        targetValue = if (index.plus(1) <= rating) 1f
                        else if (index.plus(1) - rating < 1f) 1f - (index.plus(1) - rating)
                        else 0f,
                        label = "star fill animation",
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )

                    Box(modifier = Modifier
                        .fillMaxSize()
                        .drawBehind {
                            drawRect(
                                color = Honey,
                                size = size.copy(width = size.width.times(percentFilled))
                            )
                        }
                    )
                }
            }
        }

        Row {
            SmallFloatingActionButton(
                onClick = {
                    if (rating >= .5f) {
                        rating = (rating - .5f) roundTo 1
                    }
                }, shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = "decrease rating"
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            SmallFloatingActionButton(
                onClick = {
                    if (rating <= 4.5f) {
                        rating = (rating + .5f) roundTo 1
                    }
                }, shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Rounded.Star,
                    contentDescription = "increment rating"
                )
            }
        }
    }
}