package com.enmanuelbergling.pathpower

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.enmanuelbergling.path_power.ui.list.SimpleBeehiveExample
import com.enmanuelbergling.path_power.ui.list.SimpleLazyListExample
import com.enmanuelbergling.pathpower.ui.cars.model.McQueen
import com.enmanuelbergling.pathpower.ui.cars.navigation.CarsNavHost
import com.enmanuelbergling.pathpower.ui.theme.PathPowerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PathPowerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CarsNavHost()
//                    SimpleBeehiveExample() performance issues
                }
            }
        }
    }
}