package com.blogspot.soyamr.weathermap.presentation.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

fun getLocationAsDMS(location: Location, decimalPlace: Int): String {
    var strLatitude = Location.convert(location.latitude, Location.FORMAT_SECONDS)
    strLatitude = replaceDelimiters(strLatitude, decimalPlace)
    val latCardinal = if (location.longitude >= 0) 'N' else 'S'
    strLatitude = "$strLatitude $latCardinal"
    var strLongitude = Location.convert(location.longitude, Location.FORMAT_SECONDS)
    strLongitude = replaceDelimiters(strLongitude, decimalPlace)
    val lonCardinal = if (location.longitude >= 0) 'E' else 'W'
    strLongitude = "$strLongitude $lonCardinal"
    return "$strLatitude $strLongitude"
}


fun bitMapFromVector(vectorResID: Int, context: Context): BitmapDescriptor {
    val vectorDrawable = ContextCompat.getDrawable(context, vectorResID)
    vectorDrawable!!.setBounds(
        0,
        0,
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight
    )
    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

private fun replaceDelimiters(coordinateString: String, decimalPlace: Int): String {
    var formattedCoordinates = coordinateString
    formattedCoordinates = formattedCoordinates.replaceFirst(":".toRegex(), "°")
    formattedCoordinates = formattedCoordinates.replaceFirst(":".toRegex(), "'")
    val pointIndex = formattedCoordinates.indexOf(".")
    val endIndex = pointIndex + 1 + decimalPlace
    if (endIndex < formattedCoordinates.length) {
        formattedCoordinates = formattedCoordinates.substring(0, endIndex)
    }
    formattedCoordinates += "\""
    return formattedCoordinates
}