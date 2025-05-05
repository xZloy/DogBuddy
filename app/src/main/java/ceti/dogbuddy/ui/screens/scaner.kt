package ceti.dogbuddy.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import org.tensorflow.lite.Interpreter
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
import androidx.compose.ui.window.Dialog
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
                    .background(Color(0xFFE3F2FD))
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


@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    onImageCaptured: (Bitmap) -> Unit = {}
) {
    val context = LocalContext.current  // Obtener el contexto
    val lifecycleOwner = context as LifecycleOwner
    val imageCapture = remember { androidx.camera.core.ImageCapture.Builder().build() }

    var showDialog by remember { mutableStateOf(false) }
    var predictedLabel by remember { mutableStateOf("") }
    var confidence by remember { mutableStateOf(0f) }
    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }

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

    LaunchedEffect(Unit) {
        CameraUtils.imageCapture = imageCapture
        CameraUtils.onImageCaptured = { bitmap ->
            // Ajustar la orientación de la imagen
            val rotatedBitmap = rotateBitmap(bitmap, context)  // Pasa el contexto aquí

            capturedImage = rotatedBitmap
            val resized = Bitmap.createScaledBitmap(rotatedBitmap, 224, 224, true)
            val tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(resized)

            val imageProcessor = ImageProcessor.Builder()
                .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
                .add(NormalizeOp(0f, 255f))
                .build()

            val processedImage = imageProcessor.process(tensorImage)

            val tflite = Interpreter(loadModelFile(context, "model.tflite"))
            val labels = context.assets.open("labels.txt").bufferedReader().readLines()
            val outputBuffer = Array(1) { FloatArray(labels.size) }

            tflite.run(processedImage.buffer, outputBuffer)

            val prediction = outputBuffer[0].withIndex().maxByOrNull { it.value }?.index ?: -1
            confidence = if (prediction != -1) outputBuffer[0][prediction] * 100 else 0.0f
            predictedLabel = if (prediction != -1) labels[prediction] else "Desconocido"

            showDialog = true
        }
    }

    if (showDialog && capturedImage != null) {
        ResultDialog(
            raza = predictedLabel,
            confianza = confidence,
            image = capturedImage!!,
            onDismiss = { showDialog = false }
        )
    }
}

fun rotateBitmap(bitmap: Bitmap, context: Context): Bitmap {
    val rotationDegrees = getRotationDegrees(context, CameraSelector.DEFAULT_BACK_CAMERA)  // Usa el selector de cámara aquí
    val matrix = android.graphics.Matrix()
    matrix.postRotate(rotationDegrees.toFloat())  // Rota la imagen según la orientación
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

fun getRotationDegrees(context: Context, cameraSelector: CameraSelector): Int {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    val cameraProvider = cameraProviderFuture.get()

    val camera = cameraProvider.bindToLifecycle(
        context as LifecycleOwner,
        cameraSelector
    )

    val cameraInfo = camera.cameraInfo
    return cameraInfo.sensorRotationDegrees  // Obtén la rotación de la cámara
}


@Composable
fun ResultDialog(
    raza: String,
    confianza: Float,
    image: Bitmap,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFFDF6EC), // Fondo cálido y claro
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_paw),
                    contentDescription = null,
                    tint = Color(0xFF3D84C2),
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "¡Raza Detectada!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF01579B)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    bitmap = image.asImageBitmap(),
                    contentDescription = "Foto del perrito",
                    modifier = Modifier
                        .size(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(2.dp, Color(0xFF3D84C2), RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Raza: $raza",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF01579B)
                )
                Text(
                    text = "Confianza",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF01579B)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(100.dp)
                ) {
                    CircularProgressIndicator(
                        progress = confianza / 100f,
                        strokeWidth = 8.dp,
                        color = Color(0xFF3D84C2),
                        trackColor = Color(0xFFE0F2F1),
                        modifier = Modifier.fillMaxSize()
                    )

                    Text(
                        text = "%.2f%%".format(confianza),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D84C2)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()  // Hacer el botón más amplio
                ) {
                    Text("Aceptar", color = Color.White)
                }
            }
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