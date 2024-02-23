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
            "m504.07,320.38c39.86,102.67 -83.64,191.63 -247.99,191.63s-288.34,-87.7 -247.99,-191.63c24.8,-63.88 148.8,-159.69 247.99,-319.38c99.2,159.69 223.2,255.5 247.99,319.38z"
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