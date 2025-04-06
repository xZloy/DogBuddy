package ceti.dogbuddy.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ceti.dogbuddy.R
import androidx.compose.ui.text.font.FontWeight


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp) // Espacio para la barra de navegación
                .background(Color.White)
        ) {
            // Encabezado
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

            Spacer(modifier = Modifier.height(16.dp))

            // Título
            Text(
                text = "Escanea a tu perro",
                color = Color(0xFF01579B),
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Aquí puedes agregar la funcionalidad de escaneo o dejar el texto como está
            Text(
                text = "Aquí irá el escáner de tu perro",
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 16.dp)
            )
        }

        // Barra de navegación inferior
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
