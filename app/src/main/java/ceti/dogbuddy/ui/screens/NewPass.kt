package ceti.dogbuddy.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
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
fun RecoverPswdDogBuddy(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredWidth(width = 360.dp)
            .requiredHeight(height = 800.dp)
            .background(color = Color.White)
    ) {
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 0.dp,
                    y = (-7).dp)
                .requiredWidth(width = 360.dp)
                .requiredHeight(height = 806.dp)
        ) {
            Text(
                text = "DogBuddy",
                color = Color.White,
                style = TextStyle(
                    fontSize = 40.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 81.dp,
                        y = 0.dp)
                    .requiredWidth(width = 237.dp)
                    .requiredHeight(height = 11.dp))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 0.dp,
                        y = 7.dp)
                    .requiredWidth(width = 360.dp)
                    .requiredHeight(height = 799.dp)
                    .background(color = Color(0xffe3f2fd)))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 0.dp,
                        y = 7.dp)
                    .requiredWidth(width = 360.dp)
                    .requiredHeight(height = 55.dp)
                    .background(color = Color(0xff01579b)))
            Image(
                painter = painterResource(id = R.drawable.image4),
                contentDescription = "image 4",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 0.dp,
                        y = 11.1.dp)
                    .fillMaxWidth()
                    .requiredHeight(height = 47.dp))
            Text(
                text = "Recuperar contraseña",
                color = Color(0xff01579b),
                style = TextStyle(
                    fontSize = 20.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 62.dp,
                        y = 189.dp)
                    .requiredWidth(width = 225.dp)
                    .requiredHeight(height = 33.dp))
        }
        Image(
            painter = painterResource(id = R.drawable.image4),
            contentDescription = "image 4",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 0.dp,
                    y = 208.dp)
                .fillMaxWidth()
                .requiredHeight(height = 177.dp))
        Text(
            text = "Correo:",
            color = Color(0xff01579b),
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 30.dp,
                    y = 385.dp)
                .requiredWidth(width = 279.dp)
                .requiredHeight(height = 100.dp))
        Image(
            painter = painterResource(id = R.drawable.rectangle12),
            contentDescription = "Rectangle 12",
            modifier = Modifier
                .fillMaxSize()
                .shadow(elevation = 4.dp))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 70.dp,
                    y = 527.dp)
                .requiredWidth(width = 210.dp)
                .requiredHeight(height = 49.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .background(color = Color(0xff4fc3f7))
                .shadow(elevation = 4.dp,
                    shape = RoundedCornerShape(16.dp)))
        Text(
            text = "Aceptar",
            color = Color.White,
            style = TextStyle(
                fontSize = 20.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 135.dp,
                    y = 537.dp))
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun NewPass() {
    RecoverPswdDogBuddy(Modifier)
}