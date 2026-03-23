package com.maxi.dailyfeed.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maxi.dailyfeed.data.common.DataConstants.Tables.NEWS_SOURCES

@Entity(NEWS_SOURCES)
data class NewsSourceEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("name")
    val name: String,
    @ColumnInfo("description")
    val description: String?,
    @ColumnInfo("url")
    val url: String,
)
