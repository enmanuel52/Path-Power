package com.enmanuelbergling.pathpower.ui.shape

import android.annotation.SuppressLint
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.graphics.PathParser

data object Star : Shape {
    @SuppressLint("RestrictedApi")
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val pathData =
            "M9.153,5.408C10.42,3.136 11.053,2 12,2C12.947,2 13.58,3.136 14.847,5.408L15.175,5.996C15.535,6.642 15.714,6.965 15.995,7.178C16.276,7.391 16.625,7.47 17.324,7.628L17.961,7.772C20.42,8.329 21.65,8.607 21.943,9.548C22.235,10.489 21.397,11.469 19.72,13.43L19.286,13.937C18.81,14.494 18.571,14.773 18.464,15.118C18.357,15.462 18.393,15.834 18.465,16.578L18.531,17.254C18.784,19.871 18.911,21.179 18.145,21.76C17.379,22.342 16.227,21.812 13.924,20.751L13.328,20.477C12.674,20.176 12.347,20.025 12,20.025C11.653,20.025 11.326,20.176 10.672,20.477L10.076,20.751C7.773,21.812 6.621,22.342 5.855,21.76C5.089,21.179 5.216,19.871 5.469,17.254L5.535,16.578C5.607,15.834 5.643,15.462 5.536,15.118C5.429,14.773 5.19,14.494 4.714,13.937L4.28,13.43C2.603,11.469 1.765,10.489 2.057,9.548C2.35,8.607 3.58,8.329 6.04,7.772L6.676,7.628C7.375,7.47 7.724,7.391 8.005,7.178C8.286,6.965 8.466,6.642 8.825,5.996L9.153,5.408Z"
        val scaleX = size.width / 24f
        val scaleY = size.height / 24f

        return Outline.Generic(
            PathParser.createPathFromPathData(
                resize(
                    pathData,
                    scaleX,
                    scaleY
                )
            ).asComposePath()
        )
    }
}