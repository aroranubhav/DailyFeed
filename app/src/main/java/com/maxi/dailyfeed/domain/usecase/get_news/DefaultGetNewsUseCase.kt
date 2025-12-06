package com.maxi.dailyfeed.domain.usecase.get_news

import com.maxi.dailyfeed.common.Resource
import com.maxi.dailyfeed.data.repository.DefaultNewsRepository
import com.maxi.dailyfeed.domain.model.Article
import com.maxi.dailyfeed.domain.repository.NewsRepository
import com.maxi.dailyfeed.domain.usecase.common.Utils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultGetNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) : GetNewsUseCase {

    override fun getNews(
        language: String,
        country: String,
    ): Flow<Resource<List<Article>>> {
        val (languageCode, countryCode) = Utils.normalizeInputs(language, country)

        return repository.getNews(languageCode, countryCode)
    }
}