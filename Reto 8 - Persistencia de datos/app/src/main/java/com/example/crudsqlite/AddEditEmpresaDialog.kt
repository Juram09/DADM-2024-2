package com.example.crudsqlite

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Spinner
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment

class AddEditEmpresaDialog(
    private val empresa: Empresa?,
    private val onEmpresaSaved: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_add_edit_empresa, null)

        val etName: EditText = view.findViewById(R.id.etName)
        val etUrl: EditText = view.findViewById(R.id.etUrl)
        val etPhone: EditText = view.findViewById(R.id.etPhone)
        val etEmail: EditText = view.findViewById(R.id.etEmail)
        val etProducts: EditText = view.findViewById(R.id.etProducts)
        val spClassification: Spinner = view.findViewById(R.id.spClassification)

        val classifications = arrayOf("Consultoría", "Desarrollo a la medida", "Fábrica de software")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, classifications)
        spClassification.adapter = adapter

        // Si estamos editando, llenar los campos con los datos de la empresa
        empresa?.let {
            etName.setText(it.name)
            etUrl.setText(it.url)
            etPhone.setText(it.phone)
            etEmail.setText(it.email)
            etProducts.setText(it.products)

            // Seleccionar la clasificación de la empresa
            val index = classifications.indexOf(it.classification)
            spClassification.setSelection(index)
        }

        builder.setView(view)
            .setTitle(if (empresa == null) "Agregar Empresa" else "Editar Empresa")
            .setPositiveButton("Guardar") { _, _ ->
                val name = etName.text.toString()
                val url = etUrl.text.toString()
                val phone = etPhone.text.toString()
                val email = etEmail.text.toString()
                val products = etProducts.text.toString()
                val classification = spClassification.selectedItem.toString()

                val newEmpresa = Empresa(
                    id = empresa?.id ?: 0, // Si es nuevo, id será 0
                    name = name,
                    url = url,
                    phone = phone,
                    email = email,
                    products = products,
                    classification = classification
                )

                val dbHelper = EmpresaDatabaseHelper(requireContext())
                if (empresa == null) {
                    dbHelper.addEmpresa(newEmpresa)
                } else {
                    dbHelper.updateEmpresa(newEmpresa)
                }
                onEmpresaSaved() // Notificar que se guardó la empresa
            }
            .setNegativeButton("Cancelar", null)

        return builder.create()
    }
}
