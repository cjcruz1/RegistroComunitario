package com.grupo4.encuesta

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics

class AvailableDevices : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.availabledevices)
        var displayMetrics :DisplayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        var width :Int = displayMetrics.widthPixels
        var height :Int = displayMetrics.heightPixels

        window.setLayout(width*.8 as Int, height*.6 as Int)

    }

}
