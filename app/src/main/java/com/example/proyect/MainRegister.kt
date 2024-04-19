package com.example.proyect

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainRegister : AppCompatActivity() {
    var task: EditText? = null
    var dt: EditText? = null
    var add: Button? = null
    var spinner: Spinner? = null
    var back: TextView? = null
    var tareasRef: DatabaseReference? = null
    var auth: FirebaseAuth? = null
    var user: FirebaseUser? = null
    var progressBar: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_register)
        auth = FirebaseAuth.getInstance()
        user = auth!!.currentUser
        tareasRef = FirebaseDatabase.getInstance().getReference().child("tareas")
        task = findViewById(R.id.named)
        dt = findViewById(R.id.detaild)
        add = findViewById(R.id.edit)
        progressBar = findViewById(R.id.load)
        progressBar?.setVisibility(View.GONE)
        back = findViewById(R.id.backpd)
        spinner = findViewById(R.id.spinner)
        val usuariosRef = FirebaseDatabase.getInstance().getReference().child("usuarios")
        usuariosRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val nombresUsuarios: MutableList<String?> = ArrayList()
                for (usuarioSnapshot in dataSnapshot.getChildren()) {
                    val rol = usuarioSnapshot.child("rol").getValue(
                        String::class.java
                    )
                    if (rol != null && rol == "usuario") {
                        val nombreUsuario = usuarioSnapshot.child("nombre").getValue(
                            String::class.java
                        )
                        nombresUsuarios.add(nombreUsuario)
                    }
                }
                val adapter = ArrayAdapter(
                    this@MainRegister,
                    android.R.layout.simple_spinner_item,
                    nombresUsuarios
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner?.setAdapter(adapter)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar errores de lectura de la base de datos
            }
        })
        if (user == null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        back?.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, Maintareas::class.java)
            startActivity(intent)
            finish()
        })
        add?.setOnClickListener(View.OnClickListener {
            progressBar?.setVisibility(View.VISIBLE)
            addTarea()
            Toast.makeText(
                this@MainRegister, "Task added.",
                Toast.LENGTH_SHORT
            ).show()
            progressBar?.setVisibility(View.GONE)
            val intent = Intent(applicationContext, Admin::class.java)
            startActivity(intent)
            finish()
        })
    }

    private fun addTarea() {
        val nombre: String
        val detalle: String
        val currentDate = date.currentDate;
        nombre = task!!.getText().toString()
        detalle = dt!!.getText().toString()
        val nombreSeleccionado = spinner!!.getSelectedItem().toString()
        val tarea = Tarea()
        tarea.name = nombre
        tarea.detalles = detalle
        tarea.date = currentDate
        tarea.nombre = nombreSeleccionado
        tareasRef!!.push().setValue(tarea)
    }
}
