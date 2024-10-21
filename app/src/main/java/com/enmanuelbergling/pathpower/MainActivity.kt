package com.enmanuelbergling.pathpower

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.path_power.ui.bottom_bar.JumpingBottomBar
import com.enmanuelbergling.path_power.ui.bottom_bar.JumpingItem
import com.enmanuelbergling.path_power.ui.canvas.AnimatedWavesIndicator
import com.enmanuelbergling.path_power.ui.canvas.WaveColor
import com.enmanuelbergling.path_power.ui.canvas.WaveForce
import com.enmanuelbergling.path_power.ui.list.BasicBeehiveExample
import com.enmanuelbergling.path_power.ui.shaders.plasmaShader
import com.enmanuelbergling.path_power.ui.shape.Heart
import com.enmanuelbergling.pathpower.ui.cars.navigation.CarsNavHost
import com.enmanuelbergling.pathpower.ui.theme.PathPowerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            PathPowerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            JumpBottomBarSample()
                        }
                    ) { paddingValues ->
                        var progress by remember { mutableFloatStateOf(0f) }
                        val animatedProgress by animateFloatAsState(progress, label = "progress animation")
                        var height by remember { mutableFloatStateOf(0.6f) }
                        val animatedHeight by animateFloatAsState(height, label = "scale animation")
                        var frequency by remember { mutableFloatStateOf(0.6f) }
                        val animatedFrequency by animateFloatAsState(frequency, label = "frequency animation")
                        var speed by remember { mutableFloatStateOf(0.6f) }
                        val animatedSpeed by animateFloatAsState(speed, label = "speed animation")
                        // Remembering shader wave color avoids glitches when progress and other params are updated
                        val backWaveColor = remember { WaveColor.Shader.Animated(plasmaShader(0.45f)) }
                        val frontWaveColor = remember { WaveColor.Shader.Animated(plasmaShader()) }
                        Column(
                            modifier = Modifier
                                .padding(paddingValues)
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = CenterHorizontally,
                        ) {
                            AnimatedWavesIndicator(
                                progress = animatedProgress,
                                modifier = Modifier
                                    .size(300.dp, 270.dp)
                                    .clip(Heart)
                                    .border(4.dp, MaterialTheme.colorScheme.surfaceVariant, Heart),
                                color = MaterialTheme.colorScheme.primary,
                                waveForce = WaveForce.AGSLBased.Custom(
                                    frequency = animatedFrequency,
                                    speed = animatedSpeed,
                                    height = animatedHeight,
                                    backWaveColor = backWaveColor,
                                    frontWaveColor = frontWaveColor,
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Slider(
                                value = progress,
                                onValueChange = { newValue -> progress = newValue },
                                valueRange = 0f..1f
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Slider(
                                value = height,
                                onValueChange = { newValue -> height = newValue },
                                valueRange = 0.1f..4.0f
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Slider(
                                value = frequency,
                                onValueChange = { newValue -> frequency = newValue },
                                valueRange = 0.1f..4.0f
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Slider(
                                value = speed,
                                onValueChange = { newValue -> speed = newValue },
                                valueRange = 0.1f..4.0f
                            )
                        }

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