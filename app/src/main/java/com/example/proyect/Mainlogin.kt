package com.example.proyect

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Mainlogin : AppCompatActivity() {
    var Email: EditText? = null
    var Pass: EditText? = null
    var Name: EditText? = null
    var Registrar: Button? = null
    var mAuth: FirebaseAuth? = null
    var progressBar: ProgressBar? = null
    var volver: TextView? = null
    public override fun onStart() {
        super.onStart()
        val currentUser = mAuth!!.currentUser
        if (currentUser != null) {
            val intent = Intent(applicationContext, Maintareas::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainlogin)
        mAuth = FirebaseAuth.getInstance()
        Email = findViewById(R.id.EmailR)
        Pass = findViewById(R.id.PassR)
        Name = findViewById(R.id.Name)
        Registrar = findViewById(R.id.Register)
        volver = findViewById(R.id.back)
        progressBar = findViewById(R.id.carga)
        progressBar?.setVisibility(View.GONE)
        volver?.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        })
        Registrar?.setOnClickListener(View.OnClickListener {
            progressBar?.setVisibility(View.VISIBLE)
            val email: String
            val password: String
            val nombre: String
            email = Email?.getText().toString()
            password = Pass?.getText().toString()
            nombre = Name?.getText().toString()
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this@Mainlogin, "Ingresa un Email", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this@Mainlogin, "Ingresa una Contraseña", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if (TextUtils.isEmpty(nombre)) {
                Toast.makeText(this@Mainlogin, "Ingresa un Nombre", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    progressBar?.setVisibility(View.GONE)
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@Mainlogin, "Cuenta Creada.",
                            Toast.LENGTH_SHORT
                        ).show()
                        val userID =
                            mAuth!!.currentUser!!.uid // Obtiene el UID del usuario registrado
                        asignarRolUsuario(userID, nombre)
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            this@Mainlogin, "Error.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        })
    }

    private fun asignarRolUsuario(userID: String, nombre: String) {
        val databaseReference =
            FirebaseDatabase.getInstance().getReference().child("usuarios").child(userID)
        databaseReference.child("rol").setValue("usuario").addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // El rol se asignó correctamente
            } else {
                // Error al asignar el rol
            }
        }
        databaseReference.child("nombre").setValue(nombre).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // El nombre se almacenó correctamente
            } else {
                // Error al almacenar el nombre
            }
        }
    }
}