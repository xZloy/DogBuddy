package ceti.dogbuddy.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.renderscript.Element
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
//import androidx.camera.core.ImageProcessor
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import org.tensorflow.lite.Interpreter
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import ceti.dogbuddy.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import java.io.File
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import androidx.compose.ui.unit.dp
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ImageProcessor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = context as LifecycleOwner

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    //val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            bitmap?.let { image ->
                CameraUtils.onImageCaptured?.invoke(image)
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (hasCameraPermission) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 60.dp)
                    .background(Color.White)
            ) {
                // Encabezado
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF01579B))
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "DogBuddy",
                        fontSize = 24.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Título
                Text(
                    text = "Escanea a tu mascota",
                    color = Color(0xFF01579B),
                    fontSize = 20.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Vista previa de la cámara
                /*CameraPreview(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(760.dp)
                        .padding(horizontal = 16.dp)
                )*/
                CameraPreview(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(760.dp)
                        .padding(horizontal = 16.dp),
                    onImageCaptured = { bitmap ->
                        val resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
                        val tensorImage = TensorImage(DataType.FLOAT32)
                        tensorImage.load(resized)

                        val tflite = Interpreter(loadModelFile(context, "model.tflite"))
                        val labels = context.assets.open("labels.txt").bufferedReader().readLines()
                        val outputBuffer = Array(1) { FloatArray(labels.size) }

                        tflite.run(tensorImage.buffer, outputBuffer)

                        val prediction = outputBuffer[0].withIndex().maxByOrNull { it.value }?.index ?: -1
                        val predictedLabel = if (prediction != -1) labels[prediction] else "Desconocido"

                        Toast.makeText(context, "Raza detectada: $predictedLabel", Toast.LENGTH_LONG).show()
                    }
                )
            }
        } else {
            // Mostrar mensaje si no tiene permiso
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Se requiere permiso de cámara para escanear a tu mascota.",
                    color = Color.Red,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp)
                )
                Button(onClick = {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }) {
                    Text("Otorgar permiso")
                }
            }
        }

        // Barra de navegación inferior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .align(Alignment.BottomCenter)
                .background(Color(0xFF01579B)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(R.drawable.home, "Inicio", "home", navController)
                BottomNavItem(R.drawable.camera, "Scanear", "scaner", navController)
                BottomNavItem(R.drawable.calendar, "Calendario", "calendar", navController)
                BottomNavItem(R.drawable.user, "Perfil", "profile", navController)
            }
        }

        // Botones para subir imagen y tomar foto
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(80.dp)
                .align(Alignment.BottomCenter)
        ) {
            // Botón para subir imagen
            IconButton(
                onClick = {
                    imagePickerLauncher.launch("image/*")
                },
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.White, shape = CircleShape)
                    .padding(16.dp)
                    .align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = "Subir Imagen",
                    tint = Color(0xFF01579B)
                )
            }

            // Botón para capturar foto (cámara)
            /*IconButton(
                onClick = { /* Lógica para capturar la foto */ },
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0x9001579B), shape = CircleShape)
                    .padding(16.dp)
                    .align(Alignment.Center) // Alineación al centro
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Capturar Foto",
                    tint = Color.White
                )
            }*/
            // Botón para capturar foto
            IconButton(
                onClick = {
                    val photoFile = File.createTempFile("photo_", ".jpg")
                    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                    CameraUtils.imageCapture?.takePicture(
                        outputOptions,
                        ContextCompat.getMainExecutor(context),
                        object : ImageCapture.OnImageSavedCallback {
                            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                                CameraUtils.onImageCaptured?.invoke(bitmap)
                            }

                            override fun onError(exception: ImageCaptureException) {
                                Log.e("ScannerScreen", "Error taking photo: ${exception.message}")
                            }
                        }
                    )
                },
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0x9001579B), shape = CircleShape)
                    .padding(16.dp)
                    .align(Alignment.Center)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Capturar Foto",
                    tint = Color.White
                )
            }

        }

    }
}
object CameraUtils {
    var imageCapture: androidx.camera.core.ImageCapture? = null
    var onImageCaptured: ((Bitmap) -> Unit)? = null
}

/*@Composable
fun CameraPreview(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = context as LifecycleOwner

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview
                    )
                } catch (e: Exception) {
                    Log.e("CameraPreview", "Error setting up camera: ${e.message}")
                }

            }, ContextCompat.getMainExecutor(ctx))

            previewView
        }
    )
}*/
@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    onImageCaptured: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = context as LifecycleOwner
    val imageCapture = remember { androidx.camera.core.ImageCapture.Builder().build() }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                } catch (e: Exception) {
                    Log.e("CameraPreview", "Error setting up camera: ${e.message}")
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        }
    )

    // Esto expone el objeto para que puedas capturar foto
    LaunchedEffect(Unit) {
        CameraUtils.imageCapture = imageCapture
        CameraUtils.onImageCaptured = { bitmap ->
            // 1. Escalar imagen al tamaño requerido por el modelo (MobileNetV2 = 224x224)
            val resized = Bitmap.createScaledBitmap(bitmap, 224, 224, true)

            // 2. Convertir a TensorImage
            val tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(resized)

            // 3. Procesar imagen con normalización explícita
            val imageProcessor = ImageProcessor.Builder()
                .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
                .add(NormalizeOp(0f, 255f)) // Muy importante para MobileNet
                .build()

            val processedImage = imageProcessor.process(tensorImage)

            // 4. Ejecutar modelo
            val tflite = Interpreter(loadModelFile(context, "model.tflite"))
            val labels = context.assets.open("labels.txt").bufferedReader().readLines()
            val outputBuffer = Array(1) { FloatArray(labels.size) } // 120 para Stanford Dogs

            tflite.run(processedImage.buffer, outputBuffer)

            // 5. Buscar el índice con mayor probabilidad
            val prediction = outputBuffer[0].withIndex().maxByOrNull { it.value }?.index ?: -1
            val confidence = if (prediction != -1) outputBuffer[0][prediction] * 100 else 0.0f
            val predictedLabel = if (prediction != -1) labels[prediction] else "Desconocido"

            // 6. Mostrar resultado
            Toast.makeText(context, "Raza: $predictedLabel (%.2f%%)".format(confidence), Toast.LENGTH_LONG).show()
        }
    }
}

fun loadModelFile(context: Context, modelName: String): MappedByteBuffer {
    val fileDescriptor = context.assets.openFd(modelName)
    val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
    val fileChannel = inputStream.channel
    val startOffset = fileDescriptor.startOffset
    val declaredLength = fileDescriptor.declaredLength
    return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
}

