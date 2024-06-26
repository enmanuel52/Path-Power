package com.enmanuelbergling.pathpower

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.enmanuelbergling.path_power.ui.list.BasicBeehiveExample
import com.enmanuelbergling.pathpower.ui.cars.navigation.CarsNavHost
import com.enmanuelbergling.pathpower.ui.theme.PathPowerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var basicExampleVisible by rememberSaveable {
                mutableStateOf(true)
            }

            PathPowerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {

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
            }
        }
    }
}