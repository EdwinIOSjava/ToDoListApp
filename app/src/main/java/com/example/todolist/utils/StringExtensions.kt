package com.example.todolist.utils

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan

fun String.addStrikethrough() : SpannableString {
    val spannable = SpannableString(this)
    spannable.setSpan(StrikethroughSpan(), 0, this.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    spannable.setSpan(ForegroundColorSpan(Color.LTGRAY  ),0,spannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return spannable
}
fun String.changeColorText() :SpannableString{
    val spannable = SpannableString(this)

    return spannable
}
