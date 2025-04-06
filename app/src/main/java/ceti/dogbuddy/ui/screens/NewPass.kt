package ceti.dogbuddy.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ceti.dogbuddy.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

private lateinit var auth: FirebaseAuth
@Composable
fun RecoverPswdDogBuddy(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xffe3f2fd))
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
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

        // Formulario de Login
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Recuperar contraseña",
                color = Color(0xff01579b),
                style = TextStyle(fontSize = 24.sp)
            )

            Image(
                painter = painterResource(id = R.drawable.image4),
                contentDescription = "Logo de DogBuddy",
                modifier = Modifier
                    .size(200.dp)
                    .padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Correo:",
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

            Button(
                onClick = {
                    println("press")
                    /*
                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Favor de llenar todos los campos",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener() { task ->
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(
                                        context,
                                        "Authentication success.",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                    val user = auth.currentUser
                                    updateUI(user, navController)
                                } else {
                                    // If sign in fails, display a message to the user.
                                    //Log.w(TAG, "signInWithEmail:failure", task.exception)
                                    Toast.makeText(
                                        context,
                                        "Authentication failed.",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                    updateUI(null, navController)
                                }
                            }
                    }
                */
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xff4fc3f7)),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .width(150.dp)
                    .height(48.dp)
            ) {
                Text(stringResource(R.string.accept), fontSize = 18.sp)
            }

        }
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun NewPass() {
    RecoverPswdDogBuddy(Modifier)
}