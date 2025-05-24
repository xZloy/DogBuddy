package ceti.dogbuddy.ui.screens.questions


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import ceti.dogbuddy.ui.viewmodels.DogViewModel


@Composable
fun RecommendedFoodScreen(
    navController: NavController,
    backStackEntry: NavBackStackEntry,
    viewModel: DogViewModel = viewModel()
) {
    val petName = backStackEntry.arguments?.getString("petName") ?: "Tu Mascota"


    val recommendedFoods = listOf(
        Food("Croquetas royal canin", "Ricas en proteínas y sin granos", R.drawable.chicken),
        Food("Croquetas Proplan sabor Salmón", "Fuente natural de Omega 3 y 6", R.drawable.chicken),
        Food("Croquetas Light", "Bajas en calorías para mascotas sedentarias", R.drawable.chicken),
        Food("Croquetas Saludables", "Con probióticos y antioxidantes", R.drawable.chicken)
    )

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
                text = "Recomendaciones para ${petName}",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF01579B)
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFF01579B), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                recommendedFoods.forEach { food ->
                    Text(
                        text = food.name,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF01579B)
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = food.description,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
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
