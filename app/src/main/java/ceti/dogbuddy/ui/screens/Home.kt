package ceti.dogbuddy.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import ceti.dogbuddy.R

@Composable
fun HomeDogBuddy(navController: NavController) {
    val user = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current

    if (user == null) {
        navController.navigate("login") {
            popUpTo("home") { inclusive = true }
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp) // deja espacio para navbar
        ) {
            // Encabezado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF01579B))
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image4),
                    contentDescription = "Logo",
                    modifier = Modifier.height(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¡Bienvenido a DogBuddy!",
                color = Color(0xFF01579B),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image8),
                    contentDescription = "Mascota",
                    modifier = Modifier.size(54.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Firulais", fontSize = 20.sp, color = Color(0xFF01579B))
                    Text("Cambiar mascota", fontSize = 12.sp, color = Color(0xFF01579B))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            SectionButton("Alimentación Saludable", Color(0xFF6FCF97), R.drawable.image5) {
                navController.navigate("alimentacion")
            }

            Spacer(modifier = Modifier.height(16.dp))

            SectionButton("Higiene y limpieza", Color(0xFF4FC3F7), R.drawable.clean) {
                navController.navigate("higiene")
            }

            Spacer(modifier = Modifier.height(16.dp))

            SectionButton("Trucos y juegos", Color(0xFFF78F4F), R.drawable.play) {
                navController.navigate("juegos")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "¿Quieres que conozcamos más a tu perro?",
                color = Color(0xFF01579B),
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        // ⬇ Barra de navegación colocada correctamente
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
                    route = "scan",
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
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = text, fontSize = 18.sp, color = Color.White)
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
        Text(text = label, color = Color.White, fontSize = 10.sp)
    }
}
