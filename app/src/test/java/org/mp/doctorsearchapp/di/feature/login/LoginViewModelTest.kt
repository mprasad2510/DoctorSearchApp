package org.mp.doctorsearchapp.di.feature.login

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mp.doctorsearchapp.data.Repository
import org.mp.doctorsearchapp.data.remote.model.Login
import org.mp.doctorsearchapp.utils.schedulers.BaseSchedulerProvider
import org.mp.doctorsearchapp.utils.schedulers.ImmediateSchedulerProvider
import org.mockito.Mockito.`when` as whenever


class LoginViewModelTest {
    @Mock
    private lateinit var repository: Repository
    private lateinit var schedulerProvider: BaseSchedulerProvider
    private lateinit var loginViewModel: LoginViewModel
    @Mock
    lateinit var observer: Observer<LoginViewState>

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUpLoginViewModel() {
        MockitoAnnotations.initMocks(this)
        schedulerProvider = ImmediateSchedulerProvider()
        loginViewModel = LoginViewModel(LoginActionProcessorHolder(repository, schedulerProvider))
        loginViewModel.states().observeForever(observer)
    }

    @Test
    fun initialIntentTest() {
        loginViewModel.processIntents(Observable.just(LoginIntent.InitialIntent))
        verify(observer).onChanged(LoginViewState(
                isLoading = false,
                userId = "",
                password = "",
                accessToken = null,
                errorMessage = "",
                isError = false,
                isLoginSuccessful = false
                  ))
    }

    @Test
    fun typePasswordIntentTest() {
        loginViewModel.processIntents(Observable.just(LoginIntent.typePasswordIntent(password)))
        assert(loginViewModel.states().value?.password == password)

    }

    @Test
    fun typeUserIdIntentTest() {
        loginViewModel.processIntents(Observable.just(LoginIntent.typeUserIdIntent(userId)))
        assert(loginViewModel.states().value?.userId == userId)
    }

    @Test
    fun loginWithEmptyUsernamePassword() {
        loginViewModel.processIntents(Observable.just(LoginIntent.DoLoginIntent("", "")))
        verify(observer).onChanged(LoginViewState(isError = true,
                isLoading = false,
                password = "",
                userId = "",
                isLoginSuccessful = false,
                errorMessage = userIdPasswordError,
                accessToken = null))
    }


    @Test
    fun loginWithValidUsernamePassword() {
        whenever(repository.doLogin()).thenReturn(Observable.just(loginResponse))
        loginViewModel.processIntents(Observable.just(LoginIntent.DoLoginIntent(password, password)))
        //Loading ViewSstate
        verify(observer).onChanged(LoginViewState(isError = false,
                isLoading = true,
                password = "",
                userId = "",
                isLoginSuccessful = false,
                errorMessage = "",
                accessToken = null))

        //LoginSuccess Viewstate
        verify(observer).onChanged(LoginViewState(isError = false,
                isLoading = false,
                password = "",
                userId = "",
                isLoginSuccessful = true,
                errorMessage = "",
                accessToken = ""))
    }

    @Test
    fun loginFailTest() {
        whenever(repository.doLogin()).thenReturn(Observable.error(throwable))
        loginViewModel.processIntents(Observable.just(LoginIntent.DoLoginIntent(userId, password)))
        verify(observer).onChanged(LoginViewState(isLoading = false,
                userId = "",
                password = "",
                errorMessage = errormessage,
                isError = true,
                isLoginSuccessful = false,
                accessToken = null))


    }

    companion object {
        private const val errormessage = "This is some kind of error"
        private val throwable = Throwable(errormessage)
        private val loginResponse = Login("")
        private val password ="password"
        private val userId = "userId"
        private val userIdPasswordError = "UserId or password can't be empty"
    }

}
