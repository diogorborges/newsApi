package com.x0.newsapi.data.usecase

import com.x0.newsapi.data.NewsApiRepository
import com.x0.newsapi.data.model.news.Article
import io.reactivex.Single
import javax.inject.Inject

class NewsUseCase @Inject constructor(private val newsApiRepository: NewsApiRepository) {

    fun getNews(pageNumber: Int, isRefreshing: Boolean): Single<ArrayList<Article>> =
        newsApiRepository.getNews(pageNumber, isRefreshing)

    fun shouldLoadMoreNews() : Boolean = newsApiRepository.shouldLoadMoreNews()

    fun getNewsPageNumber(): Int = newsApiRepository.getNewsPageNumber()

    fun saveShouldLoadMoreNews(shouldLoadMoreNews: Boolean) = newsApiRepository.saveShouldLoadMoreNews(shouldLoadMoreNews)
}