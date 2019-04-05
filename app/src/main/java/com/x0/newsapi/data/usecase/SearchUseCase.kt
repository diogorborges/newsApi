package com.x0.newsapi.data.usecase

import com.x0.newsapi.data.NewsApiRepository
import com.x0.newsapi.data.model.news.NewsResponse
import io.reactivex.Single
import javax.inject.Inject

class SearchUseCase @Inject constructor(val newsApiRepository: NewsApiRepository) {

    fun getSourcesBySubject(subject: String): Single<NewsResponse> =
        newsApiRepository.getSourcesBySubject(subject)

}