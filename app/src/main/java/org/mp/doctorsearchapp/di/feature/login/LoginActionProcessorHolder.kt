package org.mp.doctorsearchapp.di.feature.login

import android.arch.lifecycle.Transformations.map
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import org.mp.doctorsearchapp.data.Repository
import org.mp.doctorsearchapp.data.remote.model.Login
import org.mp.doctorsearchapp.di.feature.search.HomeActivity
import org.mp.doctorsearchapp.di.mvibase.MviActionProcessorHolder
import org.mp.doctorsearchapp.utils.schedulers.BaseSchedulerProvider
import javax.inject.Inject


class LoginActionProcessorHolder @Inject constructor(private val repository: Repository,
                                                     private val schedulerProvider: BaseSchedulerProvider
) : MviActionProcessorHolder<LoginAction, LoginResult> {
     override fun transformFromAction(): ObservableTransformer<LoginAction, LoginResult> {
        return ObservableTransformer { action ->
            action.publish { shared ->
                Observable.merge(
                        shared.ofType(LoginAction.LoadLoginAction::class.java).compose(loadLogin()),
                        shared.ofType(LoginAction.DoLoginAction::class.java).compose(doLogin()),
                        shared.ofType(LoginAction.TypeUserIdAction::class.java).compose(typeUserId()),
                        shared.ofType(LoginAction.TypePasswordAction::class.java).compose(typeUserPassword())
                )
            }

        }
    }

    private fun typeUserId(): ObservableTransformer<LoginAction.TypeUserIdAction, LoginResult.TypeUserIdResult> {
        return ObservableTransformer { action ->
            action.flatMap { Observable.just(LoginResult.TypeUserIdResult(it.userId)) }
        }

    }

    private fun typeUserPassword(): ObservableTransformer<LoginAction.TypePasswordAction, LoginResult.TypePasswordResult> {
        return ObservableTransformer { action ->
            action.flatMap { Observable.just(LoginResult.TypePasswordResult(it.password)) }
        }

    }

    private fun doLogin(): ObservableTransformer<LoginAction.DoLoginAction, LoginResult.DoLoginResult> {
        return ObservableTransformer { action ->
            action.flatMap {
                if(!it.password.isEmpty() && !it.userId.isEmpty()) {
                    repository.doLogin()
                            .map { response -> LoginResult.DoLoginResult.Success(response.accessToken)
                            }
                            .cast(LoginResult.DoLoginResult::class.java)
                            .onErrorReturn {
                                t -> LoginResult.DoLoginResult.Failure(t.localizedMessage)
                            }
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .startWith(LoginResult.DoLoginResult.InFlight)
                }else{
                    Observable.just(LoginResult.DoLoginResult.Failure("UserId or password can't be empty"))
                }
            }
        }
    }



    private fun loadLogin(): ObservableTransformer<LoginAction.LoadLoginAction, LoginResult.LoadLoginResult> {
        return ObservableTransformer { action ->
            action.flatMap {
                Observable.just(LoginResult.LoadLoginResult.Success("Welcome"))
            }
        }
    }


}