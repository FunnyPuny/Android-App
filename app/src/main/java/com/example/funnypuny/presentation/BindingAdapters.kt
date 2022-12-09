package com.example.funnypuny.presentation

import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.example.funnypuny.R

@BindingAdapter("errorInputName")
fun bindErrorInputName(text: EditText, isError: Boolean) {
        val message = if (isError) {
            text.context.getString(R.string.error_input_name)
        } else {
            null
        }
        text. error = message
}