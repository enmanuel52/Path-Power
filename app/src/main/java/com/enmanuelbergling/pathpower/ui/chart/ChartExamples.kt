package com.enmanuelbergling.pathpower.ui.chart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
internal fun ChartsExample() {
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

@Preview
@Composable
internal fun ChartLinePlaceholder(modifier: Modifier = Modifier) {
    val color = LocalContentColor.current

    val percents by remember {
        mutableStateOf(
            (1..8).map { Random.nextFloat() }
        )
    }

    Canvas(
        modifier = modifier.sizeIn(
            minHeight = 100.dp,
            minWidth = 125.dp,
            maxHeight = 160.dp,
            maxWidth = 200.dp
        )
    ) {
        val chartPath = getChartLinePath(percents)

        drawPath(chartPath, color, style = Stroke(2.dp.toPx()))

        val chartOnGround = chartPath.apply {
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
        }

        drawPath(
            chartOnGround,
            Brush.verticalGradient(listOf(color, Color.Transparent)),
            style = Fill
        )
    }
}

@Preview
@Composable
internal fun ChartBarPlaceholder(modifier: Modifier = Modifier) {

    val percents by remember {
        mutableStateOf(
            (1..8).map { Random.nextFloat() }
        )
    }

    Canvas(
        modifier = modifier.sizeIn(
            minHeight = 100.dp,
            minWidth = 125.dp,
            maxHeight = 160.dp,
            maxWidth = 200.dp
        )
    ) {
        drawChartBar(percents)
    }
}
