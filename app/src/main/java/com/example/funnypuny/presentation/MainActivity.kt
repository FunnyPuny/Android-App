package com.example.funnypuny.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.funnypuny.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), HabitItemFragment.OnHabitItemEditingFinishedListener{

    private lateinit var viewModel: MainViewModel
    private lateinit var habitListAdapter: HabitListAdapter
    private var habitItemContainer: FragmentContainerView? = null

    private lateinit var bottom_navigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        habitItemContainer = findViewById(R.id.habit_item_container)
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.habitList.observe(this) {
            habitListAdapter.submitList(it)
        }

        bottom_navigation = findViewById(R.id.bottom_navigation_main)
        bottom_navigation.itemIconTintList = null

        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_add_box -> {
                    if (isOnePaneMode()) {
                        val intent = HabitItemActivity.newIntentAddItem(this)
                        startActivity(intent)
                    } else {
                        launchFragment(HabitItemFragment.newInstanceAddItem())
                    }
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_home -> {
                    // put your code here
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                    // put your code here
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onHabitItemEditingFinished() {
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_LONG).show()
        supportFragmentManager.popBackStack()
    }

    private fun isOnePaneMode(): Boolean {
        return habitItemContainer == null
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.habit_item_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupRecyclerView() {
        val rvShopList = findViewById<RecyclerView>(R.id.rv_habit_list)
        with(rvShopList) {
            habitListAdapter = HabitListAdapter()
            adapter = habitListAdapter
            recycledViewPool.setMaxRecycledViews(
                HabitListAdapter.VIEW_TYPE_ENABLED,
                HabitListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                HabitListAdapter.VIEW_TYPE_DISABLED,
                HabitListAdapter.MAX_POOL_SIZE
            )
        }
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(rvShopList)
    }

    private fun setupSwipeListener(rvShopList: RecyclerView) {
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
                val item = habitListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteHabitItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun setupLongClickListener() {
        habitListAdapter.onHabitItemLongClickListener = {
            if (isOnePaneMode()) {
                Log.d("MainActivity", it.toString())
                val intent = HabitItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            } else {
                launchFragment(HabitItemFragment.newInstanceEditItem(it.id))
            }
        }
    }

    private fun setupClickListener() {
        habitListAdapter.onHabitItemClickListener = {
            viewModel.changeEnableState(it)
        }
    }

}