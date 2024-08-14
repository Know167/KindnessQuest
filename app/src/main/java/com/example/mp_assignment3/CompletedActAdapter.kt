package com.example.mp_assignment3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CompletedActAdapter(private val completedActs: List<String>) :
    RecyclerView.Adapter<CompletedActAdapter.CompletedActViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedActViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_completed_act, parent, false)
        return CompletedActViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompletedActViewHolder, position: Int) {
        holder.bind(completedActs[position])
    }

    override fun getItemCount() = completedActs.size

    class CompletedActViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvAct: TextView = itemView.findViewById(R.id.tvAct)

        fun bind(act: String) {
            tvAct.text = act
        }
    }
}
