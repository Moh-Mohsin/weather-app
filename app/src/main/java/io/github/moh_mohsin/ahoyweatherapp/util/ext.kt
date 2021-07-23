package io.github.moh_mohsin.ahoyweatherapp.util

import com.google.gson.Gson
import io.github.moh_mohsin.ahoyweatherapp.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit


suspend fun <T> Response<T>.handle(): Result<T> {
    return withContext(Dispatchers.IO) {
        try {
            if (isSuccessful) {
                val result = body()!!
                Result.Success(result)
            } else {
                Result.Error(getExceptionFromErrorCode())
            }
        } catch (e: IOException) {
            Result.Error(NetworkException())
        } catch (e: Exception) {
            Result.Error(UnknownException())
        }
    }
}




fun <T> Response<T>.getExceptionFromErrorCode(): AppException {
    return try {
        val gson = Gson()

        val errorMessage: MyErrorMessage = try {
            gson.fromJson(
                this.errorBody()!!.charStream(),
                MyErrorMessage::class.java
            )
        } catch (e: Exception) {
            Timber.e(e, "exception while parsing error message")
            return UnknownException()
        }

        Timber.d("error message $errorMessage")

        when (errorMessage.cod) {
            //TODO: add different exceptions for different error codes?
            else -> GeneralException(msg = Message.Raw(errorMessage.message))
        }
    } catch (e: Exception) {
        //in case an exception is thrown due to an invalid server response
        ServerException()
    }
}

data class MyErrorMessage(val cod: Int, val message: String)


