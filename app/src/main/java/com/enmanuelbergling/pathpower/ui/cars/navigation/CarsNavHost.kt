package com.enmanuelbergling.pathpower.ui.cars.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.enmanuelbergling.pathpower.ui.cars.detail.detailsScreen
import com.enmanuelbergling.pathpower.ui.cars.detail.toDestination
import com.enmanuelbergling.pathpower.ui.cars.home.HomeScreenDestination
import com.enmanuelbergling.pathpower.ui.cars.home.homeScreen

@Composable
fun CarsNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = HomeScreenDestination) {
        homeScreen { carModel -> navController.navigate(carModel.toDestination()) }

        detailsScreen { navController.popBackStack() }
    }
}