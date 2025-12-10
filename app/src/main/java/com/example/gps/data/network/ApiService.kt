package com.example.gps.data.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("lugares")
    suspend fun getLugares(): List<Lugar>

    @GET("lugares/{id}")
    suspend fun getLugar(@Path("id") id: Int): Lugar

    @Multipart
    @POST("lugares")
    suspend fun createLugar(
        @Part("nombre") nombre: RequestBody,
        @Part("salon") salon: RequestBody,
        @Part("descripcion") descripcion: RequestBody,
        @Part("latitud") latitud: RequestBody,
        @Part("longitud") longitud: RequestBody,
        @Part imagen: MultipartBody.Part
    ): Response<Unit>

    @FormUrlEncoded
    @PUT("lugares/{id}")
    suspend fun actualizarLugar(
        @Path("id") id: Int,
        @Field("nombre") nombre: String,
        @Field("salon") salon: String,
        @Field("descripcion") descripcion: String
    ): Response<Unit>

    @DELETE("lugares/{id}")
    suspend fun deleteLugar(@Path("id") id: Int): Response<Unit>
}
