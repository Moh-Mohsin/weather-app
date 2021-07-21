package io.github.moh_mohsin.ahoyweatherapp.data

import io.github.moh_mohsin.ahoyweatherapp.R

sealed class AppException(
    open val msg: Message = Message.Res(R.string.error_occurred)
) : Exception()

class NetworkException : AppException(Message.Res(R.string.error_network))
class UnknownException : AppException(Message.Res(R.string.error_unknown))
class GeneralException(override val msg: Message) : AppException(msg)
class ServerException : AppException(Message.Res(R.string.error_server))
