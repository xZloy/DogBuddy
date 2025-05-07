package ceti.dogbuddy


import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ceti.dogbuddy.ui.screens.AditionalInfoScreen
import ceti.dogbuddy.ui.screens.CalendarDogBuddy
import ceti.dogbuddy.ui.screens.HomeDogBuddy
import ceti.dogbuddy.ui.screens.LoginDogBuddy
import ceti.dogbuddy.ui.screens.RecoverPassDogBuddy
import ceti.dogbuddy.ui.screens.RegisterDogBuddy
import ceti.dogbuddy.ui.screens.ScannerScreen
import ceti.dogbuddy.ui.screens.UserScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

//import ceti.dogbuddy.ui.theme.DogBuddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var startDestination by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        startDestination = getStartDestination()
    }

    if (startDestination != null) {
        NavHost(navController, startDestination = startDestination!!) {
            composable("login") { LoginDogBuddy(navController) }
            composable("register") { RegisterDogBuddy(navController) }
            composable("home") { HomeDogBuddy(navController) }
            composable("calendar") { CalendarDogBuddy(navController) }
            composable("newpass") { RecoverPassDogBuddy(navController) }
            composable("scaner") { ScannerScreen(navController) }
            composable("profile") { UserScreen(navController) }
            composable(
                route = "info?raza={raza}&imagePath={imagePath}",
                arguments = listOf(
                    navArgument("raza") { type = NavType.StringType },
                    navArgument("imagePath") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val raza = backStackEntry.arguments?.getString("raza") ?: "Desconocido"
                val imagePath = backStackEntry.arguments?.getString("imagePath")
                val imageBitmap = imagePath?.let { BitmapFactory.decodeFile(it) }

                // Asegúrate de pasar navController al composable
                AditionalInfoScreen(
                    navController = navController,
                    raza = raza,
                    imageBitmap = imageBitmap
                )
            }


        }
    }
}

suspend fun getStartDestination(): String {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser ?: return "login"

    return suspendCoroutine { continuation ->
        val db = FirebaseFirestore.getInstance()
        db.collection("usuarios").document(user.uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    continuation.resume("home")
                } else {
                    auth.signOut()
                    continuation.resume("login")
                }
            }
            .addOnFailureListener {
                auth.signOut()
                continuation.resume("login")
            }
    }
}
