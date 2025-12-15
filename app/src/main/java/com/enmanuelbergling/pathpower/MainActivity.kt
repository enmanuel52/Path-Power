package com.enmanuelbergling.pathpower

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.enmanuelbergling.path_power.ui.bottom_bar.JumpingBottomBar
import com.enmanuelbergling.path_power.ui.bottom_bar.JumpingItem
import com.enmanuelbergling.path_power.ui.canvas.AnimatedWavesWithAGSLPreview
import com.enmanuelbergling.path_power.ui.list.BasicBeehiveExample
import com.enmanuelbergling.pathpower.ui.cars.navigation.CarsNavHost
import com.enmanuelbergling.pathpower.ui.theme.PathPowerTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {

            PathPowerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
//                            JumpBottomBarSample()
                        },
                        contentWindowInsets = WindowInsets.statusBars,
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ) { paddingValues ->
                        AnimatedWavesWithAGSLPreview(Modifier.padding(paddingValues))
                    }
                }
            }
        }
    }
}

val ITEMS = listOf(
    JumpingItem(Icons.Rounded.Add),
    JumpingItem(Icons.Rounded.Done),
    JumpingItem(Icons.Rounded.Build),
    JumpingItem(Icons.Rounded.Phone),
    JumpingItem(Icons.Rounded.Delete),
)

@Composable
fun JumpBottomBarSample(modifier: Modifier = Modifier) {

    var selected by remember {
        mutableStateOf(ITEMS.first())
    }

    JumpingBottomBar(items = ITEMS, selected = selected, modifier) {
        selected = it
    }
}

@Composable
fun BeehiveCoolSample(modifier: Modifier = Modifier) {
    var basicExampleVisible by rememberSaveable {
        mutableStateOf(true)
    }

    Scaffold(floatingActionButton = {
        ExtendedFloatingActionButton(onClick = {
            basicExampleVisible = !basicExampleVisible
        }, text = {
            if (basicExampleVisible) Text(text = "Cool")
            else Text(text = "Basic")
        }, icon = {
            Icon(
                imageVector = Icons.Rounded.Settings,
                contentDescription = "switch sample icon"
            )
        })
    }) {

        AnimatedContent(
            targetState = basicExampleVisible, transitionSpec = {
                slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Up) togetherWith slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up
                )
            }, label = "sample switcher animation", modifier = Modifier.padding(it)
        ) { basicSample ->
            if (basicSample) {
                BasicBeehiveExample()
            } else {
                CarsNavHost()
            }
        }
    }
}