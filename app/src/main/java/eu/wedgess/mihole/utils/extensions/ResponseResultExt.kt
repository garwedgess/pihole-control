package eu.wedgess.mihole.utils.extensions

import eu.wedgess.mihole.data.model.ResponseResult

fun <E> ResponseResult.Error<E>.handleError(): Throwable =
    when (this) {
        is ResponseResult.Error.Api -> Throwable(this.body as String? ?: "Unknown error")
        is ResponseResult.Error.Network -> this.exception
        is ResponseResult.Error.Serialization -> this.exception
        is ResponseResult.Error.Unknown -> this.exception
    }