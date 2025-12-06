package com.maxi.dailyfeed.data.source.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.maxi.dailyfeed.data.common.DataConstants.Keys as Keys

@Serializable
data class ArticleDto(
    @SerialName(Keys.TITLE)
    val title: String,
    @SerialName(Keys.DESCRIPTION)
    val description: String?,
    @SerialName(Keys.URL)
    val url: String,
    @SerialName(Keys.IMAGE_URL)
    val imageUrl: String?,
    @SerialName(Keys.PUBLISHED_AT)
    val publishedAt: String?
)
