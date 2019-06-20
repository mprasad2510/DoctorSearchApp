package org.mp.doctorsearchapp.di.mvibase

import io.reactivex.ObservableTransformer


interface MviActionProcessorHolder<I: MviAction, R: MviResult>{
    fun transformFromAction(): ObservableTransformer<I, R>
}