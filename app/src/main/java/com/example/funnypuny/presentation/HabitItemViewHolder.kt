package com.example.funnypuny.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.funnypuny.R

class HabitItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val tvName = view.findViewById<TextView>(R.id.tv_name)
}