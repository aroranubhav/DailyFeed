package com.maxi.dailyfeed.data.common

class InternalServerException(
    errorMessage: String? = null,
    errorBody: String? = null,
    requestMethod: String? = null,
    requestUrl: String? = null
) : ApiException(
    500,
    errorMessage,
    errorBody,
    requestMethod,
    requestUrl
)