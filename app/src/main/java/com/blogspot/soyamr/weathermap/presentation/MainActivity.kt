package com.blogspot.soyamr.weathermap.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.blogspot.soyamr.domain.usecases.GetCityWeatherByName
import com.blogspot.soyamr.weathermap.R
import com.blogspot.soyamr.weathermap.presentation.map.MapsFragment
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<MapsFragment>(R.id.fragment_container_view)
            }
        }
    }
}