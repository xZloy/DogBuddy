package ceti.dogbuddy.ui.screens.questions

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ceti.dogbuddy.R
import ceti.dogbuddy.ui.screens.cropToSquare
import ceti.dogbuddy.ui.viewmodels.DogViewModel

@Composable
fun PetConditionScreen(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    viewModel: DogViewModel = viewModel()
) {
    val petName = backStackEntry.arguments?.getString("petName") ?: "Tu Mascota"

    val selectedConditions = remember { mutableStateListOf<String>() }

    // Obtén los datos del ViewModel
    val dogs by viewModel.dogs
    val selectedDogIndex by viewModel.selectedDogIndex
    val loading by viewModel.loading

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

    val conditions = listOf(
        "Estómago sensible",
        "Sensibilidad de piel y pelo",
        "Reducción de peso",
        "Sin restricciones"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
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
                    // Flecha de regresar
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
                text = "Pregunta sobre ${petName}",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF01579B)
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Imagen de la mascota
            if (loading) {
                Text(
                    "Cargando perfil de tu mascota...",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                selectedDog?.let {
                    dogImageBitmap?.let {
                        Image(
                            bitmap = it,
                            contentDescription = "Foto del perro",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color(0xFF7DC1FD), CircleShape)
                                .align(Alignment.CenterHorizontally)
                        )
                    } ?: Image(
                        painter = painterResource(id = R.drawable.image8),
                        contentDescription = "Mascota",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color(0xFF7DC1FD), CircleShape)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¿${petName} tiene alguna condición de salud que se deba tomar en cuenta?",
                style = TextStyle(
                    fontSize = 22.sp,
                    color = Color(0xFF01579B)
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 25.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Botones de opciones
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp),
                verticalArrangement = Arrangement.spacedBy(25.dp)
            ) {

                conditions.forEach { condition ->
                    val isSelected = selectedConditions.contains(condition)
                    val backgroundColor = if (isSelected)  Color(0xFF6FCF97) else Color.White
                    val contentColor = if (isSelected) Color.White else Color(0xFF01579B)

                    Button(
                        onClick = {
                            if (condition == "Sin restricciones") {
                                if (isSelected) {
                                    selectedConditions.clear()
                                } else {
                                    selectedConditions.clear()
                                    selectedConditions.add(condition)
                                }
                            } else {
                                if (selectedConditions.contains("Sin restricciones")) {
                                    selectedConditions.clear()
                                }
                                if (isSelected) {
                                    selectedConditions.remove(condition)
                                } else {
                                    selectedConditions.add(condition)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = backgroundColor,
                            contentColor = contentColor
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                painter = when (condition) {
                                    "Estómago sensible" -> painterResource(id = R.drawable.stomach)
                                    "Sensibilidad de piel y pelo" -> painterResource(id = R.drawable.skin)
                                    "Reducción de peso" -> painterResource(id = R.drawable.weight)
                                    "Sin restricciones" -> painterResource(id = R.drawable.no_restrictions)
                                    else -> painterResource(id = R.drawable.paw)
                                },
                                contentDescription = condition,
                                tint = contentColor,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = condition,
                                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    selectedDog?.get("name")?.let { dogName ->
                        navController.navigate("next_screen/$dogName")
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                enabled = selectedConditions.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedConditions.isNotEmpty()) Color(0xff4fc3f7) else Color.Gray,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Continuar", style = TextStyle(fontSize = 16.sp))
            }
        }
    }
}

