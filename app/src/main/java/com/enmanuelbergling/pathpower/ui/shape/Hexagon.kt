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
            "m9.242,0.33a2.396,2.25 0,0 0,-2.483 0l-5.591,3.182a2.396,2.25 0,0 0,-1.154 1.924l0,5.128a2.396,2.25 0,0 0,1.154 1.924l5.591,3.182a2.396,2.25 0,0 0,2.483 0l5.591,-3.182a2.396,2.25 0,0 0,1.154 -1.924l0,-5.128a2.396,2.25 0,0 0,-1.154 -1.924l-5.591,-3.182z"
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