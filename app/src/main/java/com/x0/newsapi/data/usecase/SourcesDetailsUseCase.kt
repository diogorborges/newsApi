package com.x0.newsapi.data.usecase

import com.x0.newsapi.data.NewsApiRepository
import com.x0.newsapi.data.model.news.NewsResponse
import io.reactivex.Single
import javax.inject.Inject

class SourcesDetailsUseCase @Inject constructor(val newsApiRepository: NewsApiRepository) {

    fun getSourcesById(sources: String): Single<NewsResponse> =
        newsApiRepository.getSourceById(sources)

}