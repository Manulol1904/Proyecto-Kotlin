package com.example.proyect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Complete : AppCompatActivity() {
    var name: TextView? = null
    var detail: TextView? = null
    var back: TextView? = null
    var complete: Button? = null
    var tareasRef: DatabaseReference? = null
    var auth: FirebaseAuth? = null
    var user: FirebaseUser? = null
    var tarea: Tarea? = null // Variable para almacenar la tarea actual
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete)
        complete = findViewById(R.id.complete)
        auth = FirebaseAuth.getInstance()
        user = auth!!.currentUser
        val intent = intent
        if (intent != null && intent.hasExtra("tarea")) {
            tarea = intent.getSerializableExtra("tarea") as Tarea?
            val key = intent.getStringExtra("key")
            name = findViewById(R.id.named)
            detail = findViewById(R.id.detaild)
            name?.setText(tarea!!.name)
            detail?.setText(tarea!!.detalles)
            back = findViewById(R.id.backpd)
            back?.setOnClickListener(View.OnClickListener {
                val intent = Intent(applicationContext, Maintareas::class.java)
                startActivity(intent)
                finish()
            })
            complete?.setOnClickListener(View.OnClickListener {
                eliminarTarea()
                val intent = Intent(applicationContext, Maintareas::class.java)
                startActivity(intent)
                finish()
            })
        }
    }

    private fun eliminarTarea() {
        // Asegúrate de tener la tarea actual
        if (tarea != null && tarea!!.key != null) {
            val tareaRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("tareas")
                .child(tarea!!.key!!)
            tareaRef.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@Complete,
                        "Tarea eliminada correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(this@Complete, "Error al eliminar la tarea", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else {
            Log.e("Detalles", "La tarea o la clave (key) son nulas")
        }
    }
}
