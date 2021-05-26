package com.blogspot.soyamr.weathermap.presentation.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.blogspot.soyamr.weathermap.R
import com.bumptech.glide.Glide

@BindingAdapter("image")
fun setImage(image: ImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(image.context).load(url).centerCrop()
            .placeholder(R.drawable.ic_description_placeholder)
            .into(image)
    } else {
        image.setImageResource(R.drawable.ic_description_placeholder)
    }
}

@BindingAdapter("app:windSpeed", "app:windDirection")
fun setWindSpeed(view: TextView, windSpeed: String?, windDirectionResourceId: Int?) {
    if (windDirectionResourceId != null && windDirectionResourceId != 0 && !windSpeed.isNullOrBlank()) {
        view.text = HtmlCompat.fromHtml(
            view.context.getString(
                R.string.wind_speed,
                view.context.getString(windDirectionResourceId),
                windSpeed
            ), HtmlCompat.FROM_HTML_MODE_COMPACT
        )
    }
}

@BindingAdapter("app:humidity")
fun setHumidity(view: TextView, humidity: String?) {
    if (!humidity.isNullOrBlank()) {
        view.text = view.context.getString(
            R.string.humidity_percent, humidity,
        )
    }
}

@BindingAdapter("app:pressure")
fun setPressure(view: TextView, pressure: String?) {
    if (pressure != null) {
        view.text = HtmlCompat.fromHtml(
            view.context.getString(
                R.string.pressure_unit,
                pressure,
            ), HtmlCompat.FROM_HTML_MODE_COMPACT
        )
    }
}