package com.enmanuelbergling.pathpower

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import com.enmanuelbergling.pathpower.ui.wallpaper.Wallpaper
import kotlin.math.absoluteValue
import androidx.compose.ui.unit.lerp as dpInterpolation

val ItemHeight = 180.dp
val MaxPaddingItem = 80.dp
const val FartherSection = .35f

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CardStack(
    list: List<Wallpaper>,
    modifier: Modifier = Modifier,
) {
    val state = rememberLazyListState()

    var listSize by remember {
        mutableStateOf(Size.Zero)
    }

    var selectedWallpaper by remember {
        mutableStateOf<Wallpaper?>(null)
    }

    BackHandler(selectedWallpaper != null) {
        selectedWallpaper = null
    }

    Column(modifier) {
        Box(
            Modifier
                .weight(.4f)
                .fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            androidx.compose.animation.AnimatedVisibility(
                selectedWallpaper != null,
            ) {

                selectedWallpaper?.let { model ->
                    WallCard(
                        model = model,
                        modifier = Modifier.fillMaxWidth(.7f),
                        animatedVisibilityScope = this,
                    ) { selectedWallpaper = null }
                }
            }
        }

        LazyColumn(
            state = state,
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(-ItemHeight),
            modifier = Modifier
                .weight(.6f)
                .fillMaxWidth()
                .onSizeChanged {
                    listSize = it.toSize()
                },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            items(list, key = { it.key }) { wallpaper ->
                androidx.compose.animation.AnimatedVisibility(
                    selectedWallpaper != wallpaper,
                    modifier = Modifier.animateItem(),
                    enter = fadeIn(),
                ) {
                    val fraction by remember {
                        derivedStateOf {
                            val itemInfo = state.layoutInfo.visibleItemsInfo.find { it.key == wallpaper.key }
                            val result = itemInfo?.let {
                                it.offset / listSize.height
                            } ?: -0.4f

                            result.coerceAtMost(1f)
                        }
                    }

                    val transition = updateTransition(fraction, "fraction transition")

                    val topPadding by transition.animateDp(label = "padding animation") {
                        computeTopPadding(it)
                    }
                    val animatedRotation by transition.animateFloat(label = "rotation animation") {
                        computeRotation(it)
                    }
                    val animatedScale by transition.animateFloat(label = "scale animation") {
                        computeScale(it)
                    }

                    Column(
                        modifier = Modifier
                            .height(MaxPaddingItem + ItemHeight)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        WallCard(
                            model = wallpaper,
                            modifier = Modifier
                                .height(ItemHeight)
                                .graphicsLayer {
                                    translationY = topPadding.toPx()
                                }
                                .graphicsLayer {
                                    transformOrigin = TransformOrigin(.5f, .12f)
                                    rotationX = -animatedRotation

                                    scaleX = animatedScale
                                    scaleY = animatedScale
                                },
                            animatedVisibilityScope = this@AnimatedVisibility,
                        ) {
                            if (selectedWallpaper == null) {
                                selectedWallpaper = wallpaper
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun computeRotation(fraction: Float) =
    //to slightly increase rotation in the last ones
    if (fraction <= FartherSection) {
        val newFraction = fraction.coerceAtLeast(0f) / FartherSection
        lerp(0f, 12f, newFraction)
    } else {
        val newFraction = (fraction - FartherSection) / (1f - FartherSection)
        lerp(12f, 55f, newFraction)
    }


/**
 * Its high increase rating is due the rotation point is up
 * */
private fun computeScale(fraction: Float) =
    if (fraction < 0) {
        val maxValue = .35f
        val newFraction = fraction.absoluteValue / maxValue
        lerp(.85f, .55f, newFraction)
    } else if (fraction <= FartherSection) {
        val newFraction = fraction / FartherSection
        lerp(.85f, 1.4f, newFraction)
    } else {
        val newFraction = (fraction - FartherSection) / (1f - FartherSection)
        lerp(1.4f, 1.55f, newFraction)
    }


private fun computeTopPadding(fraction: Float) = if (fraction < 0f) {
    val maxValue = .35f
    val newFraction = fraction.absoluteValue / maxValue
    dpInterpolation(
        start = MaxPaddingItem,
        stop = MaxPaddingItem.times(3.9f),
        fraction = newFraction
    )
} else if (fraction <= FartherSection) {
    val newFraction = fraction / FartherSection
    dpInterpolation(
        MaxPaddingItem, 0.dp, newFraction
    )
} else {
    val newFraction = (fraction - FartherSection) / (1f - FartherSection)
    dpInterpolation(
        0.dp, MaxPaddingItem / 4, newFraction,
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.WallCard(
    model: Wallpaper,
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClick: () -> Unit,
) {
    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(4),
        modifier = Modifier
            .sharedElement(
                rememberSharedContentState(key = model.key),
                animatedVisibilityScope = animatedVisibilityScope,
                boundsTransform = { _, _ ->
                    spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)
                },
            )
            .then(modifier)
            .aspectRatio(7f / 5f),
    ) {
        AsyncImage(
            model.image,
            contentDescription = "car image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}