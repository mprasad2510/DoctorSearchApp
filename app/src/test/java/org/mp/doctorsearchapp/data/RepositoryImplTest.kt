package org.mp.doctorsearchapp.data

import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mp.doctorsearchapp.data.remote.Network
import org.mp.doctorsearchapp.data.remote.model.Doctors
import org.mp.doctorsearchapp.data.remote.model.Login
import org.mp.doctorsearchapp.data.remote.model.SearchDoctors


class RepositoryImplTest {
    @Mock
    private lateinit var network: Network
    private lateinit var repository: RepositoryImpl
    @Before
    fun setUpRepository(){
        MockitoAnnotations.initMocks(this)
        repository = RepositoryImpl(network)
    }

    @Test
    fun loadListTest(){
        whenever(network.loadList("",0.0,0.0,"")).thenReturn(Observable.just(listResponse))
        repository.loadList("",0.0,0.0,"")
        Mockito.verify<Network>(network).loadList("",0.0,0.0,"")
    }

    @Test
    fun doLogin(){
        whenever(network.doLogin()).thenReturn(Observable.just(loginResponse))
        repository.doLogin()
        Mockito.verify<Network>(network).doLogin()
    }

    companion object {
        private val listResponse = SearchDoctors(listOf<Doctors>(),"")
        private val loginResponse = Login("")
    }
}