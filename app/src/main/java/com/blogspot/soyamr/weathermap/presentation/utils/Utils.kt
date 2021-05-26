package com.blogspot.soyamr.weathermap.presentation.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

object Utils {
    fun bitMapFromVector(vectorResID: Int, context: Context): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResID)
        vectorDrawable?.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )

        if (vectorDrawable == null)
            return null

        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun getUrl(value: String): String = "https://openweathermap.org/img/wn/$value@2x.png"


    fun convertTOmmHg(hPa: Double) = hPa / 1.33322387415
}