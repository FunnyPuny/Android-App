package com.example.funnypuny.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.funnypuny.domain.entity.HabitEntity

// Здесь сравниваем новый список и старый

class HabitListDiffCallback(
    private val oldList: List<HabitEntity>,
    private val newList: List<HabitEntity>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    //Сравнивает сами объекты, чтобы адаптер понял работает он с одним и тем же объектом или нет

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id
    }

    // Сравнивает поля объектов, чтобы узнать нужно ли перерисовывать какой-то из объектов

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }
}