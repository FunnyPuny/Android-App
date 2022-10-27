package com.example.funnypuny.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.funnypuny.R
import com.example.funnypuny.domain.HabbitItem
import java.lang.RuntimeException

class HabbitListAdapter: RecyclerView.Adapter<HabbitListAdapter.HabbitItemViewHolder>() {
    
    var list = listOf<HabbitItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onHabbitItemClickListener: OnHabbitItemClickListener? = null

    class HabbitItemViewHolder(val view: View):RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tv_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabbitItemViewHolder {
        val layout = when (viewType) {
            VIEW_TYPE_DISABLED -> R.layout.item_habbit_disabled
            VIEW_TYPE_ENABLED -> R.layout.item_habbit_enabled
            else -> throw RuntimeException("Unknow view type: $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(
            layout,
            parent,
            false
        )
        return HabbitItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabbitItemViewHolder, position: Int) {
        val habbitItem = list[position]
        holder.tvName.text = habbitItem.name
        holder.view.setOnClickListener {
            onHabbitItemClickListener?.onHabbitItemClick(habbitItem)
        }
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

    interface OnHabbitItemClickListener {

        fun onHabbitItemClick (habbitItem: HabbitItem ) {

        }
    }

    companion object {
        const val VIEW_TYPE_ENABLED = 100
        const val VIEW_TYPE_DISABLED = 101
        const val MAX_POOL_SIZE = 30
    }

}