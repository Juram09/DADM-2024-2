package com.example.crudsqlite

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var rvEmpresas: RecyclerView
    private lateinit var etFilterName: EditText
    private lateinit var spFilterClassification: Spinner
    private lateinit var btnAddEmpresa: Button
    private lateinit var empresaAdapter: EmpresaAdapter
    private lateinit var dbHelper: EmpresaDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvEmpresas = findViewById(R.id.rvEmpresas)
        etFilterName = findViewById(R.id.etFilterName)
        spFilterClassification = findViewById(R.id.spFilterClassification)
        btnAddEmpresa = findViewById(R.id.btnAddEmpresa)

        dbHelper = EmpresaDatabaseHelper(this)

        // Configurar RecyclerView
        rvEmpresas.layoutManager = LinearLayoutManager(this)
        empresaAdapter = EmpresaAdapter(
            onEdit = { empresa -> showAddEditDialog(empresa) },
            onDelete = { empresa -> confirmDeleteEmpresa(empresa) }
        )
        rvEmpresas.adapter = empresaAdapter

        // Spinner para clasificaciones
        val classifications = arrayOf("Todas", "Consultoría", "Desarrollo a la medida", "Fábrica de software")
        spFilterClassification.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            classifications
        )

        // Configurar el listener del spinner
        spFilterClassification.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedClassification = classifications[position]
                filterEmpresas(etFilterName.text.toString(), selectedClassification)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hacer nada si no se selecciona ninguna clasificación
            }
        }

        // Listener para buscar por nombre
        etFilterName.setOnKeyListener { _, _, _ ->
            filterEmpresas(etFilterName.text.toString(), spFilterClassification.selectedItem.toString())
            false
        }

        // Botón para agregar nueva empresa
        btnAddEmpresa.setOnClickListener {
            showAddEditDialog(null)
        }

        // Cargar empresas al iniciar
        loadEmpresas()
    }

    private fun loadEmpresas() {
        val empresas = dbHelper.getAllEmpresas()
        empresaAdapter.submitList(empresas)
    }

    private fun filterEmpresas(name: String, classification: String) {
        val filteredEmpresas = dbHelper.getFilteredEmpresas(name, if (classification == "Todas") null else classification)
        empresaAdapter.submitList(filteredEmpresas)
    }

    private fun showAddEditDialog(empresa: Empresa?) {
        val dialog = AddEditEmpresaDialog(empresa) {
            loadEmpresas()
        }
        dialog.show(supportFragmentManager, "AddEditEmpresaDialog")
    }

    private fun confirmDeleteEmpresa(empresa: Empresa) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar empresa")
            .setMessage("¿Estás seguro de que deseas eliminar esta empresa?")
            .setPositiveButton("Sí") { _, _ ->
                dbHelper.deleteEmpresa(empresa.id)
                loadEmpresas()
            }
            .setNegativeButton("No", null)
            .show()
    }
}
