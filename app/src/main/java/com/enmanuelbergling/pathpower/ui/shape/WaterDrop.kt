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
            "M520.25,353.6c0,125 -100.5,219.2 -233.8,219.2S52.55,478.6 52.55,353.6c0,-115.3 164.7,-301.5 197.7,-337.7c9.3,-10.2 22.4,-15.9 36.2,-15.9s26.9,5.8 36.2,15.9C355.55,52.1 520.25,238.4 520.25,353.6z"
        val scaleX = size.width / 572.8f
        val scaleY = size.height / 572.8f

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