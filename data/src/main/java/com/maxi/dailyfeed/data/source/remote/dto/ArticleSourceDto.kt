package com.maxi.dailyfeed.data.source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleSourceDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String
)
