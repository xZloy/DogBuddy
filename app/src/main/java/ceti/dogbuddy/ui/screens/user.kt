package ceti.dogbuddy.ui.screens

import android.util.Base64
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ceti.dogbuddy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.io.ByteArrayInputStream
import android.graphics.BitmapFactory
import java.io.Serializable
import android.graphics.Bitmap
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.DialogProperties


fun cropToSquare(bitmap: Bitmap): Bitmap {
    val dimension = minOf(bitmap.width, bitmap.height)
    val x = (bitmap.width - dimension) / 2
    val y = (bitmap.height - dimension) / 2
    return Bitmap.createBitmap(bitmap, x, y, dimension, dimension)
}

@Composable
fun UserScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    var nombreUsuario by remember { mutableStateOf("Cargando...") }
    var mascotas by remember { mutableStateOf(listOf<Mascota>()) }
    var loading by remember { mutableStateOf(true) }
    var editEnabledMap by remember { mutableStateOf(mutableMapOf<String, Boolean>()) }


    // Cargar nombre usuario
    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { uid ->
            db.collection("usuarios").document(uid).get()
                .addOnSuccessListener { doc ->
                    nombreUsuario = doc.getString("username") ?: "Usuario sin nombre"
                }
                .addOnFailureListener {
                    nombreUsuario = "Error al cargar"
                }
        }
    }

    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { uid ->
            db.collection("pet")
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener { result ->
                    mascotas = result.toMascotasList()
                    loading = false
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al cargar mascotas", Toast.LENGTH_SHORT).show()
                    loading = false
                }
        }
    }
    fun removeMascotaFromList(id: String) {
        mascotas = mascotas.filter { it.id != id }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE1F5FE))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp),
            verticalArrangement = Arrangement.Top
        ) {
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

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Perfil",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF01579B),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_circle_user),
                contentDescription = "Perfil",
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                text = nombreUsuario,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Mis mascotas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF01579B),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (loading) {
                Text(
                    text = "Cargando mascotas...",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color.Gray
                )
            } else {
                if (mascotas.isEmpty()) {
                    Text(
                        text = "No tienes mascotas registradas.",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color.Gray
                    )
                } else {
                    mascotas.forEach { mascota ->
                        MascotaItem(
                            id = mascota.id,
                            nombre = mascota.name,
                            imagenBase64 = mascota.photoBase64,
                            navController = navController,
                            onMascotaDeleted = { removeMascotaFromList(it) }
                        )
                    }

                }
            }
        }

        // Botón cerrar sesión
        Button(
            onClick = {
                auth.signOut()

                Toast.makeText(context, "¡Hasta pronto, $nombreUsuario!", Toast.LENGTH_SHORT).show()

                navController.navigate("login") {
                    popUpTo("profile") { inclusive = true }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 70.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
        ) {
            Text(text = "Cerrar sesión", color = Color.White)
        }

        // Bottom nav...
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
    }
}

data class Mascota(
    val id: String,
    val name: String,
    val photoBase64: String
) : Serializable


private fun QuerySnapshot.toMascotasList(): List<Mascota> {
    return this.documents.mapNotNull { doc ->
        val id = doc.id
        val name = doc.getString("name")
        val photoBase64 = doc.getString("photoBase")
        if (name != null && photoBase64 != null) {
            Mascota(id, name, photoBase64)
        } else null
    }
}


@Composable
fun MascotaItem(id: String, nombre: String, imagenBase64: String, navController: NavController, onMascotaDeleted: (String) -> Unit) {
    val imageBitmap = remember(imagenBase64) {
        decodeBase64ToBitmap(imagenBase64)?.let { cropToSquare(it) }
    }
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .background(Color(0xFFFFF176), shape = RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap.asImageBitmap(),
                    contentDescription = "Mascota",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.White, CircleShape)
                        .border(1.dp, Color(0xFF7DC1FD), CircleShape)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.dog_face),
                    contentDescription = "Mascota",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.White, CircleShape)
                        .border(1.dp, Color(0xff01579b), CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = nombre,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        Row {
            IconButton(onClick = {
                navController.navigate("edit_pet/$id")
            }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = Color.Black
                )
            }
            IconButton(onClick = {
                showDialog = true
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.Black
                )
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        db.collection("pet").document(id)
                            .delete()
                            .addOnSuccessListener {
                                Toast.makeText(context, "Mascota eliminada", Toast.LENGTH_SHORT).show()
                                onMascotaDeleted(id)
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Error al eliminar mascota", Toast.LENGTH_SHORT).show()
                            }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Sí, eliminar", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancelar", color = Color.White)
                }
            },
            title = {
                Text(
                    text = "¿Eliminar mascota?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color =Color.Red
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que deseas eliminar a $nombre? Esta acción no se puede deshacer.",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            },
            containerColor = Color.White,
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        )
    }

}


fun decodeBase64ToBitmap(base64Str: String): android.graphics.Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        null
    }
}
