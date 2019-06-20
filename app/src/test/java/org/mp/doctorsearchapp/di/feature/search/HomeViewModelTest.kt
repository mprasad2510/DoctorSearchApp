package org.mp.doctorsearchapp.di.feature.search

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mp.doctorsearchapp.data.Repository
import org.mp.doctorsearchapp.data.remote.model.Doctors
import org.mp.doctorsearchapp.data.remote.model.SearchDoctors
import org.mp.doctorsearchapp.utils.schedulers.BaseSchedulerProvider
import org.mp.doctorsearchapp.utils.schedulers.ImmediateSchedulerProvider


class HomeViewModelTest {
    @Mock
    private lateinit var repository: Repository
    private lateinit var schedulerProvider: BaseSchedulerProvider
    private lateinit var homeViewModel: HomeViewModel
    @Mock
    lateinit var observer: Observer<HomeViewState>

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUpLoginViewModel() {
        MockitoAnnotations.initMocks(this)
        schedulerProvider = ImmediateSchedulerProvider()
        homeViewModel = HomeViewModel(HomeActionProcessorHolder(repository, schedulerProvider))
        homeViewModel.states().observeForever(observer)
    }

    @Test
    fun InitialIntentTest() {
        val searchDoctors = SearchDoctors(listOf<Doctors>(),"")
        whenever(repository.loadList("",0.0,0.0,"")).thenReturn(
            Observable.just(searchDoctors))
        homeViewModel.processIntents(Observable.just(HomeIntent.InitialIntent))

        verify(observer).onChanged(HomeViewState(
                isLoading = false,
                query = "",
                isError = false,
                errorMessage = "",
                doctors = emptyList(),
                showShareOption = false,
                shareArticle = null,
                showList = false
        ))
    }

    @Test
    fun LoadErrorTest() {
        whenever(repository.loadList("",0.0,0.0,"")).thenReturn(Observable.error(Throwable("This is somekind of error")))
        homeViewModel.processIntents(Observable.just(HomeIntent.InitialIntent))
        verify(observer).onChanged(HomeViewState(
                isLoading = false,
                query = "",
                isError = false,
                errorMessage = "",
                doctors = emptyList(),
                shareArticle = null,
                showShareOption = false,
                showList = false
        ))
    }

    @Test
    fun onClickTest() {
        val article = Doctors("", "", "", 0.0 ,"", "","","")
        homeViewModel.processIntents(Observable.just(HomeIntent.ClickIntent(article)))
        verify(observer).onChanged(HomeViewState(
                isLoading = false,
                query = "",
                isError = false,
                errorMessage = "",
                doctors = emptyList(),
                shareArticle = article,
                showShareOption = true,
                showList = false
        ))
    }

}