package com.example.proyect

import com.example.proyect.date.currentDate
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateUnitTest {
    @get:Test
    val currentDate_isCorrect: Unit
        get() {
            // Configurar el formato esperado
            val expectedFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            // Obtener la fecha actual utilizando la función
            val actualDate = currentDate

            // Obtener la fecha actual utilizando el formato esperado
            val expectedDate = expectedFormat.format(Calendar.getInstance().time)

            // Comparar las fechas obtenidas
            Assert.assertEquals(expectedDate, actualDate)
        }
}