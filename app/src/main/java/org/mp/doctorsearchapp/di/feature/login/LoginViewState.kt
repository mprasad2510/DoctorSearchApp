package org.mp.doctorsearchapp.di.feature.login

import org.mp.doctorsearchapp.di.mvibase.MviViewState

data class LoginViewState(val isLoading: Boolean,
                          val userId: String,
                          val password: String,
                          val accessToken : String?,
                          val errorMessage: String,
                          val isError: Boolean,
                          val isLoginSuccessful: Boolean
) : MviViewState {


    companion object {
        fun idle(): LoginViewState {
            return LoginViewState(
                    isLoading = false,
                    userId = "",
                    password = "",
                    accessToken = null,
                    isError = false,
                    errorMessage = "",
                    isLoginSuccessful = false
            )
        }
    }
}