package com.blogspot.soyamr.weathermap.presentation.models.enums

import com.blogspot.soyamr.weathermap.R

enum class Direction(val symbolResourceId: Int) {
    North(R.string.north), West(R.string.west), East(R.string.east), South(R.string.south);

    companion object {
        fun getDirection(deg: Int) = when (deg) {
            in 0..90 -> North
            in 90..180 -> East
            in 180..270 -> South
            else -> West
        }
    }
}
