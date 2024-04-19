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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    var Email: EditText? = null
    var Pass: EditText? = null
    var Login: Button? = null
    var mAuth: FirebaseAuth? = null
    var progressBar: ProgressBar? = null
    var registro: TextView? = null
    public override fun onStart() {
        super.onStart()
        val currentUser = mAuth!!.currentUser
        if (currentUser != null) {
            val userRef = FirebaseDatabase.getInstance().getReference().child("usuarios")
                .child(currentUser.uid)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val rol = dataSnapshot.child("rol").getValue(
                        String::class.java
                    )
                    if (rol != null) {
                        if (rol == "usuario") {
                            val intent = Intent(applicationContext, Maintareas::class.java)
                            startActivity(intent)
                            finish()
                        } else if (rol == "admin") {
                            val intent = Intent(applicationContext, Admin::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        Email = findViewById(R.id.Email)
        Pass = findViewById(R.id.Pass)
        Login = findViewById(R.id.Login)
        registro = findViewById(R.id.Registro)
        progressBar = findViewById(R.id.carga)
        progressBar?.setVisibility(View.GONE)
        registro?.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, Mainlogin::class.java)
            startActivity(intent)
            finish()
        })
        Login?.setOnClickListener(View.OnClickListener {
            progressBar?.setVisibility(View.VISIBLE)
            val email: String
            val password: String
            email = Email?.getText().toString()
            password = Pass?.getText().toString()
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this@MainActivity, "Ingresa un Email", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this@MainActivity, "Ingresa una Contraseña", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            }
            mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    progressBar?.setVisibility(View.GONE)
                    if (task.isSuccessful) {
                        val currentUserRef =
                            FirebaseDatabase.getInstance().getReference().child("usuarios").child(
                                mAuth!!.currentUser!!.uid
                            )
                        currentUserRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val rol = dataSnapshot.child("rol").getValue(
                                    String::class.java
                                )
                                if (rol != null && rol == "admin") {
                                    val intent = Intent(this@MainActivity, Admin::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    val intent = Intent(this@MainActivity, Maintareas::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Manejar errores de lectura de la base de datos
                            }
                        })
                    } else {
                        Toast.makeText(
                            this@MainActivity, "Error en los datos.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        })
    }
}