package com.enmanuelbergling.path_power.ui.bottom_bar

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.ExperimentalTransitionApi
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.path_power.ui.shape.HoleRectShape
import kotlinx.coroutines.launch
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

@OptIn(ExperimentalTransitionApi::class)
@Composable
fun JumpingBottomBar(
    items: List<JumpingItem>,
    selected: JumpingItem,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    ballContainerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    onJump: (JumpingItem) -> Unit,
) {

    var previousSelection by remember {
        mutableStateOf(selected)
    }

    val animationProgress = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = selected) {
        animationProgress.animateTo(1f, tween(DurationInMillis))
    }

    val density = LocalDensity.current
    val maxScreenWidthPx = with(density) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
    val ballSizePx = with(density) { BallSize.toPx() }

    val ballOffsetAnimation by remember(animationProgress.value) {
        derivedStateOf {
            val currentIndex = items.indexOf(selected)
            val closeToMiddlePercent = 1 - abs(animationProgress.value - .5f).div(.5f)

            val previousIndex = items.indexOf(previousSelection)
            val distance =
                (currentIndex - previousIndex) / items.size.toFloat() * animationProgress.value
            val xProgress = previousIndex.plus(1F) / items.size + distance

            Offset(
                x = xProgress * maxScreenWidthPx,
                y = -ballSizePx * closeToMiddlePercent
            )
        }
    }


    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(BottomBarHeight)
    ) {
        Surface(modifier = Modifier
            .size(BallSize)
            .graphicsLayer {
                val xOffset = 1F / items.size * maxScreenWidthPx / 2 + ballSizePx / 2

                translationY = ballOffsetAnimation.y - ballSizePx / 2
                translationX = ballOffsetAnimation.x - xOffset
            },
            shape = CircleShape,
            color = ballContainerColor,
            tonalElevation = BottomTonalElevation,
        ){}

        val previousHoleAnimatedProgress by remember(animationProgress.value) {
            derivedStateOf {
                1f - (animationProgress.value / .6f).coerceAtMost(1f)
            }
        }

        val holeAnimatedProgress by remember(animationProgress.value) {
            derivedStateOf {
                (animationProgress.value - .6f)
                    .div(.4F)
                    .coerceIn(0f, 1f)
            }
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = HoleRectShape(
                holeSizePx = ballSizePx,
                holesCount = items.size,
                holeIndex = items.indexOf(selected),
                holeProgress = holeAnimatedProgress,
                previousHoleIndex = items.indexOf(previousSelection),
                previousHoleProgress = previousHoleAnimatedProgress
            ),
            color = containerColor,
            tonalElevation = BottomTonalElevation
        ) {}

        val scope = rememberCoroutineScope()
        //if I put this inside shaped surface items outside the shape wont be shown
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val selectionTransition = updateTransition(
                    targetState = item == selected, label = "selection transition"
                )

                val bottomBarHeightPx = with(density) { BottomBarHeight.toPx() }
                val elevationOffset by selectionTransition.animateFloat(
                    label = "elevation animation",
                    transitionSpec = { tween(DurationInMillis) }
                ) { selected ->
                    if (selected) -bottomBarHeightPx / 2 else 0f
                }

                val onBallColor = contentColorFor(backgroundColor = ballContainerColor)
                val onContainerColor = contentColorFor(backgroundColor = containerColor)

                val colorAnimation by selectionTransition.animateColor(
                    label = "color animation",
                    transitionSpec = { tween(DurationInMillis) }
                ) { selected ->
                    if (selected) onBallColor else onContainerColor
                }

                IconButton(
                    onClick = {
                        if (item != selected) {
                            previousSelection = selected

                            //rebooting here, otherwise it will blink
                            scope.launch { animationProgress.snapTo(0f) }

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

private const val DurationInMillis = 500
private val BallSize = 58.dp
private val BottomBarHeight = 80.dp
private val BottomTonalElevation = 3.dp

data class JumpingItem(
    val imageVector: ImageVector,
    val label: String = "",
)