package ceti.dogbuddy.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun MenuPerritoDogBuddy(modifier: Modifier = Modifier) {
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
                    y = (-3).dp)
                .requiredWidth(width = 360.dp)
                .requiredHeight(height = 805.dp)
        ) {
            Text(
                text = "DogBuddy",
                color = Color.White,
                style = TextStyle(
                    fontSize = 40.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 98.96.dp,
                        y = 0.dp)
                    .requiredWidth(width = 238.dp)
                    .requiredHeight(height = 11.dp))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 0.dp,
                        y = 2.86.dp)
                    .requiredWidth(width = 360.dp)
                    .requiredHeight(height = 799.dp)
                    .background(color = Color(0xffe3f2fd)))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 0.dp,
                        y = 2.86.dp)
                    .requiredWidth(width = 360.dp)
                    .requiredHeight(height = 55.dp)
                    .background(color = Color(0xff01579b)))
            Image(
                painter = painterResource(id = R.drawable.image4),
                contentDescription = "image 4",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 0.dp,
                        y = 6.96.dp)
                    .fillMaxWidth()
                    .requiredHeight(height = 47.dp))
            Text(
                text = "¡Bienvenido a DogBuddy!",
                color = Color(0xff01579b),
                style = TextStyle(
                    fontSize = 20.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 67.dp,
                        y = 73.dp)
                    .requiredWidth(width = 250.dp)
                    .requiredHeight(height = 12.dp))
            Text(
                text = "¡Bienvenido a DogBuddy!",
                color = Color(0xff01579b),
                style = TextStyle(
                    fontSize = 20.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 67.dp,
                        y = 73.dp)
                    .requiredWidth(width = 250.dp)
                    .requiredHeight(height = 40.dp))
            Image(
                painter = painterResource(id = R.drawable.image8),
                contentDescription = "image 8",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 45.98.dp,
                        y = 137.72.dp)
                    .requiredSize(size = 54.dp))
            Text(
                text = "Firulais",
                color = Color(0xff01579b),
                style = TextStyle(
                    fontSize = 20.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 105.dp,
                        y = 150.dp)
                    .requiredWidth(width = 87.dp)
                    .requiredHeight(height = 26.dp))
            Text(
                text = "Cambiar mascota",
                color = Color(0xff01579b),
                style = TextStyle(
                    fontSize = 7.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 105.dp,
                        y = 172.dp)
                    .requiredWidth(width = 69.dp)
                    .requiredHeight(height = 10.dp))

            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 39.59.dp,
                        y = 210.63.dp)
                    .requiredWidth(width = 292.dp)
                    .requiredHeight(height = 114.dp)
                    .clip(shape = RoundedCornerShape(16.dp))
                    .background(color = Color(0xff6fcf97))
                    .shadow(elevation = 4.dp,
                        shape = RoundedCornerShape(16.dp)))
            Image(
                painter = painterResource(id = R.drawable.image5),
                contentDescription = "image 5",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 35.91.dp,
                        y = 194.49.dp)
                    .requiredSize(size = 148.dp))
            Text(
                text = "Alimentacion Saludable",
                color = Color.White,
                style = TextStyle(
                    fontSize = 20.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 184.dp,
                        y = 239.dp)
                    .requiredWidth(width = 153.dp)
                    .requiredHeight(height = 57.dp))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 38.72.dp,
                        y = 349.79.dp)
                    .requiredWidth(width = 292.dp)
                    .requiredHeight(height = 114.dp)
                    .clip(shape = RoundedCornerShape(16.dp))
                    .background(color = Color(0xff4fc3f7))
                    .shadow(elevation = 4.dp,
                        shape = RoundedCornerShape(16.dp)))
            Text(
                text = "Higiene y limpieza ",
                color = Color.White,
                style = TextStyle(
                    fontSize = 20.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 180.dp,
                        y = 374.dp)
                    .requiredWidth(width = 153.dp)
                    .requiredHeight(height = 57.dp))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 35.91.dp,
                        y = 468.02.dp)
                    .requiredSize(size = 148.dp))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 39.59.dp,
                        y = 484.17.dp)
                    .requiredWidth(width = 292.dp)
                    .requiredHeight(height = 114.dp)
                    .clip(shape = RoundedCornerShape(16.dp))
                    .background(color = Color(0xfff78f4f))
                    .shadow(elevation = 4.dp,
                        shape = RoundedCornerShape(16.dp)))
            Text(
                text = "Trucos y juegos",
                color = Color.White,
                style = TextStyle(
                    fontSize = 20.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 180.dp,
                        y = 512.dp)
                    .requiredWidth(width = 153.dp)
                    .requiredHeight(height = 57.dp))
            Text(
                text = "¿Quieres que conozcamos mas a tu perro?",
                color = Color(0xff01579b),
                style = TextStyle(
                    fontSize = 15.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 58.dp,
                        y = 631.dp)
                    .requiredWidth(width = 250.dp)
                    .requiredHeight(height = 40.dp))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 0.dp,
                        y = 748.18.dp)
                    .requiredWidth(width = 360.dp)
                    .requiredHeight(height = 55.dp)
                    .background(color = Color(0xff01579b)))
            Text(
                text = "Perfil",
                color = Color.White,
                style = TextStyle(
                    fontSize = 10.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 308.dp,
                        y = 789.dp)
                    .requiredWidth(width = 25.dp)
                    .requiredHeight(height = 14.dp))
            Text(
                text = "Scanear",
                color = Color.White,
                style = TextStyle(
                    fontSize = 10.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 109.dp,
                        y = 789.dp)
                    .requiredWidth(width = 42.dp)
                    .requiredHeight(height = 16.dp))
            Text(
                text = "Inicio",
                color = Color.White,
                style = TextStyle(
                    fontSize = 10.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 25.dp,
                        y = 790.dp)
                    .requiredWidth(width = 27.dp)
                    .requiredHeight(height = 15.dp))
            Text(
                text = "Calendario",
                color = Color.White,
                style = TextStyle(
                    fontSize = 10.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(x = 201.dp,
                        y = 790.dp)
                    .requiredWidth(width = 62.dp)
                    .requiredHeight(height = 15.dp))
        }
        Image(
            painter = painterResource(id = R.drawable.clean),
            contentDescription = "image 20",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 50.dp,
                    y = 341.dp)
                .requiredSize(size = 120.dp))
        Image(
            painter = painterResource(id = R.drawable.play),
            contentDescription = "image 21",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 53.dp,
                    y = 485.dp)
                .requiredSize(size = 108.dp))
        Image(
            painter = painterResource(id = R.drawable.home),
            contentDescription = "Home",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 16.dp,
                    y = 747.dp)
                .requiredSize(size = 45.dp))
        Image(
            painter = painterResource(id = R.drawable.camera),
            contentDescription = "Camera",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 107.dp,
                    y = 747.dp)
                .requiredSize(size = 45.dp))
        Image(
            painter = painterResource(id = R.drawable.user),
            contentDescription = "User",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 296.dp,
                    y = 747.dp)
                .requiredSize(size = 48.dp))
        Image(
            painter = painterResource(id = R.drawable.calendar),
            contentDescription = "Calendar",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 202.dp,
                    y = 747.dp)
                .requiredSize(size = 45.dp))
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun MenuPerritoDogBuddyPreview() {
    MenuPerritoDogBuddy(Modifier)
}