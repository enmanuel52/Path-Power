package com.enmanuelbergling.path_power.ui.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember


/**
 * split a list of items into smaller lists for each row
 * */
internal fun <T : Any> groupBeehiveItems(
    originalItems: List<T>,
    columns: Int,
    startAtLeft: Boolean = true,
): List<List<T>> {
    val groupedList = mutableListOf<List<T>>()

    val listQueue = originalItems.toMutableList()

    fun takeFirst(n: Int) {
        val taken = listQueue.take(n)
        groupedList.add(
            taken
        )
        taken.forEach(listQueue::remove)
    }

    val isColumnsEven = columns % 2 == 0

    while (listQueue.isNotEmpty()) {
        if (isColumnsEven) {
            takeFirst(columns / 2)
        } else {
            val isEvenRow = groupedList.count() % 2 == 0

            val largerRow = (isEvenRow && startAtLeft) || (!isEvenRow && !startAtLeft)
            val takenCount = if (largerRow) {
                columns.div(2).inc()
            } else {
                columns.div(2)
            }

            takeFirst(takenCount)
        }
    }

    return groupedList.toList()
}

@Composable
internal fun rememberWidthWeight(columns: Int) = remember(columns) {
    mutableFloatStateOf(
        1f / (1f + (columns - 1).times(.75f))
    )
}