package io.github.moh_mohsin.ahoyweatherapp.util

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import io.github.moh_mohsin.ahoyweatherapp.data.Message
import io.github.moh_mohsin.ahoyweatherapp.data.get


fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    val adaptiveLength = if (length == Toast.LENGTH_LONG) {
        Toast.LENGTH_LONG
    } else {
        val wordCount = message.split(' ').size
        val charCount = message.length
        if (wordCount > 4 || charCount > 35) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    }
    Toast.makeText(this, message, adaptiveLength).show()
}

fun Context.toast(res: Int, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, res, length).show()
}

fun Fragment.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    context?.toast(message, length)
}

fun Fragment.toast(message: Message, length: Int = Toast.LENGTH_SHORT) {
    context?.toast(message.get(context!!), length)
}


fun Fragment.toast(res: Int, length: Int = Toast.LENGTH_SHORT) {
    requireContext().toast(res, length)
}



fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}


fun View.show() {
    visibility = View.VISIBLE
}

fun View.showOrHide(show: Boolean) {
    if (show) show() else hide()
}
