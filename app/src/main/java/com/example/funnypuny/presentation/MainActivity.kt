package com.example.funnypuny.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.funnypuny.R
import com.example.funnypuny.domain.HabbitItem
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var habbitListAdapter: HabbitListAdapter

    private lateinit var bottom_navigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.habbitList.observe(this){
             habbitListAdapter.list = it
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
        with(rvHabbitList) {
            habbitListAdapter = HabbitListAdapter()
            adapter = habbitListAdapter
            recycledViewPool.setMaxRecycledViews(
                HabbitListAdapter.VIEW_TYPE_ENABLED,
                HabbitListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                HabbitListAdapter.VIEW_TYPE_DISABLED,
                HabbitListAdapter.MAX_POOL_SIZE
            )
        }

        setupClickListener()
        setupLongClickListener()
        setupSwipeListener(rvHabbitList)
    }

    private fun setupSwipeListener(rvHabbitList: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = habbitListAdapter.list[viewHolder.adapterPosition]
                viewModel.deleteHabbitItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvHabbitList)
    }

    private fun setupLongClickListener() {
        habbitListAdapter.onHabbitItemLongClickListener = {
            viewModel.deleteHabbitItem(it)
        }
    }

    private fun setupClickListener() {
        habbitListAdapter.onHabbitItemClickListener = {
            viewModel.changeEnabledState(it)
        }
    }

}