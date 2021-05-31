package com.example.marvel.utils

import android.app.Activity
import android.view.WindowManager
import androidx.fragment.app.Fragment

object Extensions {

    fun Activity.setNoLimitsWindow(){
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    fun Fragment.setNoLimitsWindow(){
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
}