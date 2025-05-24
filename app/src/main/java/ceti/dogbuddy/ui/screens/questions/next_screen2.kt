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
fun Nextscreen2(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    viewModel: DogViewModel = viewModel()
) {
    val petName = backStackEntry.arguments?.getString("petName") ?: "Tu Mascota"
    val proteinIcons = mapOf(
        "Pollo" to R.drawable.chicken, // Reemplaza con tus íconos reales
        "Salmón" to R.drawable.salmon,
        "Cordero" to R.drawable.lamb,
        "Res" to R.drawable.beef
    )

    val dogs by viewModel.dogs
    val selectedDogIndex by viewModel.selectedDogIndex
    val loading by viewModel.loading

    val selectedDog = remember(dogs, selectedDogIndex) {
        dogs.getOrNull(selectedDogIndex)
    }

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

    val proteinOptions = listOf("Pollo", "Salmón", "Cordero", "Res")
    val selectedProteins = remember { mutableStateListOf<String>() }

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
                text = "¿Qué tipo de proteína prefiere ${petName}?",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF01579B)
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 25.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Opciones de proteínas con íconos
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                proteinOptions.forEach { protein ->
                    val isSelected = selectedProteins.contains(protein)
                    val backgroundColor = if (isSelected) Color(0xFF6FCF97) else Color.White
                    val contentColor = if (isSelected) Color.White else Color(0xFF01579B)

                    Button(
                        onClick = {
                            if (isSelected) {
                                selectedProteins.remove(protein)
                            } else {
                                selectedProteins.add(protein)
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
                            proteinIcons[protein]?.let { iconRes ->
                                Icon(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = protein,
                                    tint = contentColor,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = protein,
                                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.selectedProteins.clear()
                    viewModel.selectedProteins.addAll(selectedProteins)

                    selectedDog?.get("name")?.let { dogName ->
                        navController.navigate("next_screen3/$dogName")
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                enabled = selectedProteins.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xff4fc3f7),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Continuar", style = TextStyle(fontSize = 16.sp))
            }
        }
    }
}
