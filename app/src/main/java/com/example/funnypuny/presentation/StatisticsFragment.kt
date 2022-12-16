package com.example.funnypuny.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.funnypuny.R
import com.example.funnypuny.databinding.FragmentHabitItemBinding
import com.example.funnypuny.databinding.FragmentStatisticsBinding


class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding: FragmentStatisticsBinding
        get() = _binding ?: throw RuntimeException("FragmentStatisticsBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        Log.d("StatisticsFragment", "onDestroyView")
        super.onDestroyView()
        _binding = null
    }/*

    companion object {
        fun newInstance() =
            StatisticsFragment().apply {
                arguments = Bundle().apply {
                }

        }
    }*/
}