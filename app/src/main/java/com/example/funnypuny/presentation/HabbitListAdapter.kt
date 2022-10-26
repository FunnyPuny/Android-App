package com.example.funnypuny.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.funnypuny.R
import com.example.funnypuny.domain.HabbitItem

class HabbitListAdapter: RecyclerView.Adapter<HabbitListAdapter.HabbitItemViewHolder>() {
    
    var list = listOf<HabbitItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class HabbitItemViewHolder(val view: View):RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tv_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabbitItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_habbit_disabled,
            parent,
            false
        )
        return HabbitItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabbitItemViewHolder, position: Int) {
        val habbitItem = list[position]
        holder.tvName.text = habbitItem.name
        holder.view.setOnClickListener {
            true
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}