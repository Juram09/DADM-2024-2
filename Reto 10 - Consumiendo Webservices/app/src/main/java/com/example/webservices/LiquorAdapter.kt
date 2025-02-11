package com.example.webservices

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LiquorAdapter(private val liquorList: List<Liquor>) :
    RecyclerView.Adapter<LiquorAdapter.LiquorViewHolder>() {

    class LiquorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val producto: TextView = itemView.findViewById(R.id.tvProducto)
        val origen: TextView = itemView.findViewById(R.id.tvOrigen)
        val gradoAlcohol: TextView = itemView.findViewById(R.id.tvGradoAlcohol)
        val registroSanitario: TextView = itemView.findViewById(R.id.tvRegistroSanitario)
        val vigenciaRegistro: TextView = itemView.findViewById(R.id.tvVigenciaRegistro)
        val resolucionRegistro: TextView = itemView.findViewById(R.id.tvResolucionRegistro)
        val productor: TextView = itemView.findViewById(R.id.tvProductor)
        val distribuidor: TextView = itemView.findViewById(R.id.tvDistribuidor)
        val nit: TextView = itemView.findViewById(R.id.tvNit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiquorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_liquor, parent, false)
        return LiquorViewHolder(view)
    }

    override fun onBindViewHolder(holder: LiquorViewHolder, position: Int) {
        val liquor = liquorList[position]
        holder.producto.text = "Producto: ${liquor.producto ?: "Desconocido"}"
        holder.origen.text = "Origen: ${liquor.origen ?: "Desconocido"}"
        holder.gradoAlcohol.text = "Grado Alcohol: ${liquor.grado_de_alcohol ?: "N/A"}"
        holder.registroSanitario.text = "Registro Sanitario: ${liquor.registro_sanitario ?: "N/A"}"
        holder.vigenciaRegistro.text = "Vigencia: ${liquor.vigencia_de_registro_sanitario ?: "N/A"}"
        holder.resolucionRegistro.text = "Resolución: ${liquor.resoluci_n_de_registro ?: "N/A"}"
        holder.productor.text = "Productor: ${liquor.productor ?: "N/A"}"
        holder.distribuidor.text = "Distribuidor: ${liquor.nombre_empresa_distribuidora ?: "N/A"}"
        holder.nit.text = "NIT: ${liquor.nit ?: "N/A"}"
    }

    override fun getItemCount(): Int = liquorList.size
}
