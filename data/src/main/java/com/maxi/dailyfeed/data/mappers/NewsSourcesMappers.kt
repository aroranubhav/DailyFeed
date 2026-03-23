package com.maxi.dailyfeed.data.mappers

import com.maxi.dailyfeed.data.source.local.entity.NewsSourceEntity
import com.maxi.dailyfeed.data.source.remote.dto.NewsSourceDto
import com.maxi.dailyfeed.domain.model.NewsSource

/*
dto to entity
 */
fun NewsSourceDto.toEntity(): NewsSourceEntity =
    NewsSourceEntity(
        id,
        name,
        description,
        url
    )

fun List<NewsSourceDto>.toEntityList(): List<NewsSourceEntity> =
    map {
        it.toEntity()
    }

/*
entity to domain
 */
fun NewsSourceEntity.toDomain(): NewsSource =
    NewsSource(
        id,
        name,
        description ?: "",
        url
    )

fun List<NewsSourceEntity>.toDomainList(): List<NewsSource> =
    map {
        it.toDomain()
    }

/*
dto to domain
 */
fun NewsSourceDto.asDomain(): NewsSource =
    NewsSource(
        id,
        name,
        description ?: "",
        url
    )

fun List<NewsSourceDto>.asDomainList(): List<NewsSource> =
    map {
        it.asDomain()
    }