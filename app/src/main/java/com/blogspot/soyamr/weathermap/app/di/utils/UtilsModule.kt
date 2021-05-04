package com.blogspot.soyamr.weathermap.app.di.utils


import com.blogspot.soyamr.weathermap.app.utils.Connectivity
import org.koin.dsl.module


val utilsModule = module {
    factory { Connectivity(get()) }
}