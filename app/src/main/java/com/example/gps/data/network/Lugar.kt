
package com.example.gps.data.network

import com.google.gson.annotations.SerializedName

data class Lugar(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nombre")
    val title: String,

    @SerializedName("descripcion")
    val description: String,

    @SerializedName("imagen")
    val imageUrl: String,

    @SerializedName("latitud")
    val latitude: Double,

    @SerializedName("longitud")
    val longitude: Double
)
