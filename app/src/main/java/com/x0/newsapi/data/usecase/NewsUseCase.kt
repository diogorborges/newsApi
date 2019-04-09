package com.x0.newsapi.data.usecase

import com.x0.newsapi.data.NewsApiRepository
import com.x0.newsapi.data.model.news.Article
import io.reactivex.Single
import javax.inject.Inject

class NewsUseCase @Inject constructor(private val newsApiRepository: NewsApiRepository) {

    fun getNews(pageNumber: Int, isRefreshing: Boolean): Single<ArrayList<Article>> =
        newsApiRepository.getNews(pageNumber, isRefreshing)

    fun shouldLoadMore() : Boolean = newsApiRepository.shouldLoadMore()

    fun getPageNumber(): Int = newsApiRepository.getPageNumber()

    fun saveShouldLoadMore(shouldLoadMore: Boolean) = newsApiRepository.saveShouldLoadMore(shouldLoadMore)
}