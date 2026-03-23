@file:OptIn(ExperimentalTime::class)

package com.maxi.dailyfeed.data.mappers

import com.maxi.dailyfeed.data.source.local.entity.ArticleEntity
import com.maxi.dailyfeed.data.source.remote.dto.ArticleDto
import com.maxi.dailyfeed.domain.model.Article
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/*
dto to entity
 */
fun ArticleDto.toEntity(): ArticleEntity =
    ArticleEntity(
        author,
        title,
        description ?: "",
        url,
        imageUrl,
        publishedAt?.let { Instant.parse(it).toEpochMilliseconds() },
        content,
        source.id,
        source.name
    )

fun List<ArticleDto>.toEntityList(): List<ArticleEntity> =
    map {
        it.toEntity()
    }

/*
entity to domain
 */
fun ArticleEntity.toDomain(): Article =
    Article(
        author,
        title,
        description ?: "",
        url,
        imageUrl ?: "",
        publishedAt,
        content ?: "",
        sourceId,
        sourceName
    )

fun List<ArticleEntity>.toDomainList(): List<Article> =
    map {
        it.toDomain()
    }

/*
dto to domain
 */
fun ArticleDto.asDomain(): Article =
    Article(
        author,
        title,
        description ?: "",
        url,
        imageUrl ?: "",
        publishedAt?.let { Instant.parse(it).toEpochMilliseconds() },
        content ?: "",
        source.id,
        source.name
    )

fun List<ArticleDto>.asDomainList(): List<Article> =
    map {
        it.asDomain()
    }
