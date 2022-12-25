package com.example.funnypuny.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.funnypuny.domain.entity.HabitEntity

class HabitItemDiffCallback: DiffUtil.ItemCallback<HabitEntity>( ) {
    override fun areItemsTheSame(oldItem: HabitEntity, newItem: HabitEntity): Boolean {
         return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HabitEntity, newItem: HabitEntity): Boolean {
        return oldItem == newItem
    }
}