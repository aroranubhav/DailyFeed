package com.maxi.dailyfeed.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maxi.dailyfeed.data.common.DataConstants.Tables.NEWS_ARTICLES

@Entity(NEWS_ARTICLES)
data class ArticleEntity(
    @ColumnInfo("author")
    val author: String,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("description")
    val description: String?,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("url")
    val url: String,
    @ColumnInfo("urlToImage")
    val imageUrl: String?,
    @ColumnInfo("publishedAt")
    val publishedAt: Long?,
    @ColumnInfo("content")
    val content: String?,
    @ColumnInfo("sourceId")
    val sourceId: String,
    @ColumnInfo("sourceName")
    val sourceName: String
)
