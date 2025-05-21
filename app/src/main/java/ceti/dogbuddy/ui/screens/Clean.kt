package ceti.dogbuddy.ui.screens

import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
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
import androidx.navigation.NavController
import androidx.navigation.testing.TestNavHostController
import ceti.dogbuddy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun CleanScreen(navController: NavController, modifier: Modifier = Modifier) {
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

    val firestore = FirebaseFirestore.getInstance()
    var dogs by remember { mutableStateOf<List<Map<String, String>>>(emptyList()) }
    var selectedDogIndex by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(user.uid) {
        withContext(Dispatchers.IO) {
            firestore.collection("pet")
                .whereEqualTo("userId", user.uid)
                .get()
                .addOnSuccessListener { result ->
                    val loadedDogs = result.documents.mapNotNull { doc ->
                        val name = doc.getString("name")
                        val photoBase = doc.getString("photoBase")
                        if (name != null && photoBase != null) mapOf("name" to name, "photoBase" to photoBase) else null
                    }
                    dogs = loadedDogs
                    loading = false
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al cargar mascotas", Toast.LENGTH_SHORT).show()
                    loading = false
                }
        }
    }

    val dogName = dogs.getOrNull(selectedDogIndex)?.get("name")
    val dogImageBitmap = dogs.getOrNull(selectedDogIndex)?.get("photoBase")?.let { base64 ->
        try {
            val imageBytes = Base64.decode(base64, Base64.DEFAULT)
            val bitmapOriginal = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            val bitmapSquare = cropToSquare(bitmapOriginal)
            bitmapSquare.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(color = Color(0xff01579b)),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.paw),
                    contentDescription = "Paw",
                    modifier = Modifier
                        .size(110.dp)
                        .padding(bottom = 5.dp)
                )

                Text(
                    text = "DogBuddy",
                    color = Color.White,
                    style = TextStyle(fontSize = 32.sp),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(start = 20.dp)
                        .padding(bottom = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Higiene y limpieza",
                color = Color(0xff01579b),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 24.sp),
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally))

            Spacer(modifier = Modifier.height(20.dp))

            if (loading) {
                Text("Cargando perfil de tu mascota...", modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                if (dogName != null) {
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
                                selectedDogIndex = (selectedDogIndex + 1) % dogs.size
                            }
                        ) {
                            Text(dogName, fontSize = 22.sp, color = Color(0xFF01579B), fontWeight = FontWeight.Bold)
                            Text("Cambiar mascota", fontSize = 14.sp, color = Color(0xFF01579B))
                        }
                    }
                } else {
                    Text(
                        text = "Aún no tienes perritos registrados 🐶",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Secciones
            SectionButton("Baño y cuidado de pelaje", Color(0xFF4FC3F7), R.drawable.image28) {
                navController.navigate("bath")
            }

            Spacer(modifier = Modifier.height(16.dp))

            SectionButton("Corte de uñas", Color(0xFF4FC3F7), R.drawable.image29) {
                navController.navigate("nails")
            }

            Spacer(modifier = Modifier.height(16.dp))

            SectionButton("Higiene bucal", Color(0xFF4FC3F7), R.drawable.image30) {
                navController.navigate("teeth")
            }

        }


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
                BottomNavItem(
                    icon = R.drawable.home,
                    label = "Inicio",
                    route = "home",
                    navController = navController
                )
                BottomNavItem(
                    icon = R.drawable.camera,
                    label = "Scanear",
                    route = "scaner",
                    navController = navController
                )
                BottomNavItem(
                    icon = R.drawable.calendar,
                    label = "Calendario",
                    route = "calendar",
                    navController = navController
                )
                BottomNavItem(
                    icon = R.drawable.user,
                    label = "Perfil",
                    route = "profile",
                    navController = navController
                )
            }
        }
    }
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

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun HigieneyLimpiezaPreview() {
    val fakeNavController = TestNavHostController(LocalContext.current)
    CleanScreen(navController = fakeNavController, modifier = Modifier)
}