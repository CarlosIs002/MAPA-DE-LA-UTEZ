package com.example.gps.ui.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

// Firma de la función corregida: ya no necesita tripId
@Composable
fun CameraScreen(navController: NavController) {
    val context = LocalContext.current
    var hasCamPermission by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        hasCamPermission = it
    }

    LaunchedEffect(Unit) {
        val permission = Manifest.permission.CAMERA
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            hasCamPermission = true
        } else {
            launcher.launch(permission)
        }
    }

    Scaffold { padding ->
        if (hasCamPermission) {
            CameraView(
                modifier = Modifier.padding(padding),
                onPhotoTaken = { photoUri ->
                    val encodedUri = Uri.encode(photoUri.toString())
                    // Navegación corregida: ya no se pasa el tripId
                    navController.navigate("edit_trip?photoUri=$encodedUri") {
                        // La ruta de la cámara ya no tiene un ID
                        popUpTo("camera") { inclusive = true }
                    }
                }
            )
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Se necesita permiso de cámara")
            }
        }
    }
}

@Composable
fun CameraView(
    modifier: Modifier = Modifier,
    onPhotoTaken: (Uri) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    this.controller = cameraController
                    cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                    cameraController.bindToLifecycle(lifecycleOwner)
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        FloatingActionButton(
            onClick = { takePhoto(context, cameraController, onPhotoTaken) },
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
        ) {
            Icon(Icons.Default.AddAPhoto, contentDescription = "Tomar foto")
        }
    }
}

private fun takePhoto(
    context: Context,
    cameraController: LifecycleCameraController,
    onPhotoTaken: (Uri) -> Unit
) {
    val file = File.createTempFile(
        "JPEG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis())}",
        ".jpg",
        context.externalCacheDir
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

    cameraController.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val savedUri = output.savedUri ?: Uri.fromFile(file)
                onPhotoTaken(savedUri)
            }

            override fun onError(exc: ImageCaptureException) {
                Log.e("CameraX", "Error al tomar la foto: ", exc)
            }
        }
    )
}
