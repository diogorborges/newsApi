package com.x0.newsapi.data.usecase

import com.x0.newsapi.data.NewsApiRepository
import com.x0.newsapi.data.model.news.Article
import io.reactivex.Single
import javax.inject.Inject

class ArticleUseCase @Inject constructor(private val newsApiRepository: NewsApiRepository) {

    fun getArticles(sourceId: String, pageNumber: Int): Single<ArrayList<Article>> =
        newsApiRepository.getArticles(sourceId, pageNumber)

    fun shouldLoadMoreArticles(): Boolean = newsApiRepository.shouldLoadMoreArticles()

    fun getArticlesPageNumber(): Int = newsApiRepository.getArticlesPageNumber()

    fun saveShouldLoadArticles(shouldLoadMoreArticles: Boolean) =
        newsApiRepository.saveShouldLoadMoreArticles(shouldLoadMoreArticles)

    fun getArticlesTotalResult(): Int = newsApiRepository.getArticlesTotalResult()

    fun getArticlesListSize(): Int = newsApiRepository.getArticlesListSize()

    fun clearArticles() = newsApiRepository.clearArticles()

}
