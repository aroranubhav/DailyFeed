package com.maxi.dailyfeed.data.source.common

class ForbiddenException(
    errorMessage: String? = null,
    errorBody: String? = null,
    requestMethod: String? = null,
    requestUrl: String? = null
) : ApiException(
    403,
    errorMessage,
    errorBody,
    requestMethod,
    requestUrl
)