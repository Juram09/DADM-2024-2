package com.example.webservices

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("resource/he89-pbj8.json")
    fun getLicores(
        @Query("origen") origen: String?,
        @Query("\$where") producto: String? // Búsqueda parcial
    ): Call<List<Liquor>>
}

