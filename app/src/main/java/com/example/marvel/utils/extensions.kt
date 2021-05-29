package com.example.marvel.utils

import android.app.Activity
import android.view.WindowManager

object extensions {

    fun Activity.setNoLimitsWindow(){
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
}