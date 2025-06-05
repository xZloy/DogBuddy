package ceti.dogbuddy.ui.screens
import android.os.Handler
import android.os.Looper
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
import androidx.camera.core.Preview
import androidx.camera.core.internal.utils.ImageUtil.rotateBitmap
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import ceti.dogbuddy.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = context as LifecycleOwner
    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }
    var predictedLabel by remember { mutableStateOf("") }
    var predictionConfidence by remember { mutableStateOf(0f) }
    var showDialog by remember { mutableStateOf(false) }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

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
    ) { granted -> hasCameraPermission = granted }

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

                Text(
                    text = "Escanea a tu mascota",
                    color = Color(0xFF01579B),
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                CameraPreview(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(760.dp)
                        .padding(horizontal = 16.dp),
                    navController = navController,
                    onImageCaptured = { bitmap ->
                        val imageFile = saveBitmapToFile(context, bitmap)

                        uploadImageAndGetPrediction(
                            imageFile,
                            onSuccess = { raza, confianza ->
                                capturedImage = bitmap
                                predictedLabel = limpiarNombreRaza(raza)
                                predictionConfidence = confianza
                                showDialog = true
                            },
                            onError = { error ->
                                Handler(Looper.getMainLooper()).post {
                                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                                }
                            }
                        )
                    }
                )

            }

            // ✅ Botones para cargar imagen y capturar foto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(80.dp)
                    .align(Alignment.BottomCenter)
            ) {
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
                                    Log.e("ScannerScreen", "Error al tomar la foto: ${exception.message}")
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
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Se requiere permiso de cámara.",
                    color = Color.Red,
                    fontSize = 18.sp
                )
                Button(onClick = {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }) {
                    Text("Otorgar permiso")
                }
            }
        }
        // Barra de navegación inferior (añade esto dentro del Box, al final)
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
        if (showDialog && capturedImage != null) {
            ResultDialog(
                raza = predictedLabel,
                confianza = predictionConfidence,
                image = capturedImage!!,
                onDismiss = { showDialog = false },
                onConfirm = {
                    val imageFile = saveBitmapToFile(context, capturedImage!!)
                    val route = "info?raza=$predictedLabel&imagePath=${imageFile.absolutePath}"
                    navController.navigate(route)
                    showDialog = false
                }
            )
        }

    }
}

fun rotateBitmap(bitmap: Bitmap, context: Context): Bitmap {
    val rotationDegrees = getRotationDegrees(context, CameraSelector.DEFAULT_BACK_CAMERA)
    val matrix = android.graphics.Matrix()
    matrix.postRotate(rotationDegrees.toFloat())
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}
fun getRotationDegrees(context: Context, cameraSelector: CameraSelector): Int {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    val cameraProvider = cameraProviderFuture.get()
    val camera = cameraProvider.bindToLifecycle(
        context as LifecycleOwner,
        cameraSelector
    )
    return camera.cameraInfo.sensorRotationDegrees
}

// Enviar imagen al backend ya desplegado
fun uploadImageAndGetPrediction(
    imageFile: File,
    onSuccess: (String, Float) -> Unit,
    onError: (String) -> Unit
) {
    val client = OkHttpClient()

    val mediaType = "image/jpeg".toMediaTypeOrNull()
    val requestBody = imageFile.asRequestBody(mediaType)

    val body = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("file", imageFile.name, requestBody)
        .build()

    val request = Request.Builder()
        .url("https://dogbuddy-backend.onrender.com/predict")
        .post(body)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onError("Error de red: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            if (!response.isSuccessful) {
                Handler(Looper.getMainLooper()).post {
                    onError("Error del servidor: ${response.code}")
                }
                return
            }

            val jsonString = response.body?.string()
            try {
                val json = JSONObject(jsonString ?: "")
                val raza = json.getString("raza")
                val confianza = json.getDouble("confianza").toFloat()

                Handler(Looper.getMainLooper()).post {
                    onSuccess(raza, confianza)
                }
            } catch (e: Exception) {
                Handler(Looper.getMainLooper()).post {
                    onError("Error procesando respuesta")
                }
            }
        }

    })
}
@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    navController: NavController,
    onImageCaptured: (Bitmap) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = context as LifecycleOwner
    val imageCapture = remember { ImageCapture.Builder().build() }

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
                    Log.e("CameraPreview", "Error al configurar la cámara: ${e.message}")
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        }
    )

    LaunchedEffect(Unit) {
        CameraUtils.imageCapture = imageCapture
        CameraUtils.onImageCaptured = onImageCaptured
    }
}


fun limpiarNombreRaza(nombreCrudo: String): String {
    return nombreCrudo.substringAfter("-")
        .replace("_", " ")
        .replaceFirstChar { it.uppercase() }
}
@Composable
fun ResultDialog(
    raza: String,
    confianza: Float,
    image: Bitmap,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    var buttonEnabled by remember { mutableStateOf(true) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFFDF6EC),
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
                    color = Color.Black
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

                Text(
                    text = "¿Deseas guardar este perrito?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF01579B)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            if (buttonEnabled) {
                                buttonEnabled = false
                                onConfirm()
                            }
                        },
                        enabled = buttonEnabled,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D84C2)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    ) {
                        Text("Sí", color = Color.White)
                    }
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB0BEC5)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).padding(start = 8.dp)
                    ) {
                        Text("No", color = Color.White)
                    }
                }
            }
        }
    }
}
object CameraUtils {
    var imageCapture: ImageCapture? = null
    var onImageCaptured: ((Bitmap) -> Unit)? = null
}

fun saveBitmapToFile(context: Context, bitmap: Bitmap): File {
    val fileName = "captured_image_${System.currentTimeMillis()}.jpg"
    val file = File(context.cacheDir, fileName)
    try {
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return file
}
