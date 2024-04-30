package com.enmanuelbergling.pathpower

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.pathpower.ui.animation.BetterLiquidBottomBar
import com.enmanuelbergling.pathpower.ui.animation.LiquidHorizontally
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
                    var style by remember {
                        mutableStateOf(MoreFabStyle.BottomBar)
                    }

                    Box(modifier = Modifier.fillMaxSize()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            AnimatedContent(
                                targetState = style,
                                transitionSpec = {
                                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up) togetherWith
                                            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down)
                                },
                                label = "content animation"
                            ) { fabStyle ->
                                when (fabStyle) {
                                    MoreFabStyle.BottomBar -> BetterLiquidBottomBar()
                                    MoreFabStyle.Horizontally -> LiquidHorizontally()
                                }
                            }

                        }


                        ExampleSelector(
                            modifier = Modifier.align(Center),
                            style = style,
                            onStyle = { style = it }
                        )
                    }

                }
            }
        }
    }

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ExampleSelector(
    modifier: Modifier,
    style: MoreFabStyle,
    onStyle: (MoreFabStyle) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MoreFabStyle.entries.forEach { fabStyle ->
            InputChip(
                selected = style == fabStyle,
                onClick = { onStyle(fabStyle) },
                label = { Text(text = "$fabStyle") },
                leadingIcon = {
                    AnimatedVisibility(style == fabStyle) {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = "check icon"
                        )
                    }
                }
            )
        }
    }
}

enum class MoreFabStyle { BottomBar, Horizontally }