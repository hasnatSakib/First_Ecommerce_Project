package com.example.firstecommerceproject.ui.util

import android.content.Context
import android.widget.Toast

/**
 * Utility object containing common helper functions for the UI layer.
 * 
 * This object serves as a centralized place for reusable UI-related logic 
 * that doesn't belong to a specific screen or component.
 */
object AppUtil {
    /**
     * Displays a short-duration Toast message to the user.
     *
     * @param context The context used to display the toast.
     * @param message The text message to be shown.
     */
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}