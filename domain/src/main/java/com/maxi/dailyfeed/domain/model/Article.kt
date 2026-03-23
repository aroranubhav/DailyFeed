package com.maxi.dailyfeed.domain.model

data class Article(
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val imageUrl: String,
    val publishedAt: Long?,
    val content: String,
    val sourceId: String,
    val sourceName: String,
)
