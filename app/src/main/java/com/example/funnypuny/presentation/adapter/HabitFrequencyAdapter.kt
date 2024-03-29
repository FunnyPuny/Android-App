package com.example.funnypuny.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.funnypuny.databinding.ItemFrequencyBinding
import com.example.funnypuny.domain.entity.HabitFrequencyEntity

class HabitFrequencyAdapter(private val frequencyList: ArrayList<HabitFrequencyEntity>): RecyclerView.Adapter<HabitFrequencyAdapter.FrequencyViewHolder>() {


    class FrequencyViewHolder(private val binding: ItemFrequencyBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(frequency: HabitFrequencyEntity) {
            binding.tvFrequencyItem.text = frequency.days.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrequencyViewHolder {
        val binding = ItemFrequencyBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FrequencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FrequencyViewHolder, position: Int) {
        val frequency = frequencyList[position]
        holder.bind(frequency)
    }

    override fun getItemCount(): Int {
        return frequencyList.size
    }
}