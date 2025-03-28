package ceti.dogbuddy.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
@Composable
fun HomeDogBuddy(navController: NavController, modifier: Modifier = Modifier){
    val user = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current

    // Si el usuario no está logueado, redirige a la pantalla de inicio de sesión
    if (user == null) {
        navController.navigate("login") {
            popUpTo("home") { inclusive = true }
        }
        return
    }

    // Texto que mostrará el correo del usuario
    val correo = user.email ?: "Correo no disponible"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mostrar el correo en un TextField de solo lectura
        OutlinedTextField(
            value = correo,
            onValueChange = {},
            label = { Text("Correo Electrónico") },
            enabled = false,  // Deshabilita la edición
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedIndicatorColor = Color(0xff4fc3f7),
                unfocusedIndicatorColor = Color(0xff01579b)
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Botón de Cerrar Sesión
        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()  // Cierra la sesión
                navController.navigate("login") {  // Redirige a la pantalla de login
                    popUpTo("home") { inclusive = true }
                }
                Toast.makeText(context, "Has cerrado sesión", Toast.LENGTH_SHORT).show()
            },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xff4fc3f7)),
            modifier = Modifier
                .padding(top = 20.dp)
                .width(150.dp)
                .height(48.dp)
        ) {
            Text("Cerrar Sesión", fontSize = 18.sp)
        }
    }

}