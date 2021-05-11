package com.blogspot.soyamr.weathermap.presentation.utils

import android.location.Location

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

private fun replaceDelimiters(str: String, decimalPlace: Int): String {
    var result = str
    result = result.replaceFirst(":".toRegex(), "°")
    result = result.replaceFirst(":".toRegex(), "'")
    val pointIndex = result.indexOf(".")
    val endIndex = pointIndex + 1 + decimalPlace
    if (endIndex < result.length) {
        result = result.substring(0, endIndex)
    }
    result += "\""
    return result
}