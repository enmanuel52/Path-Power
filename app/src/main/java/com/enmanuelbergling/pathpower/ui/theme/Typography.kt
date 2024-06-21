package com.enmanuelbergling.pathpower.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.enmanuelbergling.pathpower.R

private val LatoFontFamily = FontFamily(Font(R.raw.lato_regular))

val DefaultTypography = Typography()

val BeehiveTypography = Typography(
    displayLarge = DefaultTypography.displayLarge.copy(fontFamily = LatoFontFamily),
    displayMedium = DefaultTypography.displayMedium.copy(fontFamily = LatoFontFamily),
    displaySmall = DefaultTypography.displaySmall.copy(fontFamily = LatoFontFamily),
    headlineLarge = DefaultTypography.headlineLarge.copy(fontFamily = LatoFontFamily),
    headlineMedium = DefaultTypography.headlineMedium.copy(fontFamily = LatoFontFamily),
    headlineSmall = DefaultTypography.headlineSmall.copy(fontFamily = LatoFontFamily),
    titleLarge = DefaultTypography.titleLarge.copy(fontFamily = LatoFontFamily),
    titleMedium = DefaultTypography.titleMedium.copy(fontFamily = LatoFontFamily),
    titleSmall = DefaultTypography.titleSmall.copy(fontFamily = LatoFontFamily),
    bodyLarge = DefaultTypography.bodyLarge.copy(fontFamily = LatoFontFamily),
    bodyMedium = DefaultTypography.bodyMedium.copy(fontFamily = LatoFontFamily),
    bodySmall = DefaultTypography.bodySmall.copy(fontFamily = LatoFontFamily),
    labelLarge = DefaultTypography.labelLarge.copy(fontFamily = LatoFontFamily),
    labelMedium = DefaultTypography.labelMedium.copy(fontFamily = LatoFontFamily),
    labelSmall = DefaultTypography.labelSmall.copy(fontFamily = LatoFontFamily),
)