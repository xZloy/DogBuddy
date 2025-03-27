package ceti.dogbuddy.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ceti.dogbuddy.R

@Composable
fun LoginDogBuddy(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        // Fondo superior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(color = Color(0xff01579b))
        ) {
            Text(
                text = "DogBuddy",
                color = Color.White,
                style = TextStyle(fontSize = 40.sp),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 20.dp)
            )
        }

        // Contenedor de login
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 140.dp), // Mueve todo hacia abajo
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título "Inicia sesión"
            Text(
                text = "Inicia sesión",
                color = Color(0xff01579b),
                style = TextStyle(fontSize = 20.sp)
            )

            // Imagen justo debajo del título
            Image(
                painter = painterResource(id = R.drawable.image4),
                contentDescription = "Logo de DogBuddy",
                modifier = Modifier
                    .size(100.dp)
                    .padding(top = 8.dp) // Espacio entre el texto y la imagen
            )

            // Espacio para empujar los campos hacia abajo
            Spacer(modifier = Modifier.height(30.dp))

            // Campo Usuario
            Text(
                text = "Usuario:",
                color = Color(0xff01579b),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Start).padding(start = 20.dp)
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(color = Color.Gray)
            )

            // Campo Contraseña
            Text(
                text = "Contraseña:",
                color = Color(0xff01579b),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Start).padding(start = 20.dp, top = 8.dp)
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(color = Color.Gray)
            )

            // Botón de Ingresar
            Box(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = Color(0xff4fc3f7))
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                    .padding(vertical = 12.dp, horizontal = 50.dp)
            ) {
                Text(
                    text = "Ingresar",
                    color = Color.White,
                    style = TextStyle(fontSize = 20.sp),
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Enlaces de Olvido y Crear cuenta
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = Color(0xff01579b),
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "Crear cuenta",
                color = Color(0xff01579b),
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun LoginDogBuddyPreview() {
    LoginDogBuddy(Modifier)
}
