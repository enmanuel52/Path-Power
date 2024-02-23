package com.enmanuelbergling.pathpower.ui.shape

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

data object Heart : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val pathData =
            "m29.785,18.693c2.199,-3.597 2.215,-5.462 2.215,-8.38c0,-4.494 -3.504,-10.157 -8.432,-10.157s-7.568,3.395 -7.568,3.395s-2.641,-3.395 -7.568,-3.395c-4.928,0 -8.432,5.663 -8.432,10.157c0,4.494 1.832,8.926 5.127,12.22c3.295,3.294 5.498,5.265 7.873,6.965c2.375,1.7 2.95,2.265 3,2.257c0,0 1.819,-1.301 3,-2.257c1.181,-0.956 2.539,-2.07 2.539,-2.07s6.047,-5.139 8.245,-8.736z"
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