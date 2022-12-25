package com.example.funnypuny.presentation

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.funnypuny.R
import com.example.funnypuny.databinding.FragmentStatisticsBinding


class StatisticsFragment : Fragment() {

    private var status = 0
    private val handler = Handler()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val drawable = resources.getDrawable(R.drawable.circularprogressbar, null)
        //val drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.circularprogressbar)
        //val drawable = ResourcesCompat.getDrawable(getResources(),R.drawable.circularprogressbar, null)
        //val progressBar: ProgressBar = findViewById(R.id.circularProgressbar)
        binding.circularProgressbar.progress = 0
        binding.circularProgressbar.secondaryProgress = 0
        binding.circularProgressbar.max = 100
        binding.circularProgressbar.progressDrawable = drawable
        Thread {
            while (status < 100) {
                status += 1
                handler.post {
                    binding.circularProgressbar.progress = status
                    binding.textView.text = String.format("%d%%", status)
                }
                try {
                    Thread.sleep(16)
                }
                catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
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