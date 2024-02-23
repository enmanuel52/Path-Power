package com.enmanuelbergling.pathpower.ui.shape

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

data object Hexagon : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val pathData =
            "M9.166,0.33a2.25,2.25 0,0 0,-2.332 0l-5.25,3.182A2.25,2.25 0,0 0,0.5 5.436v5.128a2.25,2.25 0,0 0,1.084 1.924l5.25,3.182a2.25,2.25 0,0 0,2.332 0l5.25,-3.182a2.25,2.25 0,0 0,1.084 -1.924V5.436a2.25,2.25 0,0 0,-1.084 -1.924L9.166,0.33z"
        val scaleX = size.width / 16f
        val scaleY = size.height / 16f

        return Outline.Generic(
            PathParser().parsePathString(
                resize(
                    pathData = pathData,
                    scaleX = scaleX,
                    scaleY = scaleY
                )
            ).toPath()
        )
    }
}