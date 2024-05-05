package com.enmanuelbergling.pathpower.ui.cars.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.enmanuelbergling.pathpower.ui.cars.LocalSharedTransitionScope
import com.enmanuelbergling.pathpower.ui.cars.detail.detailsScreen
import com.enmanuelbergling.pathpower.ui.cars.detail.toDestination
import com.enmanuelbergling.pathpower.ui.cars.home.HomeScreenDestination
import com.enmanuelbergling.pathpower.ui.cars.home.homeScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CarsNavHost() {
    val navController = rememberNavController()

    SharedTransitionLayout {
        CompositionLocalProvider(value = LocalSharedTransitionScope provides this) {

            NavHost(navController = navController, startDestination = HomeScreenDestination) {
                homeScreen { carModel -> navController.navigate(carModel.toDestination()) }

                detailsScreen { navController.popBackStack() }
            }
        }
    }
}