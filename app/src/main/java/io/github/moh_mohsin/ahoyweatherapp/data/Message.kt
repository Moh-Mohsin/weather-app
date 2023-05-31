package io.github.moh_mohsin.ahoyweatherapp.data

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment

sealed class Message {
    data class Res(@StringRes val stringId: Int) : Message()
    data class Raw(val msg: String) : Message()
}


fun Message.get(context: Context): String =
    when (this) {
        is Message.Res -> context.getString(stringId)
        is Message.Raw -> msg
    }
@Composable
fun Message.get(): String =
    when (this) {
        is Message.Res -> stringResource(stringId)
        is Message.Raw -> msg
    }

fun Message.get(fragment: Fragment): String =
    get(fragment.requireContext())

fun Message.getRawOrDefault() = if (this is Message.Raw) this.msg else "An unknown error occurred"
