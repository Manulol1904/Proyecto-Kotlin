package com.example.proyect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Collections

class Maintareas : AppCompatActivity() {
    var tareasRef: DatabaseReference? = null
    var salir: TextView? = null
    var auth: FirebaseAuth? = null
    var user: FirebaseUser? = null
    var recyclerView: RecyclerView? = null
    var tareaAdapter: Tadapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maintareas)
        auth = FirebaseAuth.getInstance()
        user = auth!!.currentUser
        salir = findViewById(R.id.salir)
        if (user == null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val rolesRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(
                user!!.uid
            )
            rolesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val rol = dataSnapshot.child("rol").getValue(
                        String::class.java
                    )
                    if (rol == null || rol != "usuario") {
                        FirebaseAuth.getInstance().signOut()
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Manejar errores de lectura de la base de datos
                }
            })
        }
        salir?.setOnClickListener(View.OnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        })
        tareasRef = FirebaseDatabase.getInstance().getReference().child("tareas")
        recyclerView = findViewById(R.id.recyclerView)
        tareaAdapter = Tadapter(this, ArrayList())
        recyclerView?.setAdapter(tareaAdapter)
        recyclerView?.setLayoutManager(LinearLayoutManager(this))
        cargarTareas()
        tareaAdapter!!.setOnItemClickListener(object : Tadapter.OnItemClickListener {
            override fun onItemClick(tarea: Tarea?) {
                // Agrega un log para verificar que el clic en la CardView funciona
                Log.d("Maintareas", "Clic en CardView: " + tarea!!.name)

                // Agrega el código para abrir la actividad DetallesTareaActivity
                val intent = Intent(applicationContext, Complete::class.java)
                intent.putExtra("tarea", tarea)
                intent.putExtra("key", tarea.key)
                startActivity(intent)
            }
        })
    }

    private fun cargarTareas() {
        // Obtener el nombre del usuario actual desde la base de datos
        val usuarioRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(
            user!!.uid
        )
        usuarioRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val nombreUsuario = dataSnapshot.child("nombre").getValue(
                        String::class.java
                    )
                    nombreUsuario?.let { filtrarTareasPorUsuario(it) }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar errores de cancelación de la lectura de la base de datos
            }
        })
    }

    private fun filtrarTareasPorUsuario(nombreUsuario: String) {
        tareasRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tareas: MutableList<Tarea?> = ArrayList()
                for (tareaSnapshot in dataSnapshot.children) {
                    val tarea = tareaSnapshot.getValue(Tarea::class.java)
                    tarea?.key = tareaSnapshot.key
                    // Verificar si la tarea pertenece al usuario actual
                    if (tarea?.nombre != null && tarea.nombre == nombreUsuario) {
                        tareas.add(tarea)
                    }
                }
                Collections.reverse(tareas)
                tareaAdapter?.setTareas(tareas.filterNotNull())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar errores de cancelación de la lectura de la base de datos
            }
        })
    }

}