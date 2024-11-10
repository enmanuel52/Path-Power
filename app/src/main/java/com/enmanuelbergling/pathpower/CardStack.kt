package com.enmanuelbergling.pathpower

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.enmanuelbergling.pathpower.ui.cars.model.CARS
import com.enmanuelbergling.pathpower.ui.cars.model.CarModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CardStack(modifier: Modifier = Modifier) {
    val state = rememberLazyListState()

    var listSize by remember {
        mutableStateOf(Size.Zero)
    }

    var selectedCar by remember {
        mutableStateOf<CarModel?>(null)
    }

    Column(modifier) {
        AnimatedVisibility(
            visible = selectedCar != null,
            modifier = Modifier.weight(.4f)
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                selectedCar?.let { model ->
                    CarCard(
                        carModel = model,
                        modifier = Modifier.size(300.dp, height = 180.dp),
                        animatedVisibilityScope = this@AnimatedVisibility,
                    ) { selectedCar = null }
                }
            }
        }

        AnimatedContent(
            targetState = selectedCar != null,
            modifier = Modifier
                .weight(.6f)
                .fillMaxWidth()
                .onSizeChanged {
                    listSize = it.toSize()
                },
            label = "stack content switch",
        ) { selected ->
            if (selected) {
                selectedCar?.let { CardHeap(it, Modifier.fillMaxSize()) }
            } else {
                LazyColumn(
                    state = state,
                    contentPadding = PaddingValues(bottom = 36.dp, start = 6.dp, end = 6.dp),
                    verticalArrangement = Arrangement.spacedBy((-150).dp),
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    items(CARS, key = { it.key }) { car ->
                        val fraction by remember {
                            derivedStateOf {
                                val itemInfo = state.layoutInfo.visibleItemsInfo.find { it.key == car.key }
                                val result = itemInfo?.let {
                                    it.offset / listSize.height
                                } ?: 0f

                                result.coerceIn(0f, 1f)
                            }
                        }

                        val transition = updateTransition(fraction, "fraction transition")

                        val fartherSection = .4f

                        val animatedRotation by transition.animateFloat(
                            label = "rotation animation",
                            transitionSpec = { tween(50, easing = LinearEasing) },
                        ) { value ->
                            //to slightly increase rotation in the last ones
                            if (value <= fartherSection) {
                                val newFraction = value / fartherSection
                                lerp(0f, 10f, newFraction)
                            } else {
                                val newFraction = (value - fartherSection) / (1f - fartherSection)
                                lerp(10f, 80f, newFraction)
                            }
                        }

                        val animatedScale by transition.animateFloat(label = "") { value ->
                            if (value <= fartherSection) {
                                val newFraction = value / fartherSection
                                lerp(.8f, 1.2f, newFraction)
                            } else 1.2f
                        }

                        val topPadding by transition.animateDp(label = "y translation") { value ->
                            if (value <= fartherSection) 0.dp else {
                                val newFraction = (value - fartherSection) / (1f - fartherSection)
                                androidx.compose.ui.unit.lerp(0.dp, 80.dp, newFraction)
                            }
                        }

                        CarCard(
                            carModel = car,
                            modifier = Modifier
                                .fillMaxWidth(.8f)
                                .padding(top = topPadding)
                                .height(180.dp)
                                .graphicsLayer {
                                    transformOrigin = TransformOrigin(.5f, .35f)

                                    rotationX = -animatedRotation

                                    scaleX = animatedScale
                                    scaleY = animatedScale
                                },
                            animatedVisibilityScope = this@AnimatedContent
                        ) { selectedCar = car }
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CarCard(
    carModel: CarModel,
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClick: () -> Unit,
) {
    ElevatedCard(onClick, shape = RoundedCornerShape(4), modifier = modifier.sharedElement(
        state = rememberSharedContentState(key = carModel.key),
        animatedVisibilityScope = animatedVisibilityScope,
        boundsTransform = { _, _ ->
            spring(
                Spring.DampingRatioLowBouncy,
                Spring.StiffnessLow
            )
        }
    )) {
        Text(
            carModel.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(6.dp))

        AsyncImage(
            carModel.imageResource,
            contentDescription = "car image",
            contentScale = ContentScale.Fit,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun CarCard(
    carModel: CarModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ElevatedCard(onClick, shape = RoundedCornerShape(4), modifier = modifier) {
        Text(
            carModel.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(6.dp))

        AsyncImage(
            carModel.imageResource,
            contentDescription = "car image",
            contentScale = ContentScale.Fit,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun CardHeap(selected: CarModel, modifier: Modifier = Modifier) {

    Column(
        verticalArrangement = Arrangement.spacedBy((-165).dp),
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CARS.filterNot { it == selected }.forEachIndexed { index, it ->
            val fraction = remember { (index + 1) / CARS.size.toFloat() - 1f }
            val scale = remember { lerp(1f, 1.1f, fraction) }
            CarCard(it,
                Modifier
                    .fillMaxWidth(.8f)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }) { }
        }
    }
}