package com.enmanuelbergling.pathpower.util

import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * @param places after coma
 */
internal infix fun Float.roundTo(places: Int) = (this * 10f.pow(places)).roundToInt() / (10f.pow(places))