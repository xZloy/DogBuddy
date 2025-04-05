package ceti.dogbuddy.ui.screens

import android.app.TimePickerDialog
import android.app.DatePickerDialog
import android.widget.NumberPicker
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import ceti.dogbuddy.R
import java.text.SimpleDateFormat
import java.util.*

data class Reminder(
    val titulo: String,
    val fecha: String,
    val hora: String
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarDogBuddy(navController: NavController) {
    val scrollState = rememberScrollState()
    val showDatePicker = remember { mutableStateOf(false) }
    val selectedDateMillis = remember { mutableStateOf<Long?>(null) }
    val datePickerState = rememberDatePickerState()
    val showReminderDialog = remember { mutableStateOf(false) }
    val reminders = remember { mutableStateListOf<Reminder>() }
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

            Text(
                text = "Calendario de citas",
                color = Color(0xFF01579B),
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
            )

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

            Text(
                text = "Próximas citas",
                fontSize = 18.sp,
                color = Color(0xFF01579B),
                modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)
            )

            reminders.forEach {
                AppointmentCard(it.titulo, it.fecha, it.hora, R.drawable.image8)
            }
            Spacer(modifier = Modifier.height(100.dp))
        }

        // FAB flotante
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp, end = 16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF01579B))
                    .clickable { showReminderDialog.value = true },
                contentAlignment = Alignment.Center
            ) {
                Text("+", color = Color.White, fontSize = 30.sp)
            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            BottomNavigationBar(navController)
        }

        if (showReminderDialog.value) {
            ReminderDialogView(
                onDismiss = { showReminderDialog.value = false },
                onSaveReminder = { reminder ->
                    reminders.add(reminder)
                    showReminderDialog.value = false
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderDialogView(
    onDismiss: () -> Unit,
    onSaveReminder: (Reminder) -> Unit
) {
    val titleText = remember { mutableStateOf("") }
    val showTimePicker = remember { mutableStateOf(false) }

    val selectedDateMillis = remember { mutableStateOf<Long?>(null) }
    val datePickerState = rememberDatePickerState()
    val selectedHour = remember { mutableStateOf(12) }
    val selectedMinute = remember { mutableStateOf(0) }

    val formattedDate = selectedDateMillis.value?.let {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        sdf.format(Date(it))
    } ?: ""

    val formattedTime = String.format("%02d:%02d", selectedHour.value, selectedMinute.value)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x80000000))
            .clickable(onClick = onDismiss)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFE3F2FD))
                .padding(20.dp)
                .fillMaxWidth(0.85f)
                .clickable(enabled = false) {}
        ) {
            Text(
                "Agregar recordatorio",
                color = Color(0xFF01579B),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Título
            TextField(
                value = titleText.value,
                onValueChange = { titleText.value = it },
                label = { Text("Título", color = Color(0xFF01579B)) },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(4.dp, shape = RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            // DatePicker embebido con estilo personalizado
            Text("Seleccionar fecha", fontWeight = FontWeight.Medium, color = Color(0xFF01579B))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFE3F2FD),
                tonalElevation = 4.dp,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                DatePicker(
                    state = datePickerState,
                    showModeToggle = false,
                    modifier = Modifier.padding(8.dp)
                )
            }

            LaunchedEffect(datePickerState.selectedDateMillis) {
                selectedDateMillis.value = datePickerState.selectedDateMillis
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Selector de hora personalizado
            Text("Horario", fontWeight = FontWeight.Medium, color = Color(0xFF01579B))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                NumberPicker(
                    value = selectedHour.value,
                    range = 0..23,
                    onValueChange = { selectedHour.value = it },
                    label = "Hora"
                )
                Spacer(modifier = Modifier.width(16.dp))
                NumberPicker(
                    value = selectedMinute.value,
                    range = 0..59,
                    onValueChange = { selectedMinute.value = it },
                    label = "Min"
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val reminder = Reminder(
                        titulo = titleText.value,
                        fecha = formattedDate,
                        hora = formattedTime
                    )
                    onSaveReminder(reminder)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4FC3F7)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Guardar", color = Color.White)
            }
        }
    }
}


@Composable
fun NumberPicker(value: Int, range: IntRange, onValueChange: (Int) -> Unit, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 12.sp, color = Color(0xFF01579B))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                if (value > range.first) onValueChange(value - 1)
            }) {
                Text("−", fontSize = 24.sp, color = Color(0xFF01579B))
            }
            Text(String.format("%02d", value), fontSize = 20.sp, color = Color.Black)
            IconButton(onClick = {
                if (value < range.last) onValueChange(value + 1)
            }) {
                Text("+", fontSize = 24.sp, color = Color(0xFF01579B))
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color(0xFF01579B)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(R.drawable.home, "Inicio", "home", navController)
        BottomNavItem(R.drawable.camera, "Scanear", "scan", navController)
        BottomNavItem(R.drawable.calendar, "Calendario", "calendar", navController)
        BottomNavItem(R.drawable.user, "Perfil", "profile", navController)
    }
}
