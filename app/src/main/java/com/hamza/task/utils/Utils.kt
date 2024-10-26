package com.hamza.task.utils

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan

object Utils {

    fun formatSpannableText(context: Context, dynamicValue: String, staticValue: String, dynamicColor: Int, staticColor: Int): SpannableString {
        val fullText = "$dynamicValue / $staticValue"
        return SpannableString(fullText).apply {
            setSpan(AbsoluteSizeSpan(16, true), 0, dynamicValue.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(ForegroundColorSpan(context.resources.getColor(dynamicColor)), 0, dynamicValue.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(AbsoluteSizeSpan(13, true), dynamicValue.length + 3, fullText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(ForegroundColorSpan(context.resources.getColor(staticColor)), dynamicValue.length + 3, fullText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
}