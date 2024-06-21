package com.enmanuelbergling.pathpower.ui.cars.home

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
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

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Cars") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    Color.Transparent
                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        CarsBeeGrid(
            onDetails, Modifier
                .padding(it)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
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
            modifier = with(sharedTransitionScope) {
                Modifier.sharedElement(
                    state = rememberSharedContentState(key = carModel.imageResource),
                    animatedVisibilityScope = this@CarsBeeGrid,
                    boundsTransform = { _, _ ->
                        tween()
                    }
                )
            }
        ) {
            AsyncImage(
                carModel.imageResource,
                contentDescription = "car image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
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