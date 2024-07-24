package com.enmanuelbergling.path_power.ui.bottom_bar

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.path_power.ui.shape.getHoleRectPath
import kotlin.math.abs

val ITEMS = listOf(
    JumpingItem(Icons.Rounded.Add),
    JumpingItem(Icons.Rounded.Done),
    JumpingItem(Icons.Rounded.Build),
    JumpingItem(Icons.Rounded.Phone),
    JumpingItem(Icons.Rounded.Delete),
)

@Composable
fun JumpBottomBarSample(modifier: Modifier = Modifier) {

    var selected by remember {
        mutableStateOf(ITEMS.first())
    }

    JumpingBottomBar(items = ITEMS, selected = selected, modifier) {
        selected = it
    }
}

private const val DurationInMillis = 1000

@OptIn(ExperimentalTransitionApi::class)
@Composable
fun JumpingBottomBar(
    items: List<JumpingItem>,
    selected: JumpingItem,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    onJump: (JumpingItem) -> Unit,
) {

    var previousSelection by remember {
        mutableStateOf(selected)
    }

    val animationProgress = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = selected) {
        animationProgress.snapTo(0f)

        animationProgress.animateTo(1f, tween(DurationInMillis, 100))
    }

    val ballSize = 58.dp
    val density = LocalDensity.current
    val maxScreenWidth = with(density) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
    val holeSizePx = with(density) { (ballSize-16.dp).toPx() }
    val ballSizePx = with(density){ballSize.toPx()}

    val transitionProgress =
        updateTransition(targetState = animationProgress.value, label = "transition progress")

    val previousHoleAnimatedProgress by transitionProgress.animateFloat(
        label = "previous hole animation",
        transitionSpec = {
            spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)
        }
    ) { progress ->
        1f - (progress / .6f).coerceAtMost(1f)
    }

    val holeAnimatedProgress by transitionProgress.animateFloat(
        label = "hole animation",
        transitionSpec = {
            spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)
        }
    ) { progress ->
        (progress - .6f).div(.4F).coerceIn(0f, 1f)
    }

    val ballOffsetAnimation by transitionProgress.animateOffset(
        label = "ball offset animation",
        transitionSpec = {
            spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow)
        }
    ) { progress ->
        val currentIndex = items.indexOf(selected)
        val closeToMiddlePercent = 1 - abs(progress - .5f).div(.5f)

        val previousIndex = items.indexOf(previousSelection)
        val distance = (currentIndex - previousIndex) / items.size.toFloat() * progress
        val xProgress = previousIndex.plus(1F) / items.size + distance

        Offset(
            x = xProgress * maxScreenWidth,
            y = -ballSizePx * closeToMiddlePercent
        )
    }

    Box(modifier = modifier) {
        Box(modifier = Modifier
            .graphicsLayer {
                val xOffset = 1F/items.size * maxScreenWidth / 2 + ballSizePx/2

                translationY = ballOffsetAnimation.y - holeSizePx / 2
                translationX = ballOffsetAnimation.x - xOffset
            }
            .size(ballSize)
            .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(70.dp)
                .drawBehind {
                    drawPath(
                        path = getHoleRectPath(
                            size = size,
                            holeSizePx = holeSizePx.times(1.4f),
                            holesCount = items.size,
                            hole = items.indexOf(selected),
                            holeProgress = holeAnimatedProgress,
                            previousHole = items.indexOf(previousSelection),
                            previousHoleProgress = previousHoleAnimatedProgress
                        ),
                        color = containerColor,
                        style = Fill
                    )
                },
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val selectionTransition = updateTransition(
                    targetState = item == selected, label = "selection transition"
                )

                val elevationOffset by selectionTransition.animateFloat(
                    label =
                    "elevation animation",
                    transitionSpec = { tween(DurationInMillis) }
                ) { selected ->
                    if (selected) -holeSizePx.times(.6f) else 0f
                }

                val onPrimaryColor = MaterialTheme.colorScheme.onSecondaryContainer
                val onSurfaceColor = MaterialTheme.colorScheme.onSurface

                val colorAnimation by selectionTransition.animateColor(
                    label = "color animation",
                    transitionSpec = { tween(1500) }
                ) { selected ->
                    if (selected) onPrimaryColor else onSurfaceColor
                }

                IconButton(
                    onClick = {
                        if (item != selected) {
                            previousSelection = selected

                            onJump(item)
                        }
                    },
                    modifier = Modifier.graphicsLayer {
                        translationY = elevationOffset
                    }
                ) {
                    Icon(
                        imageVector = item.imageVector,
                        contentDescription = item.imageVector.name,
                        tint = colorAnimation
                    )
                }
            }
        }
    }


}

data class JumpingItem(
    val imageVector: ImageVector,
)