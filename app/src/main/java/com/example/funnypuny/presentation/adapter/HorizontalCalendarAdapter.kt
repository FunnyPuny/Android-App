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
    private var listener: OnItemClickListener
    /*private val currentDate: Calendar,
    private val changeMonth: Calendar?*/
) : RecyclerView.Adapter<HorizontalCalendarAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    //private var mListener: OnItemClickListener? = null
    //private var index = -1

    // This is true only the first time you load the calendar.
    //private var selectCurrentDate = true
    /*private val currentMonth = currentDate[Calendar.MONTH]
    private val currentYear = currentDate[Calendar.YEAR]
    private val currentDay = currentDate[Calendar.DAY_OF_MONTH]*/
    /*private val selectedDay =
        when {
            changeMonth != null -> changeMonth.getActualMinimum(Calendar.DAY_OF_MONTH)
            else -> currentDay
        }
    private val selectedMonth =
        when {
            changeMonth != null -> changeMonth[Calendar.MONTH]
            else -> currentMonth
        }
    private val selectedYear =
        when {
            changeMonth != null -> changeMonth[Calendar.YEAR]
            else -> currentYear
        }*/

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val binding = ItemDayBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dates.size

    /*override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss", Locale.ENGLISH)
        val cal = Calendar.getInstance()
        //cal.time = data[position]

        *//**
         * Set the year, month and day that is gonna be displayed
         *//*
        val displayMonth = cal[Calendar.MONTH]
        val displayYear = cal[Calendar.YEAR]
        val displayDay = cal[Calendar.DAY_OF_MONTH]

        *//**
         * Set text to txtDayInWeek and txtDay.
         *//*
        try {
            val dayInWeek = sdf.parse(cal.time.toString())!!
            sdf.applyPattern("EEE")
            holder.txtDayInWeek!!.text = sdf.format(dayInWeek).toString()
        } catch (ex: ParseException) {
            Log.v("Exception", ex.localizedMessage!!)
        }
        holder.txtDay!!.text = cal[Calendar.DAY_OF_MONTH].toString()

        *//*if (displayYear >= currentYear)
            if (displayMonth >= currentMonth || displayYear > currentYear)
                if (displayDay >= currentDay || displayMonth > currentMonth || displayYear > currentYear) {
                    *//**//**
                     * Invoke OnClickListener and make the item selected.
                     *//**//*
                    holder.linearLayout!!.setOnClickListener {
                        index = position
                        selectCurrentDate = false
                        holder.listener.onItemClick(position)
                        notifyDataSetChanged()
                    }

                    if (index == position)
                        makeItemSelected(holder)
                    else {
                        if (displayDay == selectedDay
                            && displayMonth == selectedMonth
                            && displayYear == selectedYear
                            && selectCurrentDate
                        )
                            makeItemSelected(holder)
                        else
                            makeItemDefault(holder)
                    }
                } else makeItemDisabled(holder)
            else makeItemDisabled(holder)
        else makeItemDisabled(holder)*//*
    }*/

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dates[position]
        holder.bind(item)

    }

    /*inner class ViewHolder(itemView: View, val listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        private val viewBinding = ItemDayBinding.bind(itemView)
        *//*var txtDay = itemView.findViewById<TextView>(R.id.tvDate)
        var txtDayInWeek = itemView.findViewById<TextView>(R.id.tvDay)
        var linearLayout = itemView.findViewById<LinearLayout>(R.id.calendar_linear_layout)*//*

    }*/
    class ViewHolder(private val binding: ItemDayBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HorizontalCalendarItem) {
            binding.tvDate.text = item.date.toString()

            //val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss", Locale.ENGLISH)
            val sdf = SimpleDateFormat("EEE", Locale.ENGLISH)
            val cal = Calendar.getInstance()
            cal.time = item.date
            binding.tvDay.text = sdf.parse(cal.time.toString())?.let { sdf.format(it).toString() }
            binding.tvDate.text = cal[Calendar.DAY_OF_MONTH].toString()

            //todo перенести if/else внутрь setTextColor or "background ="
            if (item.isSelected) {
                //This make the item selected.
                binding.tvDate.setTextColor(Color.parseColor("#272937"))
                binding.tvDate.background =
                    ContextCompat.getDrawable(itemView.context, R.drawable.ic_ellipse_enable)
                binding.tvDay.setTextColor(Color.parseColor("#272937"))
                binding.calendarLinearLayout.background =
                    ContextCompat.getDrawable(itemView.context, R.drawable.ic_white_ellipse)
                binding.calendarLinearLayout.isEnabled = false
            }
            else {
                //This make the item default.
                binding.tvDate.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.grey_dark)
                )
                binding.tvDate.background =
                    ContextCompat.getDrawable(itemView.context, R.drawable.ic_ellipse_disable)
                binding.tvDay.setTextColor(Color.BLACK)
                binding.calendarLinearLayout.setBackgroundColor(Color.WHITE)
                binding.calendarLinearLayout.isEnabled = true
            }
        }
    }

}
