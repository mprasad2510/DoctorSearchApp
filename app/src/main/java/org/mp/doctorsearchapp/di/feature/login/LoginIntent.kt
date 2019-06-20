package org.mp.doctorsearchapp.di.feature.login

import org.mp.doctorsearchapp.di.mvibase.MviIntent


sealed class LoginIntent : MviIntent {
    object InitialIntent : LoginIntent()
    data class DoLoginIntent(val userId: String, val password: String) : LoginIntent()
    data class typeUserIdIntent(val userId: String) : LoginIntent()
    data class typePasswordIntent(val password: String) : LoginIntent()

}