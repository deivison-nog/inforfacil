package com.info85.inforfacil.utils

import android.content.Context

data class ResolvedResource(
    val resId: Int,
    val resolvedByName: Boolean
)

object ResourceResolver {

    fun resolveDrawableByName(context: Context, resourceName: String, fallbackResId: Int): ResolvedResource {
        val resId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
        return if (resId != 0) {
            ResolvedResource(resId = resId, resolvedByName = true)
        } else {
            ResolvedResource(resId = fallbackResId, resolvedByName = false)
        }
    }

    fun rawResIdByName(context: Context, resourceName: String): Int? {
        val resId = context.resources.getIdentifier(resourceName, "raw", context.packageName)
        return resId.takeIf { it != 0 }
    }
}
