package ceti.dogbuddy.ui.screens

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.testing.TestNavHostController
import ceti.dogbuddy.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)


private lateinit var auth: FirebaseAuth
fun updateUI(user: FirebaseUser?, navController: NavController) {
    if (user != null) {
        navController.navigate("home") {
            popUpTo("login") { inclusive = true }
        }
    } else {
        //Toast.makeText(this, "Error al iniciar sesión.", Toast.LENGTH_SHORT).show()
    }
}
@Composable
fun RegisterDogBuddy(navController: NavController, modifier: Modifier = Modifier) {
    var usuario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    auth = Firebase.auth
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xffe3f2fd))
    ) {
        // Encabezado superior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(color = Color(0xff01579b)),
        ) {
            Text(
                text = "DogBuddy",
                color = Color.White,
                style = TextStyle(fontSize = 32.sp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 20.dp)
            )
        }

        // Formulario de Registro
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Registro",
                color = Color(0xff01579b),
                style = TextStyle(fontSize = 24.sp)
            )

            Image(
                painter = painterResource(id = R.drawable.image4),
                contentDescription = "Logo de DogBuddy",
                modifier = Modifier
                    .size(130.dp)
                    .padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.height(1.dp))

            // Campo Usuario
            Text(
                text = "Usuario:",
                color = Color(0xff01579b),
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 20.dp)
            )
            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White, // Fondo blanco al enfocar
                    unfocusedContainerColor = Color.White, // Fondo blanco sin enfocar
                    focusedTextColor = Color.Black, // Color del texto enfocado
                    unfocusedTextColor = Color.Black, // Color del texto sin enfocar
                    focusedIndicatorColor = Color(0xff4fc3f7), // Borde al enfocar
                    unfocusedIndicatorColor = Color(0xff01579b) // Borde sin enfocar
                )
            )

            // Campo Correo
            Text(
                "Correo:",
                color = Color(0xff01579b),
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 20.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White, // Fondo blanco al enfocar
                    unfocusedContainerColor = Color.White, // Fondo blanco sin enfocar
                    focusedTextColor = Color.Black, // Color del texto enfocado
                    unfocusedTextColor = Color.Black, // Color del texto sin enfocar
                    focusedIndicatorColor = Color(0xff4fc3f7), // Borde al enfocar
                    unfocusedIndicatorColor = Color(0xff01579b) // Borde sin enfocar
                )
            )

            // Campo Contraseña
            Text(
                "Contraseña:",
                color = Color(0xff01579b),
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 20.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(), // Ocultar texto
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White, // Fondo blanco al enfocar
                    unfocusedContainerColor = Color.White, // Fondo blanco sin enfocar
                    focusedTextColor = Color.Black, // Texto enfocado
                    unfocusedTextColor = Color.Black, // Texto sin enfocar
                    focusedIndicatorColor = Color(0xff4fc3f7), // Borde al enfocar
                    unfocusedIndicatorColor = Color(0xff01579b) // Borde sin enfocar
                )
            )

            // Botón de Registro
            Button(
                onClick = {
                    if(email.isEmpty() && password.isEmpty() && usuario.isEmpty()){
                        email = ""
                        password = ""
                        usuario = ""
                        Toast.makeText(context,"Favor de llenar todas las cajas", Toast.LENGTH_SHORT).show()
                    }else{
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener() { task ->
                                if (task.isSuccessful) {
                                    Log.d(TAG, "createUserWithEmail:success")
                                    val user = auth.currentUser

                                    // Verifica que el usuario no sea nulo antes de actualizar el perfil
                                    user?.let {
                                        val profileUpdates = UserProfileChangeRequest.Builder()
                                            .setDisplayName(usuario) // Agrega el nombre de usuario
                                            .build()

                                        it.updateProfile(profileUpdates)
                                            .addOnCompleteListener { profileTask ->
                                                if (profileTask.isSuccessful) {
                                                    Log.d(TAG, "User profile updated.")
                                                } else {
                                                    Log.w(TAG, "User profile update failed.", profileTask.exception)
                                                }
                                                updateUI(user,navController) // Actualiza la UI después de actualizar el perfil
                                            }
                                    }
                                } else {
                                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT,).show()
                                    updateUI(null,navController)
                                }
                            }


                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xff4fc3f7)),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .width(150.dp)
                    .height(48.dp)
            ) {
                Text("Registrar", fontSize = 18.sp)
            }

            // Enlace para iniciar sesión
            Text(
                text = "¿Ya tienes cuenta?",
                color = Color(0xff01579b),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
            TextButton(onClick = { navController.navigate("login") }) {
                Text("Inicia sesión aquí", color = Color(0xff01579b), fontSize = 16.sp)
            }
        }
    }


}

@Preview(widthDp = 360, heightDp = 800)
@Composable
fun RegisterDogBuddyPreview() {
    val fakeNavController = TestNavHostController(LocalContext.current)
    RegisterDogBuddy(navController = fakeNavController, modifier = Modifier)
}



