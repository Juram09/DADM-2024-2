package com.example.aiapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aiapp.R
import com.example.aiapp.models.Message

class ChatAdapter(private val messages: List<Message>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userMessage: TextView = view.findViewById(R.id.userMessage)
        val botMessage: TextView = view.findViewById(R.id.botMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]

        if (message.isUser) {
            holder.userMessage.text = message.text
            holder.userMessage.visibility = View.VISIBLE
            holder.botMessage.visibility = View.GONE
        } else {
            holder.botMessage.text = message.text
            holder.botMessage.visibility = View.VISIBLE
            holder.userMessage.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = messages.size
}
