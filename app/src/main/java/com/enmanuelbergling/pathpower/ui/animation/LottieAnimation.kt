package com.enmanuelbergling.pathpower.ui.animation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.enmanuelbergling.pathpower.R

@Composable
fun EmptyLottieAnimation(
    modifier: Modifier = Modifier,
) {
    val rawResLottie =
        if (isSystemInDarkTheme()) R.raw.empty_box else R.raw.empty_box1

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(rawResLottie)
    )

    LottieAnimation(
        composition = composition,
        modifier = modifier,
    )
}