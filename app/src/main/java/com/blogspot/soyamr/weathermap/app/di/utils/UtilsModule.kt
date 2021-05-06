package com.blogspot.soyamr.weathermap.app.di.utils


import com.blogspot.soyamr.domain.utils.Connectivity
import com.blogspot.soyamr.weathermap.app.utils.ConnectivityImpl
import org.koin.dsl.module


val utilsModule = module {
    factory { ConnectivityImpl(get()) as Connectivity }
}