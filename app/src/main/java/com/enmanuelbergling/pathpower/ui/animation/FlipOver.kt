package com.enmanuelbergling.pathpower.ui.animation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.path_power.ui.shape.Hexagon
import com.enmanuelbergling.pathpower.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FlipOver() {
    val scope = rememberCoroutineScope()

    val localDensity = LocalDensity.current

    val maxAnchorWithPx = with(localDensity) { 60.dp.toPx() }

    val anchors = DraggableAnchors {
        FlipState.None at 0f
        FlipState.Done at -maxAnchorWithPx
    }

    val state = remember {
        AnchoredDraggableState(
            initialValue = FlipState.None,
            anchors = anchors,
            positionalThreshold = { distance: Float -> distance * 0.6f },
            velocityThreshold = { with(localDensity) { 80.dp.toPx() } },
            animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
        )
    }

    val rotation = state.requireOffset() / -maxAnchorWithPx * -180f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
            .anchoredDraggable(state, Orientation.Horizontal),
        contentAlignment = Alignment.Center
    ) {
        Card(
            Modifier
                .size(250.dp, 220.dp)
                .graphicsLayer {
                    transformOrigin = TransformOrigin(.5f, .5f)
                    cameraDistance = density * 16

                    rotationY = rotation
                },
            shape = Hexagon
        ) {
            if (rotation > -90f) {
                Column(
                    Modifier
                        .padding(4.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.bee),
                        contentDescription = "bee image",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Maya")
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(rotationY = 180f)
                ) {
                    OutlinedButton(onClick = { /*TODO*/ }) {
                        Text(text = "Annoy")
                    }
                    OutlinedButton(onClick = { /*TODO*/ }) {
                        Text(text = "Make Honey")
                    }
                    OutlinedButton(onClick = { /*TODO*/ }) {
                        Text(text = "Sting")
                    }
                }
            }
        }

        Button(
            onClick = {
                scope.launch {

                    state.animateTo(
                        when (state.currentValue) {
                            FlipState.None -> FlipState.Done
                            FlipState.Done -> FlipState.None
                        }
                    )
                }

            },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(text = "Flip")
        }
    }
}

enum class FlipState {
    None, Done
}