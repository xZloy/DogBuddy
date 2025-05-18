package ceti.dogbuddy.ui.screens

import android.util.Base64
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ceti.dogbuddy.R
import android.graphics.BitmapFactory
import androidx.compose.runtime.LaunchedEffect

@Composable
fun HomeDogBuddy(navController: NavController) {
    val user = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current

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
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            bitmap.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF01579B))
                    .height(70.dp),
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
                text = "¡Bienvenido a DogBuddy!",
                color = Color(0xFF01579B),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

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
                                    .clip(RoundedCornerShape(50))
                            )
                        } ?: Image(
                            painter = painterResource(id = R.drawable.image8),
                            contentDescription = "Mascota",
                            modifier = Modifier
                                .size(70.dp)
                                .clip(RoundedCornerShape(50))
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

            SectionButton("Alimentación Saludable", Color(0xFF6FCF97), R.drawable.image5) {
                navController.navigate("alimentacion")
            }

            Spacer(modifier = Modifier.height(16.dp))

            SectionButton("Higiene y limpieza", Color(0xFF4FC3F7), R.drawable.clean) {
                navController.navigate("clean")
            }

            Spacer(modifier = Modifier.height(16.dp))

            SectionButton("Trucos y juegos", Color(0xFFF78F4F), R.drawable.play) {
                navController.navigate("juegos")
            }

            Spacer(modifier = Modifier.height(24.dp))

            /*Text(
                text = "¿Quieres que conozcamos más a tu perro?",
                color = Color(0xFF01579B),
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )*/
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
                BottomNavItem(R.drawable.home, "Inicio", "home", navController)
                BottomNavItem(R.drawable.camera, "Scanear", "scaner", navController)
                BottomNavItem(R.drawable.calendar, "Calendario", "calendar", navController)
                BottomNavItem(R.drawable.user, "Perfil", "profile", navController)
            }
        }
    }
}
@Composable
fun SectionButton(
    text: String,
    color: Color,
    image: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(color)
            .clickable { onClick() }
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = text,
                fontSize = 22.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: Int,
    label: String,
    route: String,
    navController: NavController
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable {
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = label,
            modifier = Modifier.size(30.dp)
        )
        Text(text = label, color = Color.White, fontSize = 12.sp)
    }
}
