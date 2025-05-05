package ceti.dogbuddy.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.testing.TestNavHostController
import ceti.dogbuddy.R

@Composable
fun AditionalInfoScreen(navController: NavController, modifier: Modifier = Modifier) {
    var edad = ""
    var peso = ""
    val context = LocalContext.current
    val radioOptions = listOf("Baja", "Media", "Alta")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xffe3f2fd))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
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

        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 107.dp,
                    y = 141.dp)
                .requiredWidth(width = 147.dp)
                .requiredHeight(height = 54.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.image8),
                contentDescription = "image 8",
                modifier = Modifier
                    .requiredSize(size = 54.dp)
            )
            Text(
                text = "Firulais",
                color = Color(0xff01579b),
                style = TextStyle(
                    fontSize = 20.sp
                ),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 60.dp,
                        y = 14.dp
                    )
                    .requiredWidth(width = 87.dp)
                    .requiredHeight(height = 26.dp)
            )
        }

        Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 200.dp)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {

                OutlinedTextField(
                    value = edad,
                    onValueChange = { edad = it },
                    label = { Text("Edad") },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .requiredWidth(width = 250.dp)
                        .requiredHeight(height = 60.dp),
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

                OutlinedTextField(
                    value = peso,
                    onValueChange = { peso = it },
                    label = { Text("Peso") },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .requiredWidth(width = 250.dp)
                        .requiredHeight(height = 60.dp),
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

        }

        Text(
            text = "Nivel de actividad",
            color = Color(0xff01579b),
            style = TextStyle(
                fontSize = 20.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 90.dp,
                    y = 405.dp))

        Row(modifier.selectableGroup()
            .fillMaxWidth()
            .offset(y = 435.dp),
            Arrangement.SpaceEvenly) {
            radioOptions.forEach { text ->
                Column(
                    Modifier
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = text,
                        style = TextStyle(
                            fontSize = 18.sp),
                        color = Color(0xff01579b),
                        modifier = Modifier.padding(start = 16.dp)
                    )

                    RadioButton(
                        selected = (text == selectedOption),
                        onClick = null,
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(8.dp))
                            .background(color = Color(0xffffce4a))
                        //#0xffffeab1  0xffffce4a
                    )
                }
            }
        }

        Button(
            onClick = {
                if(edad.isEmpty() && peso.isEmpty()){
                    Toast.makeText(context,"Favor de llenar todos los campos", Toast.LENGTH_SHORT).show()
                }else {
                    //TODO creacion de perros
                }

            },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xff4fc3f7)),
            modifier = Modifier
                .padding(top = 20.dp)
                .width(150.dp)
                .height(48.dp)
                .offset(y = 200.dp)
                .align(Alignment.Center)
        ) {
            Text(stringResource(R.string.save), fontSize = 18.sp)
        }

    }
}

@Preview(widthDp = 360, heightDp = 806)
@Composable
private fun HomePreview() {
    val fakeNavController = TestNavHostController(LocalContext.current)
    AditionalInfoScreen(navController = fakeNavController, modifier = Modifier)
}