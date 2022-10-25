package com.example.funnypuny

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var bottom_navigation: BottomNavigationView
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottom_navigation = findViewById(R.id.bottom_navigation_main)
        //imageView = findViewById(R.id.iv_add)

        bottom_navigation.itemIconTintList = null
        //imageView.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        /*when(view?.id){
            R.id.iv_add ->{
                Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show()
            }
        }*/
    }


}