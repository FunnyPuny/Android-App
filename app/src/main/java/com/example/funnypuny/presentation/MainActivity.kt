package com.example.funnypuny.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.funnypuny.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: HabbitListAdapter

    private lateinit var bottom_navigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.habbitList.observe(this){
             adapter.list = it
        }

        bottom_navigation = findViewById(R.id.bottom_navigation_main)
        bottom_navigation.itemIconTintList = null
    }

    override fun onClick(view: View?) {
        /*when(view?.id){
            R.id.iv_add ->{
                Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show()
            }
        }*/
    }

    private fun setupRecyclerView() {
        val rvHabbitList = findViewById<RecyclerView>(R.id.rv_habbit_list)
        adapter = HabbitListAdapter()
        rvHabbitList.adapter = adapter
    }

}