package com.enmanuelbergling.pathpower.ui.shape

import java.util.regex.Pattern

internal fun resize(pathData: String, scaleX: Float, scaleY: Float): String {
    val matcher = Pattern.compile("[0-9]+[.]?([0-9]+)?")
        .matcher(pathData) // match the numbers in the path
    val stringBuffer = StringBuffer()
    var count = 0
    while (matcher.find()) {
        val number = matcher.group().toFloat()
        matcher.appendReplacement(
            stringBuffer,
            (if (count % 2 == 0) number * scaleX else number * scaleY).toString()
        ) // replace numbers with scaled numbers
        ++count
    }
    return stringBuffer.toString() // return the scaled path
}

internal fun resizeUpdate(pathData: String, scaleX: Float, scaleY: Float): String {
    val regex = "[0-9]+[.]?([0-9]+)?".toRegex()
    val newPathData = StringBuilder(pathData)

    val matches = regex.findAll(newPathData)

    matches.forEachIndexed { index, matchResult ->
        val scale = if (index % 2 == 0) scaleX else scaleY
        val newPoint = matchResult.value.toFloat() * scale

        newPathData.replaceRange(
            matchResult.range,
            newPoint.toString()
        )
    }

    return newPathData.toString()
}