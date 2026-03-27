package com.example.firstecommerceproject.ui.util

import android.content.Context
import android.widget.Toast

object AppUtil {
    fun showToast(context : Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}