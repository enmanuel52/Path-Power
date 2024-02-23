package com.enmanuelbergling.pathpower.ui.shape

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

data object LayDownHexagon : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val pathData =
            "M477.04,219.21L378.58,48.68c-7.97,-13.8 -22.68,-22.29 -38.61,-22.29H143.04c-15.92,0 -30.63,8.49 -38.61,22.29L5.97,219.21c-7.96,13.8 -7.96,30.78 0,44.59l98.46,170.54c7.98,13.8 22.68,22.29 38.61,22.29h196.93c15.93,0 30.63,-8.49 38.61,-22.29l98.47,-170.54C485,249.99 485,233.01 477.04,219.21z"
        val scaleX = size.width / 483.01f
        val scaleY = size.height / 483.01f

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