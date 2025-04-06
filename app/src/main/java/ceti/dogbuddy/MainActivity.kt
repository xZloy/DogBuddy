package ceti.dogbuddy


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ceti.dogbuddy.ui.screens.CalendarDogBuddy
import ceti.dogbuddy.ui.screens.HomeDogBuddy
import ceti.dogbuddy.ui.screens.LoginDogBuddy
import ceti.dogbuddy.ui.screens.RegisterDogBuddy
import ceti.dogbuddy.ui.screens.ScannerScreen
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
    val user = FirebaseAuth.getInstance().currentUser
    val startDestination = if (user != null) "home" else "login"
    NavHost(navController, startDestination = startDestination) {
        composable("login") { LoginDogBuddy(navController) }
        composable("register") { RegisterDogBuddy(navController) }
        composable("home") { HomeDogBuddy(navController) }
        composable("calendar") { CalendarDogBuddy(navController) }
        composable("scaner") { ScannerScreen(navController) }
    }
}
