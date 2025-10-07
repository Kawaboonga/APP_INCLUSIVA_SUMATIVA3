package com.example.ref01.ui.legacy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ref01.databinding.ItemCommentBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adapter para lista de comentarios legacy (String + fecha ficticia).
 * Para mantenerlo simple y estable con tus layouts, asumimos:
 * - tvUser: mostramos un “usuario” fijo (o el primero si luego migras a modelo real)
 * - tvContent: el texto del comentario (String)
 * - tvDate: una fecha generada (para darle feedback visual)
 */
class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.VH>() {

    private val items = mutableListOf<String>()
    private val df = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())

    fun submit(list: List<String>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class VH(val bind: ItemCommentBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val content = items[position]
        holder.bind.tvUser.text = "usuario"
        holder.bind.tvContent.text = content
        holder.bind.tvDate.text = df.format(Date())
    }
}
