package com.x0.newsapi.common

import java.text.SimpleDateFormat
import java.util.Locale

private fun getSimpleDateFormat(): SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

private fun getInputStringDate(): SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

fun formattedDate(publishedAt: String): String? {
    val sampleData = getInputStringDate().parse(publishedAt)
    return getSimpleDateFormat().format(sampleData)
}
