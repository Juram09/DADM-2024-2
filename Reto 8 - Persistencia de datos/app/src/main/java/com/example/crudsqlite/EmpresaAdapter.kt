package com.example.crudsqlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EmpresaAdapter(
    private val onEdit: (Empresa) -> Unit,
    private val onDelete: (Empresa) -> Unit
) : RecyclerView.Adapter<EmpresaAdapter.EmpresaViewHolder>() {

    private var empresas: List<Empresa> = emptyList()

    fun submitList(empresas: List<Empresa>) {
        this.empresas = empresas
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmpresaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_empresa, parent, false)
        return EmpresaViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmpresaViewHolder, position: Int) {
        val empresa = empresas[position]
        holder.bind(empresa)
    }

    override fun getItemCount(): Int = empresas.size

    inner class EmpresaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvClassification: TextView = itemView.findViewById(R.id.tvClassification)
        private val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        private val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        fun bind(empresa: Empresa) {
            tvName.text = empresa.name
            tvClassification.text = empresa.classification

            btnEdit.setOnClickListener { onEdit(empresa) }
            btnDelete.setOnClickListener { onDelete(empresa) }
        }
    }
}
