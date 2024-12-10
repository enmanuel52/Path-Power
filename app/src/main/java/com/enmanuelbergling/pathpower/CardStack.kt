package com.enmanuelbergling.pathpower

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.enmanuelbergling.pathpower.ui.theme.NeutralYellow
import com.enmanuelbergling.pathpower.ui.wallpaper.Wallpaper
import kotlinx.coroutines.delay
import kotlin.math.roundToLong
import androidx.compose.ui.unit.lerp as dpInterpolation

val ItemHeight = 180.dp
val MaxPaddingItem = 80.dp
val FrameWidth = 240.dp

//const val FarSection = 0f
const val MediumSection = .25f
const val CloseSection = .7f
const val LowerFraction = -.3f

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CardStack(
    list: List<Wallpaper>,
    modifier: Modifier = Modifier,
) {
    val state = rememberLazyListState(initialFirstVisibleItemIndex = list.size / 3)

    var listSize by remember {
        mutableStateOf(IntSize.Zero)
    }

    var selectedWallpaper by remember {
        mutableStateOf<Wallpaper?>(null)
    }

    var hammerVisible by rememberSaveable { mutableStateOf(true) }

    val lottieCompositionHammer by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.hammer)
    )

    LaunchedEffect(Unit) {
        val timeMillis = lottieCompositionHammer?.duration?.times(.75)?.roundToLong()
        if (timeMillis != null) {
            delay(timeMillis)
        }
        hammerVisible = false
    }

    BackHandler(selectedWallpaper != null) {
        selectedWallpaper = null
    }

    val frameAngle = remember { Animatable(25f) }
    LaunchedEffect(hammerVisible) {
        if (!hammerVisible) {
            delay(300)

            frameAngle.animateTo(0f, spring(Spring.DampingRatioHighBouncy, Spring.StiffnessLow))
        }
    }

    Column(modifier) {
        Box(
            Modifier
                .weight(.3f)
                .fillMaxWidth()
                .padding(top = 12.dp),
            contentAlignment = Alignment.BottomCenter,
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = !hammerVisible,
                enter = slideInVertically(spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)) { it },
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            transformOrigin = TransformOrigin(.5f, 0f)
                            rotationZ = frameAngle.value
                        }
                        .drawStringFrameBehind(),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(12.dp)
                            .shadow(8.dp),
                    ) {
                        CurrentPicture(selectedWallpaper)

                        WoodenFrame(
                            Modifier
                                .width(FrameWidth)
                                .aspectRatio(7f / 5f)
                        ) { selectedWallpaper = null }
                    }
                }
            }

            AnimatedContent(
                hammerVisible,
                modifier = Modifier
                    .align(Alignment.TopCenter),
                label = "hammer onto nail",
            ) { hammer ->
                if (hammer) {
                    LottieAnimation(
                        composition = lottieCompositionHammer,
                        modifier = Modifier
                            .size(100.dp)
                            .graphicsLayer {
                                translationY = -24.dp.toPx()
                                translationX = -45.dp.toPx()
                            },
                    )
                } else {
                    NailPicture()
                }
            }
        }

        Box(Modifier.weight(.7f)) {
            LazyColumn(
                state = state,
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 24.dp, top = 154.dp),
                verticalArrangement = Arrangement.spacedBy(-ItemHeight),
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged {
                        listSize = it
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
                                    val offset = it.offset + state.layoutInfo.beforeContentPadding
                                    offset.toFloat() / listSize.height
                                } ?: (LowerFraction - 0.5f)

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
                                        alpha = if (fraction < LowerFraction) 0f else 1f
                                        translationY = topPadding.toPx()
                                    }
                                    .graphicsLayer {
                                        transformOrigin = TransformOrigin(.5f, .12f)
                                        rotationX = -animatedRotation

                                        scaleX = animatedScale
                                        scaleY = animatedScale
                                    },
                                animatedVisibilityScope = this@AnimatedVisibility,
                                renderInOverlayDuringTransition = false,
                            ) {
                                selectedWallpaper = wallpaper
                            }
                        }
                    }
                }
            }


            Title()
        }
    }
}

@Composable
private fun Title() {
    Text(
        text = "Memories",
        style = MaterialTheme.typography.displayLarge,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = Color.Black,
    )
}

@Composable
private fun NailPicture() {
    Image(
        painterResource(R.drawable.nail), contentDescription = "nail",
        modifier = Modifier
            .graphicsLayer {
                translationY = -4.dp.toPx()
            }
            .size(10.dp, 24.dp)
            .drawWithContent {
                clipRect(bottom = size.height / 3) {
                    this@drawWithContent.drawContent()
                }
            },
        contentScale = ContentScale.Fit,
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.CurrentPicture(
    selectedWallpaper: Wallpaper?,
) {
    AnimatedContent(
        selectedWallpaper,
        label = "frame content animation"
    ) { wallpaper ->
        if (wallpaper != null) {
            selectedWallpaper?.let { model ->
                WallCard(
                    model = model,
                    modifier = Modifier
                        .width(FrameWidth)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    animatedVisibilityScope = this,
                    renderInOverlayDuringTransition = true,
                ) { }
            }
        } else {
            Image(
                painter = painterResource(R.drawable.light_blue_paper),
                contentDescription = "wooden frame paper",
                modifier = Modifier
                    .width(FrameWidth)
                    .aspectRatio(7f / 5)
                    .padding(12.dp),
                contentScale = ContentScale.FillBounds
            )
        }
    }
}

private fun Modifier.drawStringFrameBehind(): Modifier = drawBehind {
    val frameStringSupportPath = getFrameStringSupportPath()

    //string
    drawPath(
        path = frameStringSupportPath,
        color = NeutralYellow,
        style = Stroke(width = 2.dp.toPx())
    )
}

private fun DrawScope.getFrameStringSupportPath() = Path().apply {
    val frameHeight = FrameWidth.toPx() * 5 / 7
    val frameTop = size.height - frameHeight
    moveTo(size.width.times(.2f), frameTop)
    val curbPx = 4.dp.toPx()
    lineTo(size.width / 2 - curbPx, curbPx)

    relativeQuadraticTo(curbPx, -curbPx, curbPx * 2, 0f)

    lineTo(size.width.times(.8f), frameTop)
}


@Composable
private fun WoodenFrame(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Image(
        painter = painterResource(R.drawable.picture_frame),
        contentDescription = "wooden frame",
        modifier = modifier.clickable(null, null, onClick = onClick),
        contentScale = ContentScale.FillBounds
    )
}

private fun computeRotation(fraction: Float) =
    if (fraction <= MediumSection) {
        val newFraction = fraction.coerceAtLeast(0f) / MediumSection
        lerp(0f, 15f, newFraction)
    } else if (fraction <= CloseSection) {
        val newFraction = (fraction - MediumSection) / (CloseSection - MediumSection)
        lerp(15f, 35f, newFraction)
    } else {
        //increasing rotation in the closer ones
        val newFraction = (fraction - CloseSection) / (1f - CloseSection)
        lerp(35f, 55f, newFraction)
    }


/**
 * Its high increase rating is due the rotation point is up
 * */
private fun computeScale(fraction: Float) =
    if (fraction < 0f) {
        val newFraction = 1f - (fraction.coerceAtLeast(LowerFraction) / LowerFraction)
        lerp(.58f, .64f, newFraction)
    } else if (fraction <= MediumSection) {
        val newFraction = fraction / MediumSection
        lerp(.64f, .85f, newFraction)
    } else if (fraction <= CloseSection) {
        val newFraction = (fraction - MediumSection) / (CloseSection - MediumSection)
        lerp(.85f, 1.4f, newFraction)
    } else {
        val newFraction = (fraction - CloseSection) / (1f - CloseSection)
        lerp(1.4f, 1.55f, newFraction)
    }


private fun computeTopPadding(fraction: Float) = if (fraction <= MediumSection) {
    val newFraction = fraction / MediumSection
    dpInterpolation(
        start = MaxPaddingItem.times(2.65f),
        stop = MaxPaddingItem,
        fraction = newFraction
    )
} else if (fraction <= CloseSection) {
    val newFraction = (fraction - MediumSection) / (CloseSection - MediumSection)
    dpInterpolation(
        MaxPaddingItem, 0.dp, newFraction
    )
} else {
    val newFraction = (fraction - CloseSection) / (1f - CloseSection)
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
    renderInOverlayDuringTransition: Boolean = true,
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
                renderInOverlayDuringTransition = renderInOverlayDuringTransition,
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