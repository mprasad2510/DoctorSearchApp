package org.mp.doctorsearchapp.di.feature.search


import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import org.mp.doctorsearchapp.di.base.BaseViewModel
import org.mp.doctorsearchapp.di.feature.login.LoginResult
import org.mp.doctorsearchapp.di.mvibase.MviActionProcessorHolder


class HomeViewModel(private val homeActionProcessorHolder:
                    HomeActionProcessorHolder
) :
        BaseViewModel<HomeIntent, HomeViewState, HomeAction, HomeResult>() {

    override fun initialState(): HomeViewState = HomeViewState.idle()
    override fun reducer(): BiFunction<HomeViewState, HomeResult, HomeViewState> = reducer
    override fun actionProcessorHolder(): MviActionProcessorHolder<HomeAction, HomeResult> =homeActionProcessorHolder
    override fun intentFilter(): ObservableTransformer<HomeIntent, HomeIntent> {
        return ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge<HomeIntent>(
                        shared.ofType(HomeIntent.InitialIntent::class.java).take(1),
                        shared.filter { it != HomeIntent.InitialIntent }
                )
            }
        }
    }

    init {
        connectObservableToLiveData()
    }

    override fun actionFromIntent(intent: HomeIntent): HomeAction {
        return when (intent) {
            is HomeIntent.InitialIntent -> HomeAction.LoadHomeAction
            is HomeIntent.SearchIntent -> HomeAction.SearchAction(intent.query)
            is HomeIntent.TypeQueryIntent -> HomeAction.TypeQueryAction(intent.typeQuery)
            is HomeIntent.ClickIntent -> HomeAction.ClickAction(intent.article)
           // is HomeIntent.LoadImageIntent -> HomeAction.LoadImageAction(intent.doctorId)
        }
    }

    companion object {
        private val reducer = BiFunction { previousState: HomeViewState, result: HomeResult ->
            when (result) {

                is HomeResult.LoadSearchResult -> {
                    when (result) {

                        is HomeResult.LoadSearchResult.Success -> {
                            previousState.copy(isLoading = false,isError = false, errorMessage = "", doctors = result.doctorList,showList = true)
                        }
                        is HomeResult.LoadSearchResult.Failure -> {
                            previousState.copy(isLoading = false, isError = true, errorMessage = result.errorMessage,showList = false)
                        }
                        is HomeResult.LoadSearchResult.InFlight -> {
                            previousState.copy(isLoading = true, isError = false, errorMessage = "", showShareOption = false,showList = false)
                        }
                    }
                }
                is HomeResult.TypeQueryResult -> {
                    previousState.copy(query = result.query, isError = false, errorMessage = "", isLoading = false)
                }

                is HomeResult.ClickResult -> {
                    //TODO: if user clicks twice in the same item share intent will not show for 2nd time coz distinctUntilChanged()
                    //TODO: Figure out a way to avoid this.
                    previousState.copy(showShareOption = true, shareArticle = result.article)
                }

//                is HomeResult.LoadImageResult -> {
//                    previousState.copy(loadImage = result.url)
//                }
            }
        }
    }

}