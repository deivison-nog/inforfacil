package com.info85.inforfacil.utils

import android.content.Context
import android.view.View
import android.widget.Toast

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Float.toPercent(): String = "${(this * 100).toInt()}%"

fun Int.toStarsString(total: Int = 3): String {
    val clamped = this.coerceIn(0, total)
    val filled = "★".repeat(clamped)
    val empty = "☆".repeat(total - clamped)
    return filled + empty
}

fun View.applyFeedbackShake() {
    animate()
        .translationX(12f)
        .setDuration(60)
        .withEndAction {
            animate().translationX(-12f).setDuration(60).withEndAction {
                animate().translationX(0f).setDuration(60).start()
            }.start()
        }.start()
}

fun View.applyFeedbackBounce() {
    scaleX = 0.9f
    scaleY = 0.9f
    animate().scaleX(1f).scaleY(1f).setDuration(180).start()
}
