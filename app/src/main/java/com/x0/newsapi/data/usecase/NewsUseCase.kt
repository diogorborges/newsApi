package com.x0.newsapi.data.usecase

import com.x0.newsapi.data.NewsApiRepository
import com.x0.newsapi.data.model.news.Article
import io.reactivex.Single
import javax.inject.Inject

class NewsUseCase @Inject constructor(val newsApiRepository: NewsApiRepository) {

    fun getNews(page: Int): Single<ArrayList<Article>> =
        newsApiRepository.getNews(page)

    fun isFirstLoad() : Boolean = newsApiRepository.isFirstLoad()

    fun getPageNumber(): Int = newsApiRepository.getPageNumber()

    fun saveIsFirstLoad(isFirstLoad: Boolean) = newsApiRepository.saveIsFirstLoad(isFirstLoad)
}