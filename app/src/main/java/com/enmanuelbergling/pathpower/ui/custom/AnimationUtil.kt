package com.enmanuelbergling.pathpower.ui.custom

import androidx.compose.animation.core.CubicBezierEasing

const val DefaultAnimationDuration = 1_800

val FastInEasing = CubicBezierEasing(.22f, 1f, .1f, .68f)
val SlowInEasing = CubicBezierEasing(.89f, .29f, .53f, .09f)