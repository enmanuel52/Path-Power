package com.enmanuelbergling.pathpower.ui.shape

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

data object Tooltip : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val pathData =
            "M26.667,0L5.333,0C2.388,0 0,2.371 0,5.297L0,22.187C0,25.111 2.055,27 5,27L11.639,27L16,32.001L20.361,27L27,27C29.945,27 32,25.111 32,22.187L32,5.297C32,2.371 29.612,0 26.667,0"
        val scaleX = size.width / 32f
        val scaleY = size.height / 32f

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