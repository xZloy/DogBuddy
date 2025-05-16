package ceti.dogbuddy.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.testing.TestNavHostController
import ceti.dogbuddy.R

@Composable
fun CleanScreen(navController: NavController, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(color = Color(0xff01579b)),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.paw),
                    contentDescription = "Paw",
                    modifier = Modifier
                        .size(110.dp)
                        .padding(bottom = 5.dp)
                )

                Text(
                    text = "DogBuddy",
                    color = Color.White,
                    style = TextStyle(fontSize = 32.sp),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(start = 20.dp)
                        .padding(bottom = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Higiene y limpieza",
                color = Color(0xff01579b),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 24.sp),
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally))

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image8),
                    contentDescription = "Mascota",
                    modifier = Modifier
                        .size(70.dp) // Aumentamos el tamaño de la imagen
                        .clip(RoundedCornerShape(50)) // Esquinas redondeadas
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Firulais", fontSize = 22.sp, color = Color(0xFF01579B), fontWeight = FontWeight.Bold)
                    Text("Cambiar mascota", fontSize = 14.sp, color = Color(0xFF01579B))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Secciones
            SectionButton("Baño y cuidado de pelaje", Color(0xFF4FC3F7), R.drawable.image28) {
                navController.navigate("bath")
            }

            Spacer(modifier = Modifier.height(16.dp))

            SectionButton("Corte de uñas", Color(0xFF4FC3F7), R.drawable.image29) {
                navController.navigate("nails")
            }

            Spacer(modifier = Modifier.height(16.dp))

            SectionButton("Higiene bucal", Color(0xFF4FC3F7), R.drawable.image30) {
                navController.navigate("teeth")
            }

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
                BottomNavItem(
                    icon = R.drawable.home,
                    label = "Inicio",
                    route = "home",
                    navController = navController
                )
                BottomNavItem(
                    icon = R.drawable.camera,
                    label = "Scanear",
                    route = "scaner",
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

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun HigieneyLimpiezaPreview() {
    val fakeNavController = TestNavHostController(LocalContext.current)
    CleanScreen(navController = fakeNavController, modifier = Modifier)
}