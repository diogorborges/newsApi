package com.x0.newsapi.data.usecase

import com.x0.newsapi.data.model.sources.Source
import com.x0.newsapi.data.repository.SourcesRepository
import io.reactivex.Single
import javax.inject.Inject

class SourcesUseCase @Inject constructor(private val sourcesRepository: SourcesRepository) {

    fun getSources(): Single<ArrayList<Source>> = sourcesRepository.getSources()
}