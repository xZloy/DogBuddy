package ceti.dogbuddy.ui.screens

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.testing.TestNavHostController
import ceti.dogbuddy.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream
import android.util.Base64
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Save
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AditionalInfoScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    raza: String,
    imageBitmap: Bitmap?
) {
    var nombre by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    val context = LocalContext.current
    val radioOptions = listOf("Baja", "Media", "Alta")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    fun getRadioColor(option: String): Color {
        return when (option) {
            "Baja" -> if (selectedOption == "Baja") Color(0xFFFFEB3B) else Color.Gray //  bajo
            "Media" -> if (selectedOption == "Media") Color(0xFFFFC107) else Color.Gray // medio
            "Alta" -> if (selectedOption == "Alta") Color(0xFFFF9800) else Color.Gray // alto
            else -> Color.Gray
        }
    }

    fun cropToSquare(bitmap: Bitmap): Bitmap {
        val dimension = minOf(bitmap.width, bitmap.height)
        val x = (bitmap.width - dimension) / 2
        val y = (bitmap.height - dimension) / 2
        return Bitmap.createBitmap(bitmap, x, y, dimension, dimension)
    }

    fun savePetToFirestore(
        userId: String,
        name: String,
        age: Int,
        breed: String,
        activityLevel: String,
        weight: Float,
        photoBase: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val db: FirebaseFirestore = Firebase.firestore

        if (userId.isEmpty() || name.isEmpty() || breed.isEmpty() || photoBase.isEmpty()) {
            onError(Exception("Campos requeridos vacíos"))
            return
        }

        val petData = hashMapOf(
            "userId" to userId,
            "name" to name,
            "age" to age,
            "breed" to breed,
            "activityLevel" to activityLevel,
            "weight" to weight,
            "photoBase" to photoBase
        )

        db.collection("pet")
            .add(petData)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }


    fun encodeBitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xffe3f2fd))
    ) {
        // Encabezado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(Color(0xFF01579B))
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.paw),
                    contentDescription = "Logotipo",
                    tint = Color.White,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(start = 16.dp)
                )

                Spacer(modifier = Modifier.weight(0.7f))

                Text(
                    text = "DogBuddy",
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Raza detectada: $raza",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff01579b)
            )
            Spacer(modifier = Modifier.height(16.dp))
            val croppedBitmap = imageBitmap?.let { cropToSquare(it) }
            croppedBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Imagen detectada",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(2.dp, Color(0xff01579b), CircleShape)
                )
            }
                ?: Text(
                text = "No se pudo cargar la imagen.",
                color = Color.Red,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedIndicatorColor = Color(0xff4fc3f7),
                    unfocusedIndicatorColor = Color(0xff01579b)
                )
            )

            OutlinedTextField(
                value = edad,
                onValueChange = {
                    if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                        edad = it
                    }
                },
                label = { Text("Edad (años)") },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedIndicatorColor = Color(0xff4fc3f7),
                    unfocusedIndicatorColor = Color(0xff01579b)
                )
            )

            OutlinedTextField(
                value = peso,
                onValueChange = {
                    // Aceptar solo números
                    if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                        peso = it
                    }
                },
                label = { Text("Peso (kg)") },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedIndicatorColor = Color(0xff4fc3f7),
                    unfocusedIndicatorColor = Color(0xff01579b)
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nivel de actividad
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nivel de actividad",
                fontSize = 18.sp,
                color = Color(0xff01579b)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                radioOptions.forEach { text ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = getRadioColor(text),
                                unselectedColor = Color.Gray
                            )
                        )
                        Text(text = text, color = Color(0xff01579b))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón Guardar
        Button(
            onClick = {
                if (nombre.isEmpty() || edad.isEmpty() || peso.isEmpty()) {
                    Toast.makeText(context, "Favor de llenar todos los campos", Toast.LENGTH_SHORT).show()
                } else {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    savePetToFirestore(
                        userId = userId,
                        name = nombre,
                        age = edad.toInt(),
                        breed = raza,
                        activityLevel = selectedOption,
                        weight = peso.toFloat(),
                        photoBase = imageBitmap?.let { encodeBitmapToBase64(it) } ?: "",
                        onSuccess = {
                            Toast.makeText(context, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        },
                        onError = { exception ->
                            Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                    )

                }
            },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xff4fc3f7)),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = "Guardar",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Guardar", fontSize = 18.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { navController.popBackStack() },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Cancelar",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Cancelar",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

    }
}

