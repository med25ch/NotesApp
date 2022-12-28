package com.resse.notesapp.data.network

import com.squareup.moshi.Json

data class BoredActivity(
    @Json(name = "activity") val activity : String,
    @Json(name = "type") val type : String,
    @Json(name = "participants") val participants : Int
)