package com.enmanuelbergling.path_power.ui.shape

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

data object Star : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val pathData =
            "m8.593,4.111c1.516,-2.72 2.274,-4.079 3.407,-4.079c1.133,0 1.891,1.36 3.407,4.079l0.392,0.704c0.431,0.773 0.646,1.159 0.982,1.414c0.336,0.255 0.754,0.35 1.591,0.539l0.762,0.172c2.944,0.666 4.416,0.999 4.766,2.125c0.35,1.126 -0.653,2.3 -2.66,4.646l-0.519,0.607c-0.57,0.667 -0.856,1 -0.984,1.413c-0.128,0.413 -0.085,0.857 0.001,1.747l0.079,0.81c0.303,3.131 0.455,4.697 -0.462,5.393c-0.917,0.696 -2.295,0.061 -5.051,-1.208l-0.713,-0.328c-0.783,-0.361 -1.175,-0.541 -1.59,-0.541c-0.415,0 -0.807,0.18 -1.59,0.541l-0.713,0.328c-2.756,1.269 -4.135,1.904 -5.051,1.208c-0.917,-0.696 -0.765,-2.262 -0.462,-5.393l0.078,-0.81c0.086,-0.89 0.129,-1.335 0.001,-1.747c-0.128,-0.413 -0.413,-0.746 -0.984,-1.413l-0.519,-0.607c-2.007,-2.347 -3.01,-3.52 -2.66,-4.646c0.35,-1.126 1.822,-1.459 4.766,-2.125l0.762,-0.172c0.837,-0.189 1.255,-0.284 1.591,-0.539c0.336,-0.255 0.551,-0.641 0.982,-1.414l0.392,-0.704l-0,0z"
        val scaleX = size.width / 24f
        val scaleY = size.height / 24f

        return Outline.Generic(
            PathParser().parsePathString(
                resize(
                    pathData,
                    scaleX,
                    scaleY
                )
            ).toPath()
        )
    }
}