package org.mp.doctorsearchapp.di.feature.login

import org.mp.doctorsearchapp.di.mvibase.MviResult


sealed class LoginResult : MviResult {
    sealed class DoLoginResult : LoginResult() {
        data class Success(val accessToken: String?) : DoLoginResult()
        data class Failure(val errorMessage: String) : DoLoginResult()
        object InFlight : DoLoginResult()
    }

    sealed class LoadLoginResult : LoginResult() {
        data class Success(val welcomeMessage: String) : LoadLoginResult()
    }

    data class TypePasswordResult(val password: String) : LoginResult()

    data class TypeUserIdResult(val userId: String) : LoginResult()
}