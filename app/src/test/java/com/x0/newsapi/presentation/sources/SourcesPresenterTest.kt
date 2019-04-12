package com.x0.newsapi.presentation.sources

import com.x0.newsapi.data.model.sources.Source
import com.x0.newsapi.data.usecase.SourcesUseCase
import com.x0.newsapi.test.DefaultPluginTestSetup
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class SourcesPresenterTest : DefaultPluginTestSetup() {

    @InjectMocks
    private lateinit var sourcesPresenter: SourcesPresenter

    @Mock
    private lateinit var view: SourcesFragment

    @Mock
    private lateinit var sourcesUseCase: SourcesUseCase

    @Before
    fun setup() = MockitoAnnotations.initMocks(this)

    @Test
    fun testGetSources() {
        val source = mock(Source::class.java)
        val sourceList = ArrayList<Source>(1)
        sourceList.add(source)

        `when`(sourcesUseCase.getSources()).thenReturn(Single.just(sourceList))

        sourcesPresenter.setView(view)

        verify(view).showLoader(true)
        verify(view).showSources(ArgumentMatchers.anyList())
        verify(view).showLoader(false)
    }

    @Test
    fun testGetSourcesError() {
        val source = mock(Source::class.java)
        val sourceList = ArrayList<Source>(1)
        sourceList.add(source)

        `when`(sourcesUseCase.getSources()).thenReturn(Single.error(Exception()))

        sourcesPresenter.setView(view)

        verify(view).showLoader(true)
        verify(view).showError(null)
        verify(view).showLoader(false)
    }
}