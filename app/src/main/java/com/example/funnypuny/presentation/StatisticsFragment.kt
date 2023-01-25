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
import com.example.funnypuny.presentation.viewmodel.StatisticViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class StatisticsFragment : Fragment() {

    private var status = 0
    private val handler = Handler()

    private var _binding: FragmentStatisticsBinding? = null
    private val binding: FragmentStatisticsBinding
        get() = _binding ?: throw RuntimeException("FragmentStatisticsBinding == null")

    val viewModel: StatisticViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("HabitItemFragment", "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val drawable = resources.getDrawable(R.drawable.circularprogressbar, null)
        //val drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.circularprogressbar)
        //val drawable = ResourcesCompat.getDrawable(getResources(),R.drawable.circularprogressbar, null)
        //val progressBar: ProgressBar = findViewById(R.id.circularProgressbar)
        binding.prgbCircularProgress.progress = 0
        binding.prgbCircularProgress.secondaryProgress = 0
        binding.prgbCircularProgress.max = 100
        binding.prgbCircularProgress.progressDrawable = drawable
        Thread {
            while (status < 100) {
                status += 1
                handler.post {
                    binding.prgbCircularProgress.progress = status
                    binding.tvCircularProgress.text = String.format("%d%%", status)
                }
                try {
                    Thread.sleep(16)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()

        /*val languages = resources.getStringArray(R.array.Languages)


        // access the spinner
        if (binding.spinerDropDown != null) {
            val adapter = activity?.let {
                ArrayAdapter(
                    it.baseContext,
                    android.R.layout.simple_spinner_item, languages)
            }

            binding.spinerDropDown.adapter = adapter

            binding.spinerDropDown.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }

            viewModel.habitListState.observe(viewLifecycleOwner) {
                habitListAdapter?.submitList(it)
            }

        }*/

    }
    override fun onDestroyView() {
        Log.d("StatisticsFragment", "onDestroyView")
        super.onDestroyView()
        _binding = null
    }

    companion object{

        private const val EXTRA_SCREEN_MODE = "extra_screen_mode"
        private const val HABIT_ITEM_ID = "habit_item_id"

        /*fun newInstance(habitItemId: Int): StatisticFragment {
            return StatisticFragment().apply {
                arguments = Bundle().apply {
                    //putSerializable(EXTRA_SCREEN_MODE, HabitItemAction.EDIT)
                    putInt(HABIT_ITEM_ID, habitItemId)
                }
            }
        }*/

    }
}
