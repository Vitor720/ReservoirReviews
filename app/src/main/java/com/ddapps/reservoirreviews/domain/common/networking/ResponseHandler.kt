package com.ddapps.reservoirreviews.domain.common.networking

import retrofit2.HttpException
import java.net.SocketTimeoutException

open class ResponseHandler {
    fun <T : Any> handleSuccess(data: T?): Resource<T> {
        return Resource.success(data)
    }

    fun <T : Any> handleThrowable(t: Throwable): Resource<T> {
        return when (t) {
			is NullPointerException -> Resource.error(ErrorCodes.NoContent.toString(), null)
            is HttpException -> Resource.error(getErrorMessage(t.code()), null)
            is SocketTimeoutException -> Resource.error(getErrorMessage(ErrorCodes.SocketTimeOut.code), null)
            else -> Resource.error(t.message, null)
        }
    }

	private fun getErrorMessage(code: Int): String {
		return when (code) {
			ErrorCodes.SocketTimeOut.code -> "Timeout"
			204							  -> "Retorno vazio"
			401                           -> "Não autorizado"
			404                           -> "Não encontrado"
			else                          -> "Error $code"
		}
	}

}

