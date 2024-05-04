package com.enmanuelbergling.pathpower.ui.cars.model

import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.compose.ui.graphics.Color

sealed interface HexagonField {

    data class LabeledField(
        val label: String,
        @IntRange val value: Int,
    ) : HexagonField

    data class Car(@DrawableRes val image: Int, val name: String) : HexagonField

    data class ColorField(val color: Color):HexagonField
}
