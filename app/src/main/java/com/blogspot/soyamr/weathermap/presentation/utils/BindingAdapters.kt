package com.blogspot.soyamr.weathermap.presentation.utils

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.blogspot.soyamr.weathermap.R
import com.bumptech.glide.Glide

@BindingAdapter("image")
fun setImage(image: ImageView, url: String?) {
    if (!url.isNullOrEmpty()){
        Glide.with(image.context).load(url).centerCrop()
            .placeholder(R.drawable.ic_description_placeholder)
            .into(image)
    }
    else{
        image.setImageResource(R.drawable.ic_description_placeholder)
    }


}