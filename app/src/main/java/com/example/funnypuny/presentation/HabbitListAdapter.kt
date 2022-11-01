package com.example.funnypuny.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.funnypuny.R
import com.example.funnypuny.domain.HabitItem
import java.lang.RuntimeException

class HabitListAdapter: RecyclerView.Adapter<HabitListAdapter.HabitItemViewHolder>() {
    
    var list = listOf<HabitItem>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            val callback = HabitListDiffCallback(list,value)
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this )
            field = value
        }

    var onHabitItemClickListener: ((HabitItem) -> Unit)? = null
    var onHabitItemLongClickListener: ((HabitItem)-> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitItemViewHolder {
        val layout = when (viewType) {
            VIEW_TYPE_DISABLED -> R.layout.item_habit_disabled
            VIEW_TYPE_ENABLED -> R.layout.item_habit_enabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent,false)
        return HabitItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabitItemViewHolder, position: Int) {
        val habitItem = list[position]
        holder.view.setOnClickListener {
            onHabitItemClickListener?.invoke(habitItem)
            true
        }
        holder.view.setOnLongClickListener {
            onHabitItemLongClickListener?.invoke(habitItem)
            true
        }
        holder.tvName.text = habitItem.name
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = list[position]
        return if (item.enabled) {
            VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }
    }

    class HabitItemViewHolder(val view: View):RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tv_name)
    }

    interface OnHabitItemClickListener {

        fun onHabitItemClick (habitItem: HabitItem ) {

        }
    }

    companion object {
        const val VIEW_TYPE_ENABLED = 100
        const val VIEW_TYPE_DISABLED = 101
        const val MAX_POOL_SIZE = 30
    }

}