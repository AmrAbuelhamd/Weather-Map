package com.blogspot.soyamr.domain.enums

enum class Direction(val symbol: String) {
    North("N"), West("W"), East("E"), South("S");

    companion object {
        fun getDirection(deg: Int) = when (deg) {
            in 0..90 -> North
            in 90..180 -> East
            in 180..270 -> South
            else -> West
        }
    }
}
