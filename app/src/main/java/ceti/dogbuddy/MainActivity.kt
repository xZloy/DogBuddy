package ceti.dogbuddy



import android.graphics.BitmapFactory
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
import androidx.navigation.NavType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ceti.dogbuddy.ui.screens.AditionalInfoScreen
import ceti.dogbuddy.ui.screens.BadTeethScreen
import ceti.dogbuddy.ui.screens.BathScreen
import ceti.dogbuddy.ui.screens.BrushTeethScreen
import ceti.dogbuddy.ui.screens.CalendarDogBuddy
import ceti.dogbuddy.ui.screens.EditPetScreen
import ceti.dogbuddy.ui.screens.CleanScreen
import ceti.dogbuddy.ui.screens.FeedingScreen
import ceti.dogbuddy.ui.screens.HomeDogBuddy
import ceti.dogbuddy.ui.screens.LoginDogBuddy
import ceti.dogbuddy.ui.screens.NailsScreen
import ceti.dogbuddy.ui.screens.ProductTeethScreen
import ceti.dogbuddy.ui.screens.questions.PetConditionScreen
import ceti.dogbuddy.ui.screens.RecoverPassDogBuddy
import ceti.dogbuddy.ui.screens.RegisterDogBuddy
import ceti.dogbuddy.ui.screens.RelaxCutScreen
import ceti.dogbuddy.ui.screens.ScannerScreen
import ceti.dogbuddy.ui.screens.ShampooScreen
import ceti.dogbuddy.ui.screens.TeethScreen
import ceti.dogbuddy.ui.screens.UserScreen
import ceti.dogbuddy.ui.screens.questions.Nextscreen
import ceti.dogbuddy.ui.screens.questions.Nextscreen2
import ceti.dogbuddy.ui.screens.questions.Nextscreen3
import ceti.dogbuddy.ui.screens.questions.RecommendedFoodScreen
import ceti.dogbuddy.ui.viewmodels.DogViewModel
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
    val view = LocalView.current
    val context = LocalContext.current
    val dogViewModel: DogViewModel = viewModel()

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
        startDestination = getStartDestination()
    }

    if (startDestination != null) {
        NavHost(navController, startDestination = startDestination!!) {
            composable("login") { LoginDogBuddy(navController) }
            composable("register") { RegisterDogBuddy(navController) }
            composable("home") {
                HomeDogBuddy(navController, viewModel = dogViewModel)
            }
            composable("calendar") {
                CalendarDogBuddy(navController, viewModel = dogViewModel)
            }
            composable("newpass") { RecoverPassDogBuddy(navController) }
            composable("scaner") { ScannerScreen(navController) }
            composable("profile") { UserScreen(navController) }
            composable("clean") {
                CleanScreen(navController, viewModel = dogViewModel)
            }
            composable("bath") {
                BathScreen(navController, viewModel = dogViewModel)
            }
            composable("shampoo") {
                ShampooScreen(navController, viewModel = dogViewModel)
            }
            composable("nails") {
                NailsScreen(navController, viewModel = dogViewModel)
            }
            composable("relaxCut") {
                RelaxCutScreen(navController, viewModel = dogViewModel)
            }
            composable("teeth") {
                TeethScreen(navController, viewModel = dogViewModel)
            }
            composable("brushTeeth") {
                BrushTeethScreen(navController, viewModel = dogViewModel)
            }
            composable("supportProducts") {
                ProductTeethScreen(navController, viewModel = dogViewModel)
            }
            composable("badTeeth") {
                BadTeethScreen(navController, viewModel = dogViewModel)
            }
            composable("alimentacion") {
                FeedingScreen(navController, viewModel = dogViewModel)
            }
            composable(
                    "pet_condition/{petName}",
                    arguments = listOf(navArgument("petName") { type = NavType.StringType })
                ) { backStackEntry ->
                    PetConditionScreen(navController, backStackEntry, viewModel = dogViewModel)
            }
            composable(
                    "next_screen/{petName}",
                    arguments = listOf(navArgument("petName") { type = NavType.StringType })
                ) { backStackEntry ->
                    Nextscreen(navController, backStackEntry, viewModel = dogViewModel)
            }
            composable(
                    "next_screen2/{petName}",
                    arguments = listOf(navArgument("petName") { type = NavType.StringType })
                ) { backStackEntry ->
                    Nextscreen2(navController, backStackEntry, viewModel = dogViewModel)
            }
                composable(
                    "next_screen3/{petName}",
                    arguments = listOf(navArgument("petName") { type = NavType.StringType })
                ) { backStackEntry ->
                    Nextscreen3(navController, backStackEntry, viewModel = dogViewModel)
                }
                composable(
                    "recommendedfood/{petName}",
                    arguments = listOf(navArgument("petName") { type = NavType.StringType })
                ) { backStackEntry ->
                    RecommendedFoodScreen(navController, backStackEntry, viewModel = dogViewModel)
                }
                composable(
                    route = "edit_pet/{mascotaId}",
                    arguments = listOf(navArgument("mascotaId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val mascotaId = backStackEntry.arguments?.getString("mascotaId") ?: ""
                    EditPetScreen(navController = navController, mascotaId = mascotaId)
                }
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

