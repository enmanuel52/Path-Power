package com.enmanuelbergling.pathpower.ui.cars.home

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.enmanuelbergling.path_power.ui.list.BeehiveGridCells
import com.enmanuelbergling.path_power.ui.list.LazyBeehiveVerticalGrid
import com.enmanuelbergling.path_power.ui.shape.Hexagon
import com.enmanuelbergling.pathpower.ui.cars.model.CARS
import com.enmanuelbergling.pathpower.ui.cars.model.CarModel
import com.enmanuelbergling.pathpower.ui.cars.model.McQueen
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedContentScope.HomeScreen(
    onDetails: (CarModel) -> Unit,
) {
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(text = "Cars") }) },
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(McQueen.color),
                        Color.Transparent,
                    )
                )
            )
    ) {
        LazyBeehiveVerticalGrid(
            items = CARS,
            gridCells = BeehiveGridCells.Adaptive(150.dp),
            modifier = Modifier.padding(it),
            spaceBetween = 8.dp
        ) { carModel ->
            ElevatedCard(
                onClick = { onDetails(carModel) },
                modifier = Modifier.fillMaxSize(),
                shape = Hexagon
            ) {
                Image(
                    painter = painterResource(id = carModel.imageResource),
                    contentDescription = "car image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
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