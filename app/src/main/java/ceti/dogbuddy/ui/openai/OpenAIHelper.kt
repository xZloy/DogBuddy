package ceti.dogbuddy.ui.openai

import okhttp3.OkHttpClient
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// ===== Retrofit Client =====

object OpenAIClient {
    private const val BASE_URL = "https://api.openai.com/v1/"
    private const val API_KEY = "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"  // <-- tu clave aquí

    private val authInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $API_KEY")
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

fun getDogRecommendations(dogName: String, onResult: (String?) -> Unit) {
    val api = OpenAIClient.retrofit.create(OpenAIApi::class.java)
    val prompt = "Give me detailed care tips and recommendations for a dog named $dogName."

    val request = OpenAIRequest(
        messages = listOf(Message("user", prompt))
    )

    api.getChatCompletion(request).enqueue(object : Callback<OpenAIResponse> {
        override fun onResponse(call: Call<OpenAIResponse>, response: Response<OpenAIResponse>) {
            val answer = response.body()?.choices?.firstOrNull()?.message?.content
            onResult(answer)
        }

        override fun onFailure(call: Call<OpenAIResponse>, t: Throwable) {
            onResult(null)
        }
    })
}