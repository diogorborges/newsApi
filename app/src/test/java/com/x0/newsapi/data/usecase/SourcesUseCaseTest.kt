package com.x0.newsapi.data.usecase

import com.x0.newsapi.data.model.sources.Source
import com.x0.newsapi.data.repository.SourcesRepository
import com.x0.newsapi.test.DefaultPluginTestSetup
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class SourcesUseCaseTest : DefaultPluginTestSetup() {

    @InjectMocks
    private lateinit var sourcesUseCase: SourcesUseCase

    @Mock
    private lateinit var sourceRepository: SourcesRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testGetSources() {
        val source = Mockito.mock(Source::class.java)
        val sourceList = java.util.ArrayList<Source>(1)
        sourceList.add(source)

        `when`(sourceRepository.getSources()).thenReturn(Single.just(sourceList))
        val testObserver = TestObserver<ArrayList<Source>>()

        sourcesUseCase.getSources().subscribe(testObserver)

        verify(sourceRepository).getSources()
    }
}