package com.example.proyect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Detalles : AppCompatActivity() {
    var name: EditText? = null
    var detail: EditText? = null
    var back: TextView? = null
    var edit: Button? = null
    var delete: Button? = null
    var tareasRef: DatabaseReference? = null
    var auth: FirebaseAuth? = null
    var user: FirebaseUser? = null
    var tarea: Tarea? = null // Variable para almacenar la tarea actual
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles)
        edit = findViewById(R.id.edit)
        delete = findViewById(R.id.delete)
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
                val intent = Intent(applicationContext, Admin::class.java)
                startActivity(intent)
                finish()
            })
            delete?.setOnClickListener(View.OnClickListener {
                eliminarTarea()
                val intent = Intent(applicationContext, Admin::class.java)
                startActivity(intent)
                finish()
            })
            edit?.setOnClickListener(View.OnClickListener {
                actualizarDatosEnFirebase()
                val intent = Intent(applicationContext, Admin::class.java)
                startActivity(intent)
                finish()
            })
        }
    }

    private fun actualizarDatosEnFirebase() {
        // Obtiene los nuevos datos de los EditText
        val nuevoNombre = name!!.getText().toString().trim { it <= ' ' }
        val nuevoDetalle = detail!!.getText().toString().trim { it <= ' ' }

        // Verifica si los datos han cambiado
        if (nuevoNombre != tarea!!.name || nuevoDetalle != tarea!!.detalles) {
            // Actualiza los datos en Firebase solo si hay cambios
            tarea!!.name = nuevoNombre
            tarea!!.detalles = nuevoDetalle
            tarea!!.date = date.currentDate;
            tareasRef = FirebaseDatabase.getInstance().getReference()
                .child("tareas")
                .child(tarea!!.key!!)
            tareasRef!!.setValue(tarea).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@Detalles,
                        "Datos actualizados correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@Detalles,
                        "Error al actualizar los datos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
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
                        this@Detalles,
                        "Tarea eliminada correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    Toast.makeText(this@Detalles, "Error al eliminar la tarea", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else {
            Log.e("Detalles", "La tarea o la clave (key) son nulas")
        }
    }
}
