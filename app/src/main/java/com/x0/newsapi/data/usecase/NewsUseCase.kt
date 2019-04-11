package com.x0.newsapi.data.usecase

import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.repository.NewsRepository
import io.reactivex.Single
import javax.inject.Inject

class NewsUseCase @Inject constructor(val newsRepository: NewsRepository) {

    fun getNews(pageNumber: Int, isRefreshing: Boolean): Single<ArrayList<Article>> =
        newsRepository.getNews(pageNumber, isRefreshing)

    fun shouldLoadMoreNews(): Boolean = newsRepository.shouldLoadMoreNews()

    fun getNewsPageNumber(): Int = newsRepository.getNewsPageNumber()

    fun saveShouldLoadMoreNews(shouldLoadMoreNews: Boolean) =
        newsRepository.saveShouldLoadMoreNews(shouldLoadMoreNews)
}