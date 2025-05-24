package ceti.dogbuddy.ui.openai

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ceti.dogbuddy.BuildConfig
// ===== Retrofit Client =====

object OpenAIClient {
    private const val BASE_URL = "https://api.openai.com/v1/"

    private val authInterceptor = Interceptor { chain ->
        val apiKey = BuildConfig.OPENAI_API_KEY
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $apiKey")
            .build()
        chain.proceed(request)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

// ===== API Interface =====

interface OpenAIApi {
    @POST("chat/completions")
    fun getChatCompletion(@Body request: OpenAIRequest): Call<OpenAIResponse>
}

// ===== Request & Response Models =====

data class OpenAIRequest(
    val model: String = "gpt-3.5-turbo",  // o \"gpt-4\"
    val messages: List<Message>
)

data class Message(
    val role: String,  // \"user\" | \"system\" | \"assistant\"
    val content: String
)

data class OpenAIResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)

// ===== Public Function to Call API =====

fun getDogRecommendations(prompt: String, onResult: (String?) -> Unit) {
    val api = OpenAIClient.retrofit.create(OpenAIApi::class.java)

    val request = OpenAIRequest(
        messages = listOf(Message("user", prompt))
    )

    api.getChatCompletion(request).enqueue(object : Callback<OpenAIResponse> {
        override fun onResponse(call: Call<OpenAIResponse>, response: Response<OpenAIResponse>) {
            Log.d("OpenAI", "Response: ${response.raw()}, Body: ${response.body()}, ErrorBody: ${response.errorBody()?.string()}")

            if (response.isSuccessful) {
                val answer = response.body()?.choices?.firstOrNull()?.message?.content
                onResult(answer)
            } else {
                onResult(null)
            }
        }

        override fun onFailure(call: Call<OpenAIResponse>, t: Throwable) {
            Log.e("OpenAI", "Request failed", t)
            onResult(null)
        }
    })
}

