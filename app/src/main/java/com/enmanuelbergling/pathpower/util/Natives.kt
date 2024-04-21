package com.enmanuelbergling.pathpower.util

import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * @param places after coma
 */
internal infix fun Float.roundTo(places: Int) = (this * 10f.pow(places)).roundToInt() / (10f.pow(places))

val ONE_MILLION = 10f.pow(6)
val ONE_THOUSAND = 10f.pow(3)

fun Float.styled() = when {
    this.absoluteValue < ONE_THOUSAND -> "${this roundTo 2}"
    this.absoluteValue < ONE_MILLION -> "${(this / ONE_THOUSAND) roundTo 1}K"
    else -> "${(this / ONE_MILLION) roundTo 1}M"
}