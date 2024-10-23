package com.hamza.task.utils

import java.util.Locale

object Ext {



    fun Double.toReadableFormat(): String {
        return when {
            this >= 1_000_000_000 -> formatNumber(this / 1_000_000_000, "B") // Billion
            this >= 1_000_000 -> formatNumber(this / 1_000_000, "M")         // Million
            this >= 1_000 -> formatNumber(this / 1_000, "K")                 // Thousand
            else -> String.format(Locale.US, "%.0f", this)                   // Less than 1000
        }
    }

    fun formatNumber(value: Double, suffix: String): String {
        return if (value % 1.0 == 0.0) {
            String.format(Locale.US, "%.0f%s", value, suffix)
        } else {
            String.format(Locale.US, "%.1f%s", value, suffix)
        }
    }






}