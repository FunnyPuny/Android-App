package com.example.funnypuny.presentation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.funnypuny.R
import com.example.funnypuny.databinding.ItemDayBinding
import java.text.SimpleDateFormat
import java.util.*


class HorizontalCalendarAdapter(
    private val dates: List<HorizontalCalendarItem>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<HorizontalCalendarAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val binding = ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dates.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dates[position]
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemDayBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.calendarLinearLayout.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
        fun bind(item: HorizontalCalendarItem) {

            binding.tvDayOfTheWeek.text = item.dayOfTheWeek
            binding.tvDayOfTheMonth.text = item.dayOfTheMonth.toString()

            with(binding.tvDayOfTheMonth) {
                if (item.isSelected) {
                    setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                    background =
                        ContextCompat.getDrawable(itemView.context, R.drawable.ic_ellipse_enable)
                } else {
                    setTextColor(ContextCompat.getColor(itemView.context, R.color.grey_dark))
                    background =
                        ContextCompat.getDrawable(itemView.context, R.drawable.ic_ellipse_disable)
                }
            }

            binding.tvDayOfTheWeek.setTextColor(
                if (item.isSelected) {
                    ContextCompat.getColor(itemView.context, R.color.black)
                } else {
                    Color.BLACK
                }
            )

            with(binding.calendarLinearLayout) {
                if (item.isSelected) {
                    background =
                        ContextCompat.getDrawable(itemView.context, R.drawable.ic_white_ellipse)
                    isEnabled = false
                } else {
                    setBackgroundColor(Color.WHITE)
                    isEnabled = true
                }
            }
        }
    }

}
