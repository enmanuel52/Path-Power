package com.enmanuelbergling.pathpower

import androidx.compose.animation.core.tween
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.enmanuelbergling.walkthrough.model.WalkScrollStyle
import com.enmanuelbergling.walkthrough.model.WalkStep
import com.enmanuelbergling.walkthrough.ui.WalkThrough
import kotlinx.coroutines.launch

val STEPS = CARS.map {
    WalkStep(
        it.imageResource, description = it.name
    )
}

private const val LOREM_IPSUM =
    "Lorem ipsum odor amet, consectetuer adipiscing elit. Scelerisque dis metus parturient viverra enim. Quisque nostra dui metus eget viverra posuere nulla quisque. Auctor senectus blandit eros facilisi parturient risus volutpat curabitur."


@Composable
fun Onboarding(modifier: Modifier = Modifier) {

    val pageState = rememberPagerState {
        STEPS.count()
    }
    val scope = rememberCoroutineScope()

    WalkThrough(steps = STEPS, pagerState = pageState, modifier, bottomButton = {
        Button(onClick = {
            scope.launch {
                pageState.animateScrollToPage(
                    page = pageState.currentPage + 1,
                    animationSpec = tween(500)
                )
            }
        }, enabled = pageState.canScrollForward) {
            Text(text = "Next")
        }
    }, scrollStyle = WalkScrollStyle.Normal)
}