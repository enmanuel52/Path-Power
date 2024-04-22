package com.enmanuelbergling.path_power.ui.shape

internal fun resize(pathData: String, scaleX: Float, scaleY: Float): String {
    val regex = "[0-9]+[.]?([0-9]+)?".toRegex()

    val matches = regex.findAll(pathData).toList()

    var newPathData = pathData

    matches.reversed().forEachIndexed { index, matchResult ->
        val scale = if (index % 2 == 1) scaleX else scaleY
        val newPoint = matchResult.value.toFloat() * scale

        newPathData = newPathData.replaceRange(matchResult.range, newPoint.toString())
    }

    return newPathData
}