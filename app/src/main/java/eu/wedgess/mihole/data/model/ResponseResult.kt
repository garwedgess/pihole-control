package eu.wedgess.mihole.data.model

import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException

sealed class ResponseResult<out T, out E> {

    data class Success<T>(val data: T) : ResponseResult<T, Nothing>()

    sealed class Error<E> : ResponseResult<Nothing, E>() {
        data class Network(val exception: IOException) : Error<Nothing>()
        data class Api<E>(val code: Int, val body: E?) : Error<E>()
        data class Serialization(val exception: SerializationException) : Error<Nothing>()
        data class Unknown(val exception: Exception) : Error<Nothing>()
    }

}
