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
import androidx.compose.material3.Icon
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
        Por favor incluye ejemplos concretos de alimentos, marcas recomendadas y cualquier consejo relevante.
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
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

            Spacer(modifier = Modifier.height(24.dp))

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

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // toma el espacio restante verticalmente
                    .padding(horizontal = 16.dp)
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFF01579B), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    when {
                        isLoading -> {
                            Text(
                                text = "Generando recomendaciones...",
                                style = TextStyle(fontSize = 16.sp, color = Color.Gray),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                        openAiResponse != null -> {
                            Text(
                                text = openAiResponse ?: "No se pudo obtener respuesta.",
                                style = TextStyle(fontSize = 16.sp, color = Color.Black),
                                lineHeight = 22.sp
                            )
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

        }
        }
    }


data class Food(
    val name: String,
    val description: String,
    val imageRes: Int
)
