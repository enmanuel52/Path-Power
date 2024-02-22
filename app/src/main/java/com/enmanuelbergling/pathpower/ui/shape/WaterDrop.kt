package com.enmanuelbergling.pathpower.ui.shape

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

data object WaterDrop : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val pathData =
            "M406.04,316c24.11,96.44 -50.59,180 -150,180s-174.4,-82.38 -150,-180c15,-60 90,-150 150,-300 60,150 135,240 150,300z"
        val scaleX = size.width / 512f
        val scaleY = size.height / 512f

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