package ceti.dogbuddy.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import ceti.dogbuddy.R
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarDogBuddy(navController: NavController) {
    val scrollState = rememberScrollState()
    val showDatePicker = remember { mutableStateOf(false) }
    val selectedDateMillis = remember { mutableStateOf<Long?>(null) }
    val datePickerState = rememberDatePickerState()

    if (showDatePicker.value) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDateMillis.value = datePickerState.selectedDateMillis
                    showDatePicker.value = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker.value = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)
                .verticalScroll(scrollState)
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
                text = "Calendario de citas",
                color = Color(0xFF01579B),
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
            )

            // Perfil de mascota
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 32.dp, bottom = 16.dp)
            ) {
                AsyncImage(
                    model = "https://storage.googleapis.com/tagjs-prod.appspot.com/v1/SrY7B7riWP/othxnuip.png",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Firulais", fontSize = 20.sp, color = Color(0xFF01579B))
                    Text("Cambiar mascota", fontSize = 10.sp, color = Color(0xFF01579B))
                }
            }

            // Calendario real con DatePicker
            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.White)
                    .shadow(elevation = 4.dp)
                    .clickable { showDatePicker.value = true }
                    .padding(16.dp)
            ) {
                val formattedDate = selectedDateMillis.value?.let {
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    sdf.format(Date(it))
                } ?: "Selecciona una fecha"

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = formattedDate,
                        fontSize = 18.sp,
                        color = Color(0xFF01579B),
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Toca para abrir el calendario",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Próximas citas
            Text(
                text = "Próximas citas",
                fontSize = 18.sp,
                color = Color(0xFF01579B),
                modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)
            )

            AppointmentCard("Consulta", "14 de Marzo", "10:30 - 11:30", R.drawable.image8)
            AppointmentCard("Consulta", "14 de Marzo", "13:00 - 14:30", R.drawable.image8)

            Spacer(modifier = Modifier.height(100.dp))
        }

        // Floating action button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF01579B)),
                contentAlignment = Alignment.Center
            ) {
                Text("+", color = Color.White, fontSize = 30.sp)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .align(Alignment.BottomCenter) // ✅ ahora sí es válido porque estamos dentro de un Box
                .background(Color(0xFF01579B)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(R.drawable.home, "Inicio", "home", navController)
                BottomNavItem(R.drawable.camera, "Scanear", "scan", navController)
                BottomNavItem(R.drawable.calendar, "Calendario", "calendar", navController)
                BottomNavItem(R.drawable.user, "Perfil", "profile", navController)
            }
        }

    }
}

@Composable
fun AppointmentCard(titulo: String, fecha: String, hora: String, icon: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFFFF59D))
            .padding(12.dp)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(20.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(titulo, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("$fecha  $hora", fontSize = 14.sp)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            //.align(Alignment.BottomCenter)
            .background(Color(0xFF01579B)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(R.drawable.home, "Inicio", "home", navController)
            BottomNavItem(R.drawable.camera, "Scanear", "scan", navController)
            BottomNavItem(R.drawable.calendar, "Calendario", "calendar", navController)
            BottomNavItem(R.drawable.user, "Perfil", "profile", navController)
        }
    }
}


