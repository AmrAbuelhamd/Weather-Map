package com.blogspot.soyamr.weathermap.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.soyamr.domain.usecases.GetCityWeatherByName
import com.blogspot.soyamr.weathermap.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    val useCase: GetCityWeatherByName by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GlobalScope.launch {
            println(useCase("Tomsk"))
        }
    }
}