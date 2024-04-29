package com.enmanuelbergling.pathpower.ui.animation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.enmanuelbergling.pathpower.ui.custom.DefaultAnimationDuration
import com.enmanuelbergling.pathpower.ui.custom.rememberLiquidEffect
import com.enmanuelbergling.pathpower.ui.model.LiquidFABUi
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun LiquidHorizontally() {

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()

    fun snackBarAction(action: String) {
        scope.launch {
            snackbarHostState.showSnackbar("$action action")
        }
    }

    val fabUis = listOf(
        LiquidFABUi(Icons.Rounded.Face) {
            snackBarAction("Face")
        },
        LiquidFABUi(Icons.Rounded.ThumbUp) {
            snackBarAction("ThumbUp")
        },
        LiquidFABUi(Icons.Rounded.ShoppingCart) {
            snackBarAction("ShoppingCart")
        },
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            HorizontallyLiquidFABContainer(fabUis = fabUis)
        }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun HorizontallyLiquidFABContainer(fabUis: List<LiquidFABUi>, modifier: Modifier = Modifier) {
    val liquidEffect = rememberLiquidEffect()

    var expanded by remember {
        mutableStateOf(false)
    }

    val animationProgress by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        label = "progress animation",
        animationSpec = tween(
            DefaultAnimationDuration,
        ),
    )

    Box(modifier) {
        HorizontallyLiquidFABGroup(
            animationProgress = animationProgress,
            renderEffect = liquidEffect,
            isExpanded = expanded,
            fabUis = fabUis,
        ) {}
        HorizontallyLiquidFABGroup(
            animationProgress = animationProgress,
            renderEffect = null,
            isExpanded = expanded,
            fabUis = fabUis,
        ) {
            expanded = !expanded
        }
    }
}

@Composable
fun HorizontallyLiquidFABGroup(
    animationProgress: Float,
    fabUis: List<LiquidFABUi>,
    renderEffect: RenderEffect?,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    onToggle: () -> Unit,
) {

    val density = LocalDensity.current
    val fabSize = 66.dp
    val fabSizePx = with(density) { fabSize.toPx() }
    val horizontalSpace = 18.dp
    val horizontalSpacePx = with(density) { horizontalSpace.toPx() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                this.renderEffect = renderEffect
            },
        contentAlignment = Alignment.CenterEnd,
    ) {
        fabUis.forEachIndexed { index, liquidFABUi ->
            LiquidFAB(
                onClick = liquidFABUi.onClick,
                modifier = Modifier
                    .graphicsLayer {

                        val maxWidthOffset = (fabSizePx + horizontalSpacePx) * (index + 1)

                        translationX = -maxWidthOffset * animationProgress
                        rotationZ = 180f + (-180f * animationProgress)
                    },
                containerColor = containerColor,
                imageVector = if (renderEffect == null) liquidFABUi.icon else null
            )
        }

        ToggleFAB(isExpanded, onToggle, modifier = Modifier.graphicsLayer {
            rotationZ = animationProgress * (-45f - 180f)
        })
    }
}