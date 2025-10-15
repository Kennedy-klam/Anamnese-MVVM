package com.kennedy.cadastrofirestore.data

data class HealthHistory(
    val vision: String? = null,
    val hearing: String? = null,

    val urinaryIncontinence: Boolean? = null,
    val urinaryIncontinenceDate: String? = null,

    val fecalIncontinence: Boolean? = null,
    val fecalIncontinenceDate: String? = null,

    val sleep: String? = null, // "Normal" ou "Distúrbios"
    val sleepDisturbanceDetails: String? = null,

    val usesOrthosis: Boolean? = null,
    val orthosisDetails: String? = null,

    val usesProsthesis: Boolean? = null,
    val prosthesisDetails: String? = null,

    val hadFallLast12Months: Boolean? = null,
    val fallCount: String? = null,

    val isSmoker: Boolean? = null,
    val smokingStoppedTime: String? = null,

    val isAlcoholic: Boolean? = null,
    val alcoholStoppedTime: String? = null,

    val mentalState: MentalState = MentalState()
)