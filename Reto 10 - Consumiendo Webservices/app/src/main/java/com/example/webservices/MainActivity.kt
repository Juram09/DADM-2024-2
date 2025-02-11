package com.example.webservices

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var etProductName: EditText
    private lateinit var spinnerOrigin: Spinner
    private lateinit var btnFilter: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LiquorAdapter
    private var liquorList: List<Liquor> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etProductName = findViewById(R.id.etProductName)
        spinnerOrigin = findViewById(R.id.spinnerOrigin)
        btnFilter = findViewById(R.id.btnFilter)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        setupSpinners()
        btnFilter.setOnClickListener { fetchLiquorData() }
    }

    private fun setupSpinners() {
        val origins = listOf("Todos", "Nacional", "Internacional")

        val originAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, origins)
        originAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerOrigin.adapter = originAdapter
    }

    private fun fetchLiquorData() {
        val selectedOrigin = when (spinnerOrigin.selectedItem as? String) {
            "Nacional" -> "N"
            "Internacional" -> "I"
            else -> null
        }

        val productName = etProductName.text.toString().trim()

        val productQuery = if (productName.isNotEmpty()) "producto like '%$productName%'" else null

        RetrofitClient.instance.getLicores(selectedOrigin, productQuery)
            .enqueue(object : Callback<List<Liquor>> {
                override fun onResponse(call: Call<List<Liquor>>, response: Response<List<Liquor>>) {
                    if (response.isSuccessful) {
                        liquorList = response.body() ?: listOf()
                        updateRecyclerView(liquorList)
                    }
                }

                override fun onFailure(call: Call<List<Liquor>>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error al obtener datos", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun updateRecyclerView(data: List<Liquor>) {
        adapter = LiquorAdapter(data)
        recyclerView.adapter = adapter
    }
}
