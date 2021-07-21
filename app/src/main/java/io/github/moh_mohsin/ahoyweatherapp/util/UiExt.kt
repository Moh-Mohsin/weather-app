package io.github.moh_mohsin.ahoyweatherapp.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import io.github.moh_mohsin.ahoyweatherapp.BuildConfig
import io.github.moh_mohsin.ahoyweatherapp.data.Message
import io.github.moh_mohsin.ahoyweatherapp.data.get
import timber.log.Timber


inline fun <reified T : Activity> Context.startActivity(block: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    block(intent)
    startActivity(intent)
}

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

fun View.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    context.toast(message, length)
}


fun Context.toastDebug(message: String, length: Int = Toast.LENGTH_SHORT) {
    if (BuildConfig.DEBUG) {
        Toast.makeText(this, message, length).show()
        Timber.d("toastDebug: $message")
    }
}

fun Fragment.toastDebug(message: String, length: Int = Toast.LENGTH_SHORT) {
    if (BuildConfig.DEBUG)
        context?.toast(message, length)
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

fun View.showOrInvisible(show: Boolean) {
    if (show) show() else invisible()
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}


fun Context.getColorCompact(@ColorRes id: Int) = ContextCompat.getColor(this, id)
fun Fragment.getColorCompact(@ColorRes id: Int) = requireContext().getColorCompact(id)


fun Context.getDrawableCompact(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Fragment.hideKeyboard() {
    val inputMethodManager =
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
}