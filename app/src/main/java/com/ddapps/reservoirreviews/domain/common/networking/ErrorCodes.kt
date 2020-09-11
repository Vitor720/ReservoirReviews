package com.ddapps.reservoirreviews.domain.common.networking

enum class ErrorCodes(val code: Int) {
	NoContent(204),
	SocketTimeOut(-1)
}