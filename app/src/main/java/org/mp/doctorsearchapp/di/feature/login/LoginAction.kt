package org.mp.doctorsearchapp.di.feature.login

import org.mp.doctorsearchapp.di.mvibase.MviAction


sealed class LoginAction : MviAction {
    object LoadLoginAction : LoginAction()
    data class DoLoginAction(val userId: String, val password: String): LoginAction()
    data class TypeUserIdAction(val userId: String) : LoginAction()
    data class TypePasswordAction(val password: String) : LoginAction()
}