package com.enmanuelbergling.pathpower.ui.cars.home

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.enmanuelbergling.path_power.ui.list.BeehiveGridCells
import com.enmanuelbergling.path_power.ui.list.LazyBeehiveVerticalGrid
import com.enmanuelbergling.path_power.ui.shape.Hexagon
import com.enmanuelbergling.pathpower.ui.cars.LocalSharedTransitionScope
import com.enmanuelbergling.pathpower.ui.cars.model.CARS
import com.enmanuelbergling.pathpower.ui.cars.model.CarModel
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedContentScope.HomeScreen(
    onDetails: (CarModel) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Cars") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    Color.Transparent
                )
            )
        }
    ) {
        CarsBeeGrid(
            onDetails, Modifier
                .padding(it)
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun AnimatedContentScope.CarsBeeGrid(
    onDetails: (CarModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current!!

    LazyBeehiveVerticalGrid(
        items = CARS,
        gridCells = BeehiveGridCells.Adaptive(150.dp),
        modifier = modifier,
        spaceBetween = 8.dp
    ) { carModel ->
        ElevatedCard(
            onClick = { onDetails(carModel) },
            shape = Hexagon,
            modifier = Modifier then with(sharedTransitionScope) {
                Modifier.sharedElement(
                    state = rememberSharedContentState(key = carModel.imageResource),
                    animatedVisibilityScope = this@CarsBeeGrid,
                    boundsTransform = { _, _ ->
                        spring(
                            Spring.DampingRatioMediumBouncy,
                            Spring.StiffnessLow
                        )
                    }
                )
            }
        ) {
            AsyncImage(
                carModel.imageResource,
                contentDescription = "car image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
//                    .clip(Hexagon)
                    .fillMaxSize()
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun AnimatedContentScope.CarsGrid(
    onDetails: (CarModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current!!

    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy((-70).dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(CARS) { carModel ->
            ElevatedCard(
                onClick = { onDetails(carModel) },
            ) {
                AsyncImage(
                    carModel.imageResource,
                    contentDescription = "car image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                            then with(sharedTransitionScope) {
                        Modifier.sharedElement(
                            state = rememberSharedContentState(key = carModel.imageResource),
                            animatedVisibilityScope = this@CarsGrid,
                            boundsTransform = { _, _ ->
                                spring(
                                    Spring.DampingRatioMediumBouncy,
                                    Spring.StiffnessLow
                                )
                            }
                        )
                    }.fillMaxSize()
                )
            }
        }
    }
}

@Serializable
data object HomeScreenDestination

fun NavGraphBuilder.homeScreen(onDetails: (CarModel) -> Unit) {
    composable<HomeScreenDestination> {
        HomeScreen(onDetails)
    }
}