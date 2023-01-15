package com.example.funnypuny.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import com.example.funnypuny.R
import com.example.funnypuny.databinding.ActivityHabitItemBinding
import com.example.funnypuny.databinding.ActivityStatisticsBinding

class StatisticsActivity : AppCompatActivity() {

    private var _binding: ActivityStatisticsBinding? = null
    private val binding: ActivityStatisticsBinding
        get() = _binding ?: throw RuntimeException("ActivityStatisticsBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .replace(R.id.statistics_container, StatisticsFragment())
            .commit()

    }

    companion object {

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newIntent(context: Context): Intent {
            val intent = Intent(context, StatisticsActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }
    }
}
