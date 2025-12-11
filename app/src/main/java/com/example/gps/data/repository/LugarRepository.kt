package com.example.gps.data.repository

import android.content.Context
import android.net.Uri
import com.example.gps.data.network.ApiService
import com.example.gps.data.network.Lugar
import com.example.gps.data.network.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class LugarRepository(private val context: Context) {

    private val apiService: ApiService = RetrofitClient.instance

    suspend fun getLugares(): List<Lugar> {
        return apiService.getLugares()
    }

    suspend fun getLugar(id: Int): Lugar {
        return apiService.getLugar(id)
    }

    suspend fun createLugar(nombre: String, salon: String, descripcion: String, imageUri: Uri, latitud: Double, longitud: Double) {
        val file = getFileFromUri(imageUri)

        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("imagen", file.name, requestFile)

        val nombrePart = nombre.toRequestBody("text/plain".toMediaTypeOrNull())
        val salonPart = salon.toRequestBody("text/plain".toMediaTypeOrNull())
        val descripcionPart = descripcion.toRequestBody("text/plain".toMediaTypeOrNull())
        val latitudPart = latitud.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val longitudPart = longitud.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        apiService.createLugar(nombrePart, salonPart, descripcionPart, latitudPart, longitudPart, imagePart)

        file.delete()
    }

    // Nueva función para llamar al servicio de actualización
    suspend fun actualizarLugar(id: Int, nombre: String, salon: String, descripcion: String) {
        apiService.actualizarLugar(id, nombre, salon, descripcion)
    }

    suspend fun deleteLugar(id: Int) {
        apiService.deleteLugar(id)
    }

    private fun getFileFromUri(uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri) ?: throw IllegalArgumentException("No se pudo abrir el stream de la URI")

        val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
        tempFile.deleteOnExit()

        val outputStream = FileOutputStream(tempFile)
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return tempFile
    }
}