package com.maxi.dailyfeed.data.common

class NotFoundException(
    errorMessage: String? = null,
    errorBody: String? = null,
    requestMethod: String? = null,
    requestUrl: String? = null
) : ApiException(
    404,
    errorMessage,
    errorBody,
    requestMethod,
    requestUrl
)