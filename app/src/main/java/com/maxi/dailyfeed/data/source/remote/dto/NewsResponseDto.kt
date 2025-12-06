package com.maxi.dailyfeed.data.source.remote.dto

import com.maxi.dailyfeed.data.common.DataConstants.Keys as Keys
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsResponseDto(
    @SerialName(Keys.STATUS)
    val status: String,
    @SerialName(Keys.TOTAL_RESULTS)
    val totalResults: Int,
    @SerialName(Keys.ARTICLES)
    val articles: List<ArticleDto>
)
