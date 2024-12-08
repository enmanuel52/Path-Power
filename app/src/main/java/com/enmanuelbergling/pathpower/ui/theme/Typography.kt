package com.enmanuelbergling.pathpower.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.enmanuelbergling.pathpower.R

val LatoFontFamily = FontFamily(Font(R.raw.lato_regular))
private val YellowTailFontFamily = FontFamily(Font(R.raw.yellowtail_regular))
private val MadiFontFamily = FontFamily(Font(R.raw.ms_madi_regular))
private val fontFamily = MadiFontFamily

val DefaultTypography = Typography()

val BeehiveTypography = Typography(
    displayLarge = DefaultTypography.displayLarge.copy(fontFamily = fontFamily),
    displayMedium = DefaultTypography.displayMedium.copy(fontFamily = fontFamily),
    displaySmall = DefaultTypography.displaySmall.copy(fontFamily = fontFamily),
    headlineLarge = DefaultTypography.headlineLarge.copy(fontFamily = fontFamily),
    headlineMedium = DefaultTypography.headlineMedium.copy(fontFamily = fontFamily),
    headlineSmall = DefaultTypography.headlineSmall.copy(fontFamily = fontFamily),
    titleLarge = DefaultTypography.titleLarge.copy(fontFamily = fontFamily),
    titleMedium = DefaultTypography.titleMedium.copy(fontFamily = fontFamily),
    titleSmall = DefaultTypography.titleSmall.copy(fontFamily = fontFamily),
    bodyLarge = DefaultTypography.bodyLarge.copy(fontFamily = fontFamily),
    bodyMedium = DefaultTypography.bodyMedium.copy(fontFamily = fontFamily),
    bodySmall = DefaultTypography.bodySmall.copy(fontFamily = fontFamily),
    labelLarge = DefaultTypography.labelLarge.copy(fontFamily = fontFamily),
    labelMedium = DefaultTypography.labelMedium.copy(fontFamily = fontFamily),
    labelSmall = DefaultTypography.labelSmall.copy(fontFamily = fontFamily),
)