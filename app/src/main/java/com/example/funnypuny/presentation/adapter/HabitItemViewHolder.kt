package com.example.funnypuny.presentation.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.funnypuny.R

class HabitItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val tvHabitName = view.findViewById<TextView>(R.id.tvHabitName)
}