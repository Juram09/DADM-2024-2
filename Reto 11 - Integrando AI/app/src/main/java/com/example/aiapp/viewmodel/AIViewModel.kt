package com.example.aiapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

class AIViewModel : ViewModel() {

    private val apiKey = "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" // Reemplaza con tu clave API
    private val apiUrl = "https://openrouter.ai/api/v1/chat/completions"

    val messages = mutableStateListOf<Pair<String, Boolean>>() // Lista de mensajes (Texto, EsUsuario)
    val isLoading = mutableStateOf(false)

    fun sendMessage(prompt: String) {
        if (prompt.isBlank()) return

        messages.add(Pair(prompt, true)) // Agregar mensaje del usuario
        isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL(apiUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Authorization", "Bearer $apiKey")
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                // Construcción correcta de "messages"
                val messagesArray = JSONArray()
                messages.forEach { message ->
                    val messageObject = JSONObject().apply {
                        put("role", if (message.second) "user" else "assistant")
                        put("content", message.first)
                    }
                    messagesArray.put(messageObject)
                }

                val json = JSONObject().apply {
                    put("model", "google/gemini-2.0-flash-lite-preview-02-05:free")
                    put("messages", messagesArray)
                }

                // Enviar la solicitud
                OutputStreamWriter(connection.outputStream).use { writer ->
                    writer.write(json.toString())
                    writer.flush()
                }

                val responseCode = connection.responseCode
                Log.d("API_DEBUG", "Código de respuesta: $responseCode")

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(response)

                    if (!jsonResponse.has("choices")) {
                        messages.add(Pair("Error en la respuesta: ${jsonResponse.optString("message", "Respuesta inesperada")}", false))
                    } else {
                        val aiResponse = jsonResponse.getJSONArray("choices")
                            .getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content")

                        withContext(Dispatchers.Main) {
                            messages.add(Pair(aiResponse, false))
                        }
                    }
                } else {
                    val errorResponse = connection.errorStream?.bufferedReader()?.use { it.readText() }
                    Log.e("API_DEBUG", "Error en la respuesta: $errorResponse")
                    messages.add(Pair("Error: $errorResponse", false))
                }

            } catch (e: Exception) {
                Log.e("API_DEBUG", "Error en la solicitud", e)
                withContext(Dispatchers.Main) {
                    messages.add(Pair("Error de conexión: ${e.message}", false))
                }
            } finally {
                isLoading.value = false
            }
        }
    }
}
