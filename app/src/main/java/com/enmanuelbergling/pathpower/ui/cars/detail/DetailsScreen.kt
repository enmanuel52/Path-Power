package com.enmanuelbergling.pathpower.ui.cars.detail

import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.enmanuelbergling.pathpower.ui.cars.model.CarModel
import kotlinx.serialization.Serializable

@Composable
fun AnimatedContentScope.DetailsScreen(
    carModel: CarModel,
    onBack: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = carModel.name)
    }
}

fun CarModel.toDestination() = CarDetailsScreenDestination(
    imageResource = imageResource,
    name = name,
    velocity = velocity,
    kindness = kindness,
    popularity = popularity,
    funny = funny,
    wheels = wheels,
    color = color
)

@Serializable
data class CarDetailsScreenDestination(
    @DrawableRes val imageResource: Int,
    val name: String,
    @IntRange(0, 10) val velocity: Int,
    @IntRange(0, 10) val kindness: Int,
    @IntRange(0, 10) val popularity: Int,
    @IntRange(0, 10) val funny: Int,
    @IntRange(0, 10) val wheels: Int,
    val color: Int,
){
    fun toCar() = CarModel(imageResource, name, velocity, kindness, popularity, funny, wheels, color)
}

fun NavGraphBuilder.detailsScreen(onBack: () -> Unit) {
    composable<CarDetailsScreenDestination> { backStackEntry ->
        val destination = backStackEntry.toRoute<CarDetailsScreenDestination>()
        DetailsScreen(carModel = destination.toCar(), onBack)
    }
}

