package com.x0.newsapi.data.usecase

import com.x0.newsapi.data.NewsApiRepository
import com.x0.newsapi.data.model.sources.Source
import io.reactivex.Single
import javax.inject.Inject

class SourcesUseCase @Inject constructor(private val newsApiRepository: NewsApiRepository) {

    fun getSources(refreshing: Boolean): Single<ArrayList<Source>> = newsApiRepository.getSources()

}