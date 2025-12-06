package com.maxi.dailyfeed.data.mapper

import com.maxi.dailyfeed.data.source.local.entity.ArticleEntity
import com.maxi.dailyfeed.data.source.remote.dto.ArticleDto
import com.maxi.dailyfeed.domain.model.Article

fun ArticleDto.toArticleEntity(): ArticleEntity =
    ArticleEntity(
        title,
        description,
        url,
        imageUrl,
        publishedAt
    )

fun ArticleEntity.toArticle(): Article =
    Article(
        title,
        description ?: "Description not found!",
        url,
        imageUrl ?: "",
        publishedAt ?: ""
    )

fun List<ArticleDto>.toEntityList(): List<ArticleEntity> = map { it.toArticleEntity() }

fun List<ArticleEntity>.toDomainList(): List<Article> = map { it.toArticle() }