package com.maxi.dailyfeed.data.source.remote.dto

import kotlinx.serialization.SerialName

data class NewsSourceDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String?,
    @SerialName("url")
    val url: String,
)
