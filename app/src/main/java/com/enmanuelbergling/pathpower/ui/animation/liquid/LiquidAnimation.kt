package com.enmanuelbergling.pathpower.ui.animation.liquid

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.annotation.FloatRange
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import androidx.compose.ui.graphics.RenderEffect as ComposeRenderEffect

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun LiquidCircles() {
    val primaryColor = MaterialTheme.colorScheme.primary

    val liquidEffect = rememberLiquidEffect()

    /*Box(
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
            .drawBehind { drawCircle(primaryColor) }
//            .blur(4.dp)
//            .blur(4.dp, 6.dp)
            .graphicsLayer {
                renderEffect = blurEffect.asComposeRenderEffect()
            },
    )*/
    val density = LocalDensity.current
    val heightPx = with(density) { 150.dp.toPx() }

    var isExpanded by remember {
        mutableStateOf(true)
    }

    val animationProgress by animateFloatAsState(
        targetValue = if (isExpanded) 1f else 0f,
        animationSpec = tween(2_000),
        label = "animation progress"
    )

    Box(
        Modifier
            .fillMaxHeight()
            .padding(24.dp)
    ) {
        BoxWithIconGroup(liquidEffect, animationProgress, heightPx, primaryColor)

        BoxWithIconGroup(null, animationProgress, heightPx, primaryColor)

        Button(
            onClick = { isExpanded = !isExpanded },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(text = if (isExpanded) "Shrink" else "Expand")
        }
    }
}

@Composable
private fun BoxWithIconGroup(
    liquidEffect: ComposeRenderEffect?,
    animationProgress: Float,
    heightPx: Float,
    primaryColor: Color,
) {
    Box(modifier = Modifier
        .fillMaxHeight()
        .graphicsLayer {
            renderEffect = liquidEffect
        }) {

        BoxWithIcon(animationProgress, heightPx, primaryColor, 0f)

        BoxWithIcon(animationProgress, heightPx, primaryColor, 1.3f)

        BoxWithIcon(animationProgress, heightPx, primaryColor, 2.6f)
    }
}

@Composable
private fun BoxWithIcon(
    animationProgress: Float,
    heightPx: Float,
    primaryColor: Color,
    translation: Float,
) {
    Box(modifier = Modifier
        .graphicsLayer {
            translationY = animationProgress * heightPx * translation
        }
        .size(150.dp)
        .clip(CircleShape)
        .background(primaryColor)) {
        Icon(
            imageVector = Icons.Rounded.Build,
            contentDescription = "some icon",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.align(
                Alignment.Center
            )
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
private fun rememberLiquidEffect(): ComposeRenderEffect {
    val radius = 60f
    val blurEffect = RenderEffect.createBlurEffect(radius, radius, Shader.TileMode.DECAL)

    val matrixArray = floatArrayOf(
        1f, 0f, 0f, 0f, 0f, //Red
        0f, 1f, 0f, 0f, 0f, //Green
        0f, 0f, 1f, 0f, 0f, //Blue
        0f, 0f, 0f, 50f, -5_000f, //Alpha
    )

    val colorFilter = RenderEffect.createColorFilterEffect(
        ColorMatrixColorFilter(
            ColorMatrix(matrixArray)
        )
    )

    val liquidEffect =
        RenderEffect.createChainEffect(colorFilter, blurEffect).asComposeRenderEffect()

    return remember { liquidEffect }
}

const val DefaultAnimationDuration = 2_500

@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
fun MoreIconsContainer() {
    val liquidEffect = rememberLiquidEffect()

    var expanded by remember {
        mutableStateOf(false)
    }

    val animationProgress by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        label = "progress animation",
        animationSpec = tween(
            DefaultAnimationDuration
        ),
    )


    Box {
        MoreIcons(
            animationProgress = animationProgress,
            renderEffect = liquidEffect,
            isExpanded = expanded
        ) {}
        MoreIcons(
            animationProgress = animationProgress,
            renderEffect = null,
            isExpanded = expanded
        ) {
            expanded = !expanded
        }
    }
}

@Composable
fun MoreIcons(
    animationProgress: Float,
    renderEffect: ComposeRenderEffect?,
    isExpanded: Boolean,
    onToggle: () -> Unit,
) {
    //Just icon should overlap the liquid animation
    val containerColor = MaterialTheme.colorScheme.primaryContainer
    //if (renderEffect != null) MaterialTheme.colorScheme.primaryContainer else Color.Transparent

    Box(modifier = Modifier
        .fillMaxSize()
        .graphicsLayer {
            this.renderEffect = renderEffect
        }
    ) {
        val count = 4
        val maxAngle = 180f
        val angleStep = maxAngle / (count - 1)
        repeat(count) { index ->
            val angle = index * angleStep

            val offset = getOffsetAround(angle = angle, radius = 90.dp)

            LiquidFAB(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .align(Alignment.Center)
                    .graphicsLayer {
                        val maximumValue = (index + 1f) / count
                        val itemProgress =
                            animationProgress.coerceAtMost(maximumValue) / maximumValue
                        translationY = offset.y.toPx() * itemProgress
                        translationX = offset.x.toPx() * itemProgress
                        rotationZ = -maxAngle + (maxAngle * itemProgress)
                    },
                containerColor = containerColor,
                imageVector = if (renderEffect == null) Icons.Rounded.Favorite else null
            )
        }

        val density = LocalDensity.current
        val fabSize = with(density) { 58.dp.toPx() }

        Box(
            modifier = Modifier
                .align(Alignment.Center),
        ) {
            AnimatedVisibility(
                visible = !isExpanded,
                label = "toggle button animation",
                enter = expandIn(
                    animationSpec = keyframes {
                        IntSize(
                            width = (fabSize * .5f).roundToInt(),
                            height = (fabSize * .5f).roundToInt()
                        ) at (DefaultAnimationDuration * .05).roundToInt()

                        IntSize(
                            width = (fabSize * 1f).roundToInt(),
                            height = (fabSize * 1f).roundToInt()
                        ) at DefaultAnimationDuration/2
                    },
                    expandFrom = Alignment.Center,
                ) { size ->
                    IntSize(
                        size.width / 2,
                        size.height / 2
                    )
                },
                exit = shrinkOut(
                    animationSpec = keyframes {
                        IntSize(
                            width = (fabSize * 1f).roundToInt(),
                            height = (fabSize * 1f).roundToInt()
                        ) at (DefaultAnimationDuration * .5).roundToInt()

                        IntSize(
                            width = (fabSize * .5f).roundToInt(),
                            height = (fabSize * .5f).roundToInt()
                        ) at (DefaultAnimationDuration * .99).roundToInt()
                    },
                    shrinkTowards = Alignment.Center,
                ) { size ->
                    IntSize(size.width / 2, size.height / 2)
                },
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
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                imageVector = Icons.Rounded.Add,
                modifier = Modifier.graphicsLayer {
                    rotationZ = animationProgress * (45f + 180f)
                }
            )

        }
    }
}

@Composable
private fun LiquidFAB(
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

/**
 * Fully based on Trigonometry, starting on 9:00 clockwise
 * @param radius stands for hypotenuse
 * @param angle stands for alpha
 * */
@Composable
fun getOffsetAround(
    @FloatRange(
        from = 0.0,
        to = 360.0,
        fromInclusive = true,
        toInclusive = false,
    ) angle: Float,
    radius: Dp,
): DpOffset {
    val newAngle = angle % 90
    val radians = Math.toRadians(newAngle.toDouble()).toFloat()

    val oppositeSide = radius * sin(radians) //sin
    val adjacentSide = radius * cos(radians) //cos

    return when (angle) {
        in 0f..<90f -> DpOffset(
            x = -adjacentSide, y = -oppositeSide
        )

        in 90f..<180f -> DpOffset(
            x = oppositeSide, y = -adjacentSide
        )

        in 180f..<270f -> DpOffset(
            x = adjacentSide, y = oppositeSide
        )

        in 270f..<360f -> DpOffset(
            x = -oppositeSide, y = adjacentSide
        )

        else -> DpOffset.Zero
    }
}