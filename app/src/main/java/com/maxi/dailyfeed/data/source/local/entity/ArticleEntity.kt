package com.maxi.dailyfeed.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maxi.dailyfeed.data.source.common.DataConstants.Keys as Keys
import com.maxi.dailyfeed.data.source.common.DataConstants.Tables.ARTICLES

@Entity(tableName = ARTICLES)
data class ArticleEntity(
    @ColumnInfo(Keys.TITLE)
    val title: String,
    @ColumnInfo(Keys.DESCRIPTION)
    val description: String?,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(Keys.URL)
    val url: String,
    @ColumnInfo(Keys.IMAGE_URL)
    val imageUrl: String?,
    @ColumnInfo(Keys.PUBLISHED_AT)
    val publishedAt: String?
)
