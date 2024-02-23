package com.enmanuelbergling.pathpower.ui.shape

import android.graphics.Path
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.PathFillType
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
            "m477.04,216.06l-98.47,-190.74c-7.97,-15.44 -22.68,-24.93 -38.61,-24.93l-196.93,0c-15.92,0 -30.63,9.5 -38.61,24.93l-98.46,190.74c-7.96,15.44 -7.96,34.43 0,49.87l98.46,190.76c7.98,15.44 22.68,24.94 38.61,24.94l196.93,0c15.93,0 30.63,-9.5 38.61,-24.94l98.47,-190.76c7.96,-15.44 7.96,-34.44 0,-49.87z"
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