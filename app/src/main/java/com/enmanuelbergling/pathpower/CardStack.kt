package com.enmanuelbergling.pathpower

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import com.enmanuelbergling.pathpower.ui.cars.model.CARS
import com.enmanuelbergling.pathpower.ui.cars.model.CarModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
fun SharedTransitionScope.CardStack(modifier: Modifier = Modifier) {
    val density = LocalDensity.current
    val state = rememberLazyListState()

    var listSize by remember {
        mutableStateOf(Size.Zero)
    }

    var selectedCar by remember {
        mutableStateOf<CarModel?>(null)
    }


    Column {
        AnimatedVisibility(
            selectedCar != null,
            Modifier.weight(.4f)
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

        val verticalPadding = 120.dp
        LazyColumn(
            state = state,
            contentPadding = PaddingValues(horizontal = 6.dp, vertical = verticalPadding),
            verticalArrangement = Arrangement.spacedBy(-(if (selectedCar != null) 160 else 112).dp),
            modifier = Modifier
                .weight(.6f)
                .fillMaxWidth()
                .onSizeChanged {
                    listSize = it.toSize()
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            items(CARS, key = { it.key }) { car ->
                val fraction by remember {
                    derivedStateOf {
                        val itemInfo = state.layoutInfo.visibleItemsInfo.find { it.key == car.key }
                        val afterOffset = with(density) { verticalPadding.toPx() }
                        val result = itemInfo?.let {
                            it.offset / (listSize.height - afterOffset)
                        } ?: 0f

                        result.coerceIn(0f, 1f)
                    }
                }

                val animatedScale by animateFloatAsState(
                    if (selectedCar == null) 1f
                    else lerp(1f, 1.8f, fraction)
                )

                //to slightly increase rotation in the last ones
                val animatedRotation by animateFloatAsState(
                    targetValue = if (selectedCar != null) 0f
                    else if (fraction <= .6) lerp(0f, 28f, fraction)
                    else lerp(0f, 40f, fraction),
                    label = "rotation animation",
                    animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)
                )

                AnimatedVisibility(selectedCar != car) {
                    CarCard(
                        carModel = car,
                        modifier = Modifier
                            .fillMaxWidth(.75f)
                            .height(180.dp)
                            .graphicsLayer {
                                transformOrigin = TransformOrigin(.5f, .75f)

                                rotationX = -animatedRotation

                                scaleX = animatedScale
                                scaleY = animatedScale
                            },
                        animatedVisibilityScope = this@AnimatedVisibility
                    ) { selectedCar = car }

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
    ElevatedCard(onClick, modifier = modifier.sharedElement(
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