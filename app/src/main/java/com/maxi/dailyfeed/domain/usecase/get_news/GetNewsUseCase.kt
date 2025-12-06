package com.maxi.dailyfeed.domain.usecase.get_news

import com.maxi.dailyfeed.common.Resource
import com.maxi.dailyfeed.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface GetNewsUseCase {

    fun getNews(
        language: String,
        country: String
    ): Flow<Resource<List<Article>>>
}