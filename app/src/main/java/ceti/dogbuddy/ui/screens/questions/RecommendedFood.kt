package ceti.dogbuddy.ui.screens.questions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import ceti.dogbuddy.R
import ceti.dogbuddy.ui.openai.getDogRecommendations
import ceti.dogbuddy.ui.viewmodels.DogViewModel

@Composable
fun RecommendedFoodScreen(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    viewModel: DogViewModel = viewModel()
) {
    val petName = backStackEntry.arguments?.getString("petName") ?: "Tu Mascota"

    var isNavigatingBack by remember { mutableStateOf(false) }

    val healthConditions = viewModel.selectedHealthConditions.joinToString(", ")
    val dietPreference = viewModel.selectedDietPreference.value ?: "sin preferencia especificada"
    val proteins = viewModel.selectedProteins.joinToString(", ")
    val benefits = viewModel.selectedBenefits.joinToString(", ")

    val prompt = """
        Dame recomendaciones detalladas de alimentación para un perro llamado $petName.
        Condiciones de salud: $healthConditions.
        Preferencia dietética: $dietPreference.
        Proteínas preferidas: $proteins.
        Beneficios adicionales deseados: $benefits.
        Por favor incluye una lista concreta de marca de croquetas de alimentos, marcas recomendadas y cualquier consejo relevante.
    """.trimIndent()

    var openAiResponse by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        getDogRecommendations(prompt) { response ->
            openAiResponse = response
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
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
                        .clickable {
                            navController.navigate("home") {
                                popUpTo(0) { inclusive = true }
                            }
                        },
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Recomendaciones para $petName",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF01579B)
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFF01579B), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize()
                ) {
                    when {
                        isLoading -> {
                            Row(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(color = Color(0xFF01579B))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Generando recomendaciones...",
                                    style = TextStyle(fontSize = 16.sp, color = Color.Gray)
                                )
                            }
                        }

                        openAiResponse != null -> {
                            val sections = openAiResponse!!.split("\n\n")
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                sections.forEach { section ->
                                    if (section.isNotBlank()) {
                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                                        ) {
                                            Text(
                                                text = section.trim(),
                                                style = TextStyle(fontSize = 16.sp, color = Color.Black),
                                                modifier = Modifier.padding(12.dp),
                                                lineHeight = 22.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        else -> {
                            Text(
                                text = "No se pudo obtener recomendaciones en este momento.",
                                style = TextStyle(fontSize = 16.sp, color = Color.Red),
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (!isNavigatingBack) {
                        isNavigatingBack = true
                        navController.navigate("home") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "Regresar", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}



data class Food(
    val name: String,
    val description: String,
    val imageRes: Int
)
