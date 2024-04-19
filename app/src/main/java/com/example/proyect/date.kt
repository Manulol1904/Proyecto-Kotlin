package com.example.proyect

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object date {
    @JvmStatic
    val currentDate: String
        get() {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return dateFormat.format(calendar.time)
        }
}
