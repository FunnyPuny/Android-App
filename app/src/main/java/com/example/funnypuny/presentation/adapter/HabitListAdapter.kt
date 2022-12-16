package com.example.funnypuny.presentation.adapter
 
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.funnypuny.R
import com.example.funnypuny.domain.entity.Habit
import java.lang.RuntimeException

class HabitListAdapter(): ListAdapter<Habit, HabitItemViewHolder>(HabitItemDiffCallback()) {


    var onHabitItemClickListener: ((Habit) -> Unit)? = null
    var onHabitItemLongClickListener: ((Habit)-> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitItemViewHolder {
        val layout = when (viewType) {
            VIEW_TYPE_DISABLED -> R.layout.item_habit_disabled
            VIEW_TYPE_ENABLED -> R.layout.item_habit_enabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent,false)
        val viewHolder = HabitItemViewHolder(view)

        viewHolder.view.setOnLongClickListener {
            onHabitItemLongClickListener?.invoke(getItem(viewHolder.adapterPosition))
            true
        }

        viewHolder.view.setOnClickListener {
            onHabitItemClickListener?.invoke(getItem(viewHolder.adapterPosition))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: HabitItemViewHolder, position: Int) {
        val habitItem = getItem(position)
        holder.tvName.text = habitItem.name
    }


    override fun getItemViewType(position: Int): Int {
        val item = getItem(position )
        return if (item.enabled) {
            VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }
    }

    interface OnHabitItemClickListener {

        fun onHabitItemClick (habit: Habit) {

        }
    }

    companion object {
        const val VIEW_TYPE_ENABLED = 100
        const val VIEW_TYPE_DISABLED = 101
        const val MAX_POOL_SIZE = 30
    }

}