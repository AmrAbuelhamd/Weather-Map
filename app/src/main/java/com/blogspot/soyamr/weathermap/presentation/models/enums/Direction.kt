package com.blogspot.soyamr.weathermap.presentation.models.enums

import com.blogspot.soyamr.weathermap.R

enum class Direction(val symbolResourceId: Int) {
    NORTH(R.string.north), WEST(R.string.west), EAST(R.string.east), SOUTH(R.string.south);

    companion object {
        fun getDirection(deg: Int) = when (deg) {
            in 0..90 -> NORTH
            in 90..180 -> EAST
            in 180..270 -> SOUTH
            else -> WEST
        }
    }
}
