package org.mp.doctorsearchapp.di.feature.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.util.Log
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.android.HasActivityInjector
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_login.*
import org.mp.doctorsearchapp.R
import org.mp.doctorsearchapp.data.remote.model.Login
import org.mp.doctorsearchapp.di.base.BaseActivity
import org.mp.doctorsearchapp.di.feature.search.HomeActivity
import org.mp.doctorsearchapp.di.mvibase.MviView
import org.mp.doctorsearchapp.utils.gone
import org.mp.doctorsearchapp.utils.shortToast
import org.mp.doctorsearchapp.utils.visible
import java.util.concurrent.TimeUnit
import javax.inject.Inject

open class LoginActivity : BaseActivity(), MviView<LoginIntent, LoginViewState>, HasActivityInjector {

    override fun layoutId(): Int = R.layout.activity_login
    @Inject
    lateinit var factory: LoginViewmodelFactory


    private val viewModel: LoginViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)
    }

    private fun initialIntent(): Observable<LoginIntent.InitialIntent> {
        return Observable.just(LoginIntent.InitialIntent)
    }

    private val doLoginIntent = PublishSubject.create<LoginIntent.DoLoginIntent>()
    private val typeUserIdIntent = PublishSubject.create<LoginIntent.typeUserIdIntent>()
    private val typePasswordIntent = PublishSubject.create<LoginIntent.typePasswordIntent>()

     override fun bind() {
        viewModel.processIntents(intents())
        viewModel.states().observe(this, Observer { if (it != null) render(it) })


        login.setOnClickListener { doLoginIntent.onNext(LoginIntent.DoLoginIntent(
                userId = userIdEt.text.toString(),
                password = passwordEt.text.toString())) }

        disposable.add(RxTextView.textChanges(userIdEt).debounce(300, TimeUnit.MILLISECONDS).subscribe {
                text -> typeUserIdIntent.onNext(LoginIntent.typeUserIdIntent(text.toString())) })
        disposable.add(RxTextView.textChanges(passwordEt).debounce(300, TimeUnit.MILLISECONDS).subscribe { text -> typePasswordIntent.onNext(LoginIntent.typePasswordIntent(text.toString())) })
    }

    override fun intents(): Observable<LoginIntent> {
        return Observable.merge(initialIntent(), doLoginIntent, typeUserIdIntent, typePasswordIntent)
    }

    override fun render(state: LoginViewState) {
        with(state) {
            when (isLoading) {
                true -> {
                    progressBar.visible()
                    login.isEnabled = false
                }
                else -> {
                    progressBar.gone()
                    login.isEnabled = true
                }
            }
            if (userIdEt.text.toString().isEmpty()) {
                with(userIdEt) {
                    setText(userId)
                    setSelection(userId.length)
                }

            }
            if (userIdEt.text.toString().isEmpty()) {
                with(passwordEt) {
                    setText(password)
                    setSelection(password.length)
                }

            }
            if(isError){
                errorMessage.shortToast(this@LoginActivity)
                login.isEnabled = true
                progressBar.gone()
            }
             if(isLoginSuccessful){
                 val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                 intent.putExtra("accessToken",accessToken.toString())
                 Log.d("****ACCESSTOKEN****","$accessToken")
                 startActivity(intent)
             }

        }
    }
}
