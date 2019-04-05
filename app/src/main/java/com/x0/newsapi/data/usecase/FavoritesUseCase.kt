package com.x0.newsapi.data.usecase

import com.x0.newsapi.data.NewsApiRepository
import com.x0.newsapi.data.model.sources.Source
import io.reactivex.Single
import javax.inject.Inject

class FavoritesUseCase @Inject constructor(val newsApiRepository: NewsApiRepository) {

    fun getSourcesBySubject(isFavorite: Boolean): Single<ArrayList<Source>> =
        newsApiRepository.getFavoriteSources(isFavorite)

}

