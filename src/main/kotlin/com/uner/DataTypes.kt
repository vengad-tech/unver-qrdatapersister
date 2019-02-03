package com.uner

data class Settings(
    val source: String,
    val destination: String,
    val pixelSize: Int = 500,
    val redundancyFactor: Int = 3,
    val password: String,
    val writeToStdout: Boolean
)