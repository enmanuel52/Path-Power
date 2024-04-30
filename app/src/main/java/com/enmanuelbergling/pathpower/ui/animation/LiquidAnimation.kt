package com.enmanuelbergling.pathpower.ui.animation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.pathpower.ui.custom.DefaultAnimationDuration
import com.enmanuelbergling.pathpower.ui.custom.FastInEasing
import com.enmanuelbergling.pathpower.ui.custom.LiquidBottomShape
import com.enmanuelbergling.pathpower.ui.custom.SlowInEasing
import com.enmanuelbergling.pathpower.ui.custom.getOffsetAround
import com.enmanuelbergling.pathpower.ui.custom.rememberLiquidEffect
import com.enmanuelbergling.pathpower.ui.model.LiquidFABUi
import com.enmanuelbergling.pathpower.ui.model.LiquidMetrics
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.RenderEffect as ComposeRenderEffect


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun LiquidFABContainer(fabUis: List<LiquidFABUi>, modifier: Modifier = Modifier) {
    val liquidEffect = rememberLiquidEffect()

    var expanded by remember {
        mutableStateOf(false)
    }

    val animationProgress by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        label = "progress animation",
        animationSpec = tween(
            DefaultAnimationDuration,
        ),
    )

    Box(modifier) {
        LiquidFABGroup(
            animationProgress = animationProgress,
            renderEffect = liquidEffect,
            isExpanded = expanded,
            fabUis = fabUis,
        ) {}
        LiquidFABGroup(
            animationProgress = animationProgress,
            renderEffect = null,
            isExpanded = expanded,
            fabUis = fabUis,
        ) {
            expanded = !expanded
        }
    }
}

private val FabSize = 64.dp

/**
 * @param fabUis should be 3 for now
 * */
@Composable
fun LiquidFABGroup(
    animationProgress: Float,
    fabUis: List<LiquidFABUi>,
    renderEffect: ComposeRenderEffect?,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    liquidMetrics: LiquidMetrics = LiquidMetrics(),
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    onToggle: () -> Unit,
) {
    val minBoxSize by remember {
        derivedStateOf {
            FabSize + liquidMetrics.expandedDistance * 2
        }
    }

    Box(
        modifier = modifier
            .width(minBoxSize)
            .height(minBoxSize)
            .graphicsLayer {
                this.renderEffect = renderEffect
            },
        contentAlignment = Alignment.BottomCenter,
    ) {
        val startAngle = liquidMetrics.startDegrees
        val sweepAngle = liquidMetrics.sweepDegrees

        // because there are fab at start and end angles
        val angleStep = sweepAngle / (fabUis.lastIndex)

        fabUis.forEachIndexed { index, liquidFABUi ->
            val degrees = index * angleStep + startAngle

            val offset = getOffsetAround(
                degrees = degrees,
                radius = liquidMetrics.expandedDistance,
            )

            LiquidFAB(
                onClick = liquidFABUi.onClick,
                modifier = Modifier
                    .graphicsLayer {
                        val maximumValue = (index + 1f) / fabUis.size
                        val itemProgress =
                            animationProgress.coerceAtMost(maximumValue) / maximumValue

                        translationY = offset.y.toPx() * itemProgress
                        translationX = offset.x.toPx() * itemProgress
                        rotationZ = -60f + (60f * itemProgress)
                    },
                containerColor = containerColor,
                imageVector = if (renderEffect == null) liquidFABUi.icon else null
            )
        }

        ToggleFAB(
            isExpanded = isExpanded,
            onToggle = onToggle,
            modifier = Modifier.graphicsLayer {
                rotationZ = animationProgress * (45f + 180f)
            }
        )
    }
}

@Composable
fun ToggleFAB(
    isExpanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier,
) {
    Box {
        AnimatedVisibility(
            visible = !isExpanded,
            label = "toggle button animation",
            enter = expandIn(
                animationSpec = tween(
                    DefaultAnimationDuration,
                    easing = FastInEasing
                ),
                expandFrom = Alignment.Center,
            ),
            exit = shrinkOut(
                animationSpec = tween(
                    DefaultAnimationDuration,
                    easing = SlowInEasing
                ),
                shrinkTowards = Alignment.Center,
            ),
            modifier = Modifier
                .align(Alignment.Center)
                .clip(CircleShape),
        ) {
            LiquidFAB(
                onClick = onToggle,
                imageVector = null,
                modifier = Modifier.clip(CircleShape),
            )
        }
        LiquidFAB(
            onClick = onToggle,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            imageVector = Icons.Rounded.Add,
            modifier = modifier,
        )

    }
}

@Composable
fun LiquidFAB(
    modifier: Modifier = Modifier,
    imageVector: ImageVector? = null,
    shape: Shape = CircleShape,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
        containerColor = containerColor,
        contentColor = contentColor,
    ) {
        if (imageVector != null) {
            Icon(imageVector = imageVector, contentDescription = "favorite")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
fun BetterLiquidBottomBar() {
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()

    fun snackBarAction(action: String) {
        scope.launch {
            snackbarHostState.showSnackbar("$action action", withDismissAction = true)
        }
    }

    val fabUis = listOf(
        LiquidFABUi(Icons.Rounded.Face) {
            snackBarAction("Face")
        },
        LiquidFABUi(Icons.Rounded.ThumbUp) {
            snackBarAction("ThumbUp")
        },
        LiquidFABUi(Icons.Rounded.ShoppingCart) {
            snackBarAction("ShoppingCart")
        },
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.BottomCenter,
        ) {
            BottomBar(
                Modifier
                    .height(64.dp)
                    .padding(horizontal = 4.dp)
                    .padding(bottom = 4.dp)
            )

            LiquidFABContainer(
                fabUis = fabUis,
                modifier = Modifier
                    .padding(bottom = 28.dp)
            )

        }
    }
}

@Composable
private fun BottomBar(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
    cornerSize: Dp = 12.dp,
    fabSize: Dp = 64.dp,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(cornerSize))
            .clip(
                shape = LiquidBottomShape(fabSize, cornerSize, cornerSize)
            )
            .background(containerColor)
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColorFor(containerColor)) {
            Icon(imageVector = Icons.Rounded.Home, contentDescription = "home icon")

            Icon(imageVector = Icons.Rounded.Settings, contentDescription = "settings icon")
        }
    }
}