package ceti.dogbuddy.ui.screens

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ceti.dogbuddy.R
import com.google.firebase.auth.FirebaseAuth
import ceti.dogbuddy.ui.viewmodels.DogViewModel


@Composable
fun GameScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: DogViewModel = viewModel()
) {
    val user = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current
    var showFullScreenImage by remember { mutableStateOf(false) }

    if (user == null) {
        Toast.makeText(context, "Sesión expirada, por favor inicia sesión", Toast.LENGTH_SHORT).show()
        navController.navigate("login") {
            popUpTo("home") { inclusive = true }
        }
        return
    }

    // Obtén los datos del ViewModel
    val dogs by viewModel.dogs
    val selectedDogIndex by viewModel.selectedDogIndex
    val loading by viewModel.loading

    // Carga las mascotas si no están cargadas
    LaunchedEffect(user.uid) {
        if (dogs.isEmpty()) {
            viewModel.loadDogs(user.uid) {
                Toast.makeText(context, "Error al cargar mascotas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Obtén la mascota seleccionada
    val selectedDog = remember(dogs, selectedDogIndex) {
        dogs.getOrNull(selectedDogIndex)
    }

    // Procesa la imagen
    val dogImageBitmap = remember(selectedDog) {
        selectedDog?.get("photoBase")?.let { base64 ->
            try {
                val imageBytes = Base64.decode(base64, Base64.DEFAULT)
                val bitmapOriginal = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                val bitmapSquare = cropToSquare(bitmapOriginal)
                bitmapSquare.asImageBitmap()
            } catch (e: Exception) {
                null
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(color = Color(0xff01579b)),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(35.dp)
                            .clickable { navController.popBackStack() },
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "DogBuddy",
                        color = Color.White,
                        style = TextStyle(fontSize = 32.sp),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Trucos y juegos",
                color = Color(0xff01579b),
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 24.sp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Sección de mascota
            if (loading) {
                Text(
                    "Cargando perfil de tu mascota...",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                selectedDog?.let { dog ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        dogImageBitmap?.let {
                            Image(
                                bitmap = it,
                                contentDescription = "Foto del perro",
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color(0xFF7DC1FD), CircleShape)
                                    .clickable { showFullScreenImage = true }
                            )
                        } ?: Image(
                            painter = painterResource(id = R.drawable.image8),
                            contentDescription = "Mascota",
                            modifier = Modifier
                                .size(70.dp)
                                .clip(RoundedCornerShape(50))
                                .border(2.dp, Color(0xFF7DC1FD), CircleShape)
                                .clickable { showFullScreenImage = true }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(
                            modifier = Modifier.clickable {
                                if (dogs.isNotEmpty()) {
                                    val newIndex = (selectedDogIndex + 1) % dogs.size
                                    viewModel.setSelectedDogIndex(newIndex)
                                }
                            }
                        ) {
                            Text(
                                dog["name"].orEmpty(),
                                fontSize = 22.sp,
                                color = Color(0xFF01579B),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Cambiar mascota",
                                fontSize = 14.sp,
                                color = Color(0xFF01579B)
                            )
                        }
                    }
                } ?: Text(
                    text = "Aún no tienes perritos registrados 🐶",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(100.dp))

            // Secciones de funcionalidad
            SectionButton("Trucos", Color(0xFFF78F4F), R.drawable.tricks) {
                navController.navigate("trucos")
            }

            Spacer(modifier = Modifier.height(100.dp))

            SectionButton("Juegos", Color(0xFFF78F4F), R.drawable.play) {
                navController.navigate("games")
            }

        }

        // Bottom Navigation
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .align(Alignment.BottomCenter)
                .background(Color(0xFF01579B))
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(R.drawable.home, "Inicio", "home", navController)
                BottomNavItem(R.drawable.camera, "Scanear", "scaner", navController)
                BottomNavItem(R.drawable.calendar, "Calendario", "calendar", navController)
                BottomNavItem(R.drawable.user, "Perfil", "profile", navController)
            }
        }
    }

    // Diálogo para imagen a pantalla completa
    if (showFullScreenImage && dogImageBitmap != null) {
        Dialog(
            onDismissRequest = { showFullScreenImage = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = dogImageBitmap,
                    contentDescription = "Foto del perro grande",
                    modifier = Modifier
                        .fillMaxSize()
                        .clipToBounds()
                        .clickable { showFullScreenImage = false },
                    contentScale = ContentScale.Fit
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .size(36.dp)
                        .background(Color.White.copy(alpha = 0.9f), shape = CircleShape)
                        .clickable { showFullScreenImage = false },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✕",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}