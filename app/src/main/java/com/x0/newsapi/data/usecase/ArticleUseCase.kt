package com.x0.newsapi.data.usecase

import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.repository.ArticleRepository
import io.reactivex.Single
import javax.inject.Inject

class ArticleUseCase @Inject constructor(private val articleRepository: ArticleRepository) {

    fun getArticles(sourceId: String, pageNumber: Int): Single<ArrayList<Article>> =
        articleRepository.getArticles(sourceId, pageNumber)

    fun shouldLoadMoreArticles(): Boolean = articleRepository.shouldLoadMoreArticles()

    fun getArticlesPageNumber(): Int = articleRepository.getArticlesPageNumber()

    fun saveShouldLoadArticles(shouldLoadMoreArticles: Boolean) =
        articleRepository.saveShouldLoadMoreArticles(shouldLoadMoreArticles)

    fun getArticlesTotalResult(): Int = articleRepository.getArticlesTotalResult()

    fun getArticlesListSize(): Int = articleRepository.getArticlesListSize()

    fun clearArticles() = articleRepository.clearArticles()
}
