package com.example.gps.data.network

import com.google.gson.annotations.SerializedName

data class Lugar(
    val id: Int,
    // Usamos SerializedName para mapear el "nombre" del JSON a nuestro campo "title"
    @SerializedName("nombre") val title: String,
    @SerializedName("imagen") val imageUrl: String,
    val salon: String, // <-- NUEVO CAMPO
    val descripcion: String,
    val latitud: Double,
    val longitud: Double
)
