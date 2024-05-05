package com.enmanuelbergling.pathpower.ui.cars.home

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
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
        LazyBeehiveVerticalGrid(
            items = CARS,
            gridCells = BeehiveGridCells.Adaptive(150.dp),
            modifier = Modifier
                .padding(it),
            spaceBetween = 8.dp
        ) { carModel ->
            ElevatedCard(
                onClick = { onDetails(carModel) },
                shape = Hexagon
            ) {
                val sharedTransitionScope = LocalSharedTransitionScope.current!!
                AsyncImage(
                    carModel.imageResource,
                    contentDescription = "car image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                            then with(sharedTransitionScope) {
                        Modifier.sharedElement(
                            rememberSharedContentState(key = carModel.imageResource),
                            this@HomeScreen,
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