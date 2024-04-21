package com.enmanuelbergling.pathpower

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.pathpower.ui.chart.ChartDefaults
import com.enmanuelbergling.pathpower.ui.chart.ChartGridContainer
import com.enmanuelbergling.pathpower.ui.chart.ChartStyle
import com.enmanuelbergling.pathpower.ui.chart.ChartUiModel
import com.enmanuelbergling.pathpower.ui.chart.ChartValue
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
                    var visible by remember {
                        mutableStateOf(false to false)
                    }

                    val earnings = listOf(15f, 45f, 18f, 20f, 21f, 35f, 25f)

                    Column(
                        verticalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterVertically
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp)
                            .animateContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        OutlinedButton(onClick = {
                            visible = visible.copy(first = !visible.first)
                        }) {
                            Text(text = "LineChart")
                        }

                        AnimatedVisibility(visible = visible.first) {
                            ChartGridContainer(
                                chartData = ChartUiModel(
                                    steps = 4,
                                    values = earnings.mapIndexed { index, value ->
                                        ChartValue(
                                            value = value,
                                            label = index.toString(),
                                        )
                                    }
                                ),
                                style = ChartStyle.Line,
                                modifier = Modifier
                                    .aspectRatio(1.4f)
                                    .fillMaxWidth()
                                    .padding(6.dp)
                            )
                        }


                        OutlinedButton(onClick = {
                            visible = visible.copy(second = !visible.second)
                        }) {
                            Text(text = "BarChart")
                        }

                        AnimatedVisibility(visible = visible.second) {
                            ChartGridContainer(
                                chartData = ChartUiModel(
                                    steps = 4,
                                    values = earnings.mapIndexed { index, value ->
                                        ChartValue(
                                            value = value,
                                            label = index.toString(),
                                        )
                                    }
                                ),
                                style = ChartStyle.Bar(.75f),
                                colors = ChartDefaults.colors(
                                    containerColor = MaterialTheme.colorScheme.background,
                                    contentBrush = Brush.verticalGradient(
                                        listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.tertiary
                                        )
                                    )
                                ),
                                modifier = Modifier
                                    .aspectRatio(1.4f)
                                    .fillMaxWidth()
                                    .padding(6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}