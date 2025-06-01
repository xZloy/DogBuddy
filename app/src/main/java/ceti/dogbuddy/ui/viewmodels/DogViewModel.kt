package ceti.dogbuddy.ui.viewmodels

import android.app.Application
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import ceti.dogbuddy.ui.screens.Reminder
import com.google.firebase.firestore.FirebaseFirestore

class DogViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("DogBuddyPrefs", Context.MODE_PRIVATE)

    private var _selectedDogIndex = mutableStateOf(prefs.getInt("selected_dog_index", 0))
    val selectedDogIndex: State<Int> = _selectedDogIndex

    private var _dogs = mutableStateOf<List<Map<String, String>>>(emptyList())
    val dogs: State<List<Map<String, String>>> = _dogs

    private var _loading = mutableStateOf(true)
    val loading: State<Boolean> = _loading

    // Variables de alimentacion para OpenAI
    var selectedHealthConditions = mutableStateListOf<String>()
    var selectedDietPreference = mutableStateOf<String?>(null)
    var selectedProteins = mutableStateListOf<String>()
    var selectedBenefits = mutableStateListOf<String>()


    fun setSelectedDogIndex(index: Int) {
        _selectedDogIndex.value = index
        prefs.edit().putInt("selected_dog_index", index).apply()
    }

    /*fun loadDogs(userId: String, onError: () -> Unit) {
        _loading.value = true
        FirebaseFirestore.getInstance().collection("pet")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                _dogs.value = result.documents.mapNotNull { doc ->
                    val name = doc.getString("name")
                    val photoBase = doc.getString("photoBase")
                    val breed = doc.getString("breed")
                    if (name != null && photoBase != null && breed != null) mapOf(
                        "id" to doc.id,
                        "name" to name,
                        "photoBase" to photoBase,
                        "breed" to breed
                    ) else null
                }
                _loading.value = false
            }
            .addOnFailureListener {
                onError()
                _loading.value = false
            }
    }*/
    fun loadDogs(userId: String, onError: () -> Unit) {
        _loading.value = true
        FirebaseFirestore.getInstance().collection("pet")
            .get()
            .addOnSuccessListener { result ->
                val allDocs = result.documents
                println("🔍 UID buscado: $userId")

                allDocs.forEach { doc ->
                    val docUserId = doc.getString("userId")
                    println("📄 DOC ID: ${doc.id}")
                    println("➡️ userId en doc: $docUserId")
                    println("✅ ¿Coincide?: ${docUserId == userId}")
                }

                val filtered = allDocs.filter { doc ->
                    doc.getString("userId") == userId
                }

                println("🐶 Perros encontrados: ${filtered.size}")

                _dogs.value = filtered.mapNotNull { doc ->
                    val name = doc.getString("name")
                    val photoBase = doc.getString("photoBase")
                    val breed = doc.getString("breed")
                    if (name != null && photoBase != null && breed != null) mapOf(
                        "id" to doc.id,
                        "name" to name,
                        "photoBase" to photoBase,
                        "breed" to breed
                    ) else null
                }

                _loading.value = false
            }
            .addOnFailureListener {
                onError()
                _loading.value = false
            }
    }


    fun loadAppointments(userId: String, dogId: String?, onLoaded: (List<Reminder>) -> Unit) {
        if (dogId.isNullOrBlank()) {
            onLoaded(emptyList())
            return
        }

        FirebaseFirestore.getInstance()
            .collection("pet")
            .document(dogId)
            .collection("citas")
            .get()
            .addOnSuccessListener { result ->
                val reminders = result.documents.mapNotNull { it.toObject(Reminder::class.java) }
                onLoaded(reminders)
            }
            .addOnFailureListener {
                onLoaded(emptyList())
            }
    }
}