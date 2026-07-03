package com.info85.inforfacil.utils

import android.content.Context
import android.widget.Toast

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Float.toPercent(): String = "${(this * 100).toInt()}%"

fun Int.toStarsString(total: Int = 3): String {
    val filled = "★".repeat(this)
    val empty = "☆".repeat(total - this)
    return filled + empty
}
