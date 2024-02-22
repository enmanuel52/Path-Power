package com.enmanuelbergling.pathpower.ui.shape

import android.annotation.SuppressLint
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.graphics.PathParser

data object Heart : Shape {
    @SuppressLint("RestrictedApi")
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val pathData =
            "M12,6C10.201,3.903 7.194,3.255 4.939,5.175C2.685,7.096 2.367,10.306 4.138,12.577C5.61,14.465 10.065,18.448 11.525,19.737C11.688,19.881 11.77,19.953 11.865,19.982C11.948,20.006 12.039,20.006 12.123,19.982C12.218,19.953 12.299,19.881 12.463,19.737C13.923,18.448 18.378,14.465 19.85,12.577C21.62,10.306 21.342,7.075 19.048,5.175C16.755,3.275 13.799,3.903 12,6Z"
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