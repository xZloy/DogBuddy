package ceti.dogbuddy


import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ceti.dogbuddy.ui.screens.AditionalInfoScreen
import ceti.dogbuddy.ui.screens.CalendarDogBuddy
import ceti.dogbuddy.ui.screens.CleanScreen
import ceti.dogbuddy.ui.screens.HomeDogBuddy
import ceti.dogbuddy.ui.screens.LoginDogBuddy
import ceti.dogbuddy.ui.screens.RecoverPassDogBuddy
import ceti.dogbuddy.ui.screens.RegisterDogBuddy
import ceti.dogbuddy.ui.screens.ScannerScreen
import ceti.dogbuddy.ui.screens.UserScreen
import com.google.firebase.auth.FirebaseAuth

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
    val view = LocalView.current
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val window = (view.context as Activity).window
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val insetsController = ViewCompat.getWindowInsetsController(view)
        insetsController?.hide(WindowInsetsCompat.Type.systemBars())
        insetsController?.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        onDispose {
            insetsController?.show(WindowInsetsCompat.Type.systemBars())
        }
    }

    LaunchedEffect(Unit) {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {
            // 🔍 Verificar si está en Firestore
            val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
            db.collection("usuarios").document(user.uid).get()
                .addOnSuccessListener { document ->
                    startDestination = if (document.exists()) "home" else "login"
                    if (!document.exists()) auth.signOut()
                }
                .addOnFailureListener {
                    startDestination = "login"
                    auth.signOut()
                }
        } else {
            startDestination = "login"
        }
    }

    if (startDestination != null) {
        NavHost(navController, startDestination = startDestination!!) {
            composable("login") { LoginDogBuddy(navController) }
            composable("register") { RegisterDogBuddy(navController) }
            composable("home") { HomeDogBuddy(navController) }
            composable("calendar"){ CalendarDogBuddy(navController) }
            composable("newpass") { RecoverPassDogBuddy(navController) }
            composable("scaner") { ScannerScreen(navController) }
            composable("profile") { UserScreen(navController) }
            composable("info") {  AditionalInfoScreen(navController) }
            composable("clean") { CleanScreen(navController) }
        }
    }
}
