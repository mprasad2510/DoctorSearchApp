package org.mp.doctorsearchapp.di.feature.search

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import org.mp.doctorsearchapp.data.Repository
import org.mp.doctorsearchapp.data.remote.model.Doctors
import org.mp.doctorsearchapp.di.feature.search.HomeActivity.Companion.mAccessToken
import org.mp.doctorsearchapp.di.mvibase.MviActionProcessorHolder
import org.mp.doctorsearchapp.utils.schedulers.BaseSchedulerProvider
import javax.inject.Inject

class HomeActionProcessorHolder @Inject constructor(private val repository: Repository,
                                                    private val schedulerProvider: BaseSchedulerProvider
)
                                                  : MviActionProcessorHolder<HomeAction, HomeResult> {
    override fun transformFromAction(): ObservableTransformer<HomeAction, HomeResult> {
        return ObservableTransformer { action ->
            action.publish { shared ->
                Observable.merge(
                        shared.ofType(HomeAction.SearchAction::class.java).compose(loadSearchResults()),
                        shared.ofType(HomeAction.TypeQueryAction::class.java).compose(typeQuery()),
                        shared.ofType(HomeAction.ClickAction::class.java).compose(shareArticle())
                        //shared.ofType(HomeAction.LoadImageAction::class.java).compose(loadImage())

                )
            }
        }
    }


    private fun typeQuery(): ObservableTransformer<HomeAction.TypeQueryAction, HomeResult.TypeQueryResult> {
        return ObservableTransformer { action ->
            action.flatMap { Observable.just(HomeResult.TypeQueryResult(it.typeQuery)) }
        }

    }
    private fun shareArticle(): ObservableTransformer<HomeAction.ClickAction, HomeResult.ClickResult> {
        return ObservableTransformer { action ->
            action.flatMap {
                Observable.just(HomeResult.ClickResult(it.article))
            }
        }

    }

//    private fun loadImage(): ObservableTransformer<HomeAction.LoadImageAction, HomeResult.LoadImageResult> {
//        return ObservableTransformer { action ->
//            action.flatMap {
//                repository.loadPicture("Bearer $mAccessToken",it.doctorId)
//                    .map { response -> HomeResult.LoadImageResult(response)
//                    }
//                    .cast(HomeResult.LoadImageResult::class.java)
//                    .subscribeOn(schedulerProvider.io())
//                    .observeOn(schedulerProvider.ui())
//        }
//        }
//    }
   companion object {
       var lastKey: String = ""
   }
    private fun loadSearchResults(): ObservableTransformer<HomeAction.SearchAction, HomeResult.LoadSearchResult> {
        return ObservableTransformer { action ->
            action.flatMap {
                    repository.loadList(
                        it.query,
                        HomeActivity.lat,
                        HomeActivity.lng,
                        "Bearer $mAccessToken"
                    )
                        .map { response -> HomeResult.LoadSearchResult.Success(response.doctors)
                          //  lastKey = response.lastKey
                          //  doctorId = response.doctors
                        }
                        .cast(HomeResult.LoadSearchResult::class.java)
                        .onErrorReturn { t ->
                            HomeResult.LoadSearchResult.Failure(t.localizedMessage)
                        }
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .startWith(HomeResult.LoadSearchResult.InFlight)
            }
        }

    }


}