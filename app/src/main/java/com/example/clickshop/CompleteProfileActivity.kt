package com.example.clickshop

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dmax.dialog.SpotsDialog

class CompleteProfileActivity : AppCompatActivity() {
    var mTextInputUsername: TextInputEditText? = null
    var mButtonConfirmar: Button? = null
    var mAuth: FirebaseAuth? = null
    var mFirestore: FirebaseFirestore? = null
    var mDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_profile)

        // Obtener referencias a los elementos de la interfaz de usuario
        mTextInputUsername = findViewById<TextInputEditText>(R.id.TextInputUsernameCp)
        mButtonConfirmar = findViewById<Button>(R.id.ButtonConfirmar)

        // Obtener instancia de FirebaseAuth y FirebaseFirestore
        mAuth = FirebaseAuth.getInstance()
        mFirestore = FirebaseFirestore.getInstance()

        // Crear y configurar el diálogo de espera
        mDialog = SpotsDialog.Builder()
            .setContext(this)
            .setMessage("Espere un momento")
            .setCancelable(false).build()

        // Configurar el evento onClick del botón "Confirmar"
        mButtonConfirmar?.setOnClickListener(View.OnClickListener { register() })
    }

    // Método para registrar el nombre de usuario
    private fun register() {
        val username = mTextInputUsername!!.text.toString()
        if (!username.isEmpty()) {
            // Actualizar el nombre de usuario en la base de datos
            upDateUser(username)
        } else {
            // Mostrar mensaje de error si el campo de nombre de usuario está vacío
            Toast.makeText(this, "Para continuar, inserta todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    // Método para actualizar el nombre de usuario en la base de datos
    private fun upDateUser(username: String) {
        val id = mAuth!!.currentUser!!.uid
        val map: MutableMap<String, Any> = HashMap()
        map["username"] = username

        // Mostrar el diálogo de espera
        mDialog!!.show()

        // Actualizar el documento del usuario en la colección "Users" de FirebaseFirestore
        mFirestore!!.collection("Users").document(id).update(map).addOnCompleteListener { task ->
            // Ocultar el diálogo de espera
            mDialog!!.dismiss()

            if (task.isSuccessful) {
                // Registro exitoso, redirigir a la página principal
                val intent = Intent(this@CompleteProfileActivity, HomeActivity::class.java)
                startActivity(intent)
            } else {
                // Mostrar mensaje de error si no se pudo almacenar el usuario en la base de datos
                Toast.makeText(this@CompleteProfileActivity, "No se pudo almacenar el usuario en la base de datos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
