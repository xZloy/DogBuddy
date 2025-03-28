package ceti.dogbuddy.ui.screens

import android.content.Intent
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
import com.google.firebase.auth.auth

//import kotlin.coroutines.jvm.internal.CompletedContinuation.context
private lateinit var auth:FirebaseAuth
@Composable
fun LoginDogBuddy(navController: NavController, modifier: Modifier = Modifier) {
    auth = Firebase.auth
    var correo by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    val context = LocalContext.current
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

        // Formulario de Login
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Inicia sesión",
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

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Usuario
            Text(
                text = "Correo:",
                color = Color(0xff01579b),
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 20.dp)
            )
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
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
                value = contraseña,
                onValueChange = { contraseña = it },
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

            // Botón de Ingreso
            Button(
                onClick = {
                    if(correo.isEmpty() || contraseña.isEmpty()){
                        Toast.makeText(context,"Favor de llenar todos los campos",Toast.LENGTH_SHORT).show()
                    }else{
                        auth.signInWithEmailAndPassword(correo,contraseña)
                            .addOnCompleteListener() { task ->
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(context, "Authentication success.", Toast.LENGTH_SHORT,).show()
                                    val user = auth.currentUser
                                    updateUI(user,navController)
                                } else {
                                    // If sign in fails, display a message to the user.
                                    //Log.w(TAG, "signInWithEmail:failure", task.exception)
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
                Text("Ingresar", fontSize = 18.sp)
            }

            // Enlaces de Olvido y Crear cuenta
            TextButton(onClick = {  }) {
                Text("¿Olvidaste tu contraseña?", color = Color(0xff01579b), fontSize = 16.sp)
            }

            TextButton(onClick = { navController.navigate("register") }) {
                Text("Crear cuenta", color = Color(0xff01579b), fontSize = 16.sp)
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
fun LoginDogBuddyPreview() {
    val fakeNavController = TestNavHostController(LocalContext.current)
    LoginDogBuddy(navController = fakeNavController, modifier = Modifier)
}

