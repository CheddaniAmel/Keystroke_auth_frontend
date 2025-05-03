package com.example.keystrokebiometricsapp.model

data class KeystrokeEntry(
    val char: Char,
    val pressTime: Long,
    val releaseTime: Long,
    val flightTime: Long // temps entre relâchement de la précédente touche et pression de la nouvelle
)

data class KeystrokeSequence(
    val username: String,
    val sequence: List<List<Long>> // [dwellTime, flightTime]
)
