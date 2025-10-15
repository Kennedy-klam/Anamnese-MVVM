package com.kennedy.cadastrofirestore.data

data class MentalState(
    // Tempo
    val timeMonth: Boolean = false,
    val timeDay: Boolean = false,
    val timeYear: Boolean = false,
    val timeWeekDay: Boolean = false,
    val timeApproximate: Boolean = false,
    val timeScore: Int = 0,

    // Local
    val locationState: Boolean = false,
    val locationCity: Boolean = false,
    val locationNeighborhood: Boolean = false,
    val locationPlace: Boolean = false,
    val locationCountry: Boolean = false,
    val locationScore: Int = 0,

    // Palavras
    val wordCar: Boolean = false,
    val wordVase: Boolean = false,
    val wordBrick: Boolean = false,
    val wordsScore: Int = 0,

    // Cálculos
    val doesMath: Boolean? = null, // Sim/Não
    val mathScore: Int = 0
)