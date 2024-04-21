package com.enmanuelbergling.pathpower

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.pathpower.ui.chart.ChartContainer
import com.enmanuelbergling.pathpower.ui.chart.ChartDefaults
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
                    val earnings = listOf(15f, 45f, 18f, 20f, 15f, 35f, 25f)

                    Column {
                        ChartContainer(
                            chartData = ChartUiModel(
                                steps = 4,
                                values = earnings.mapIndexed { index, value ->
                                    ChartValue(
                                        value = value,
                                        label = index.toString(),
                                    )
                                }
                            ),
                            style = ChartStyle.Bar,
                            colors = ChartDefaults.colors(),
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