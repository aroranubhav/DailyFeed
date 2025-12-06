package com.maxi.dailyfeed.data.common

class UnauthorizedException(
    errorMessage: String? = null,
    errorBody: String? = null,
    requestMethod: String? = null,
    requestUrl: String? = null
) : ApiException(
    401,
    errorMessage,
    errorBody,
    requestMethod,
    requestUrl
)