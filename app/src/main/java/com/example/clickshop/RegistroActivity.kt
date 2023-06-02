package com.example.clickshop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern

class RegistroActivity : AppCompatActivity() {
    private lateinit var mCircleImageView: ImageView
    private lateinit var mTextInputUserName: TextInputEditText
    private lateinit var mTextInputEmail: TextInputEditText
    private lateinit var mTextInputPassword: TextInputEditText
    private lateinit var mTextInputConfirmPassword: TextInputEditText
    private lateinit var mButtonRegister: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Obtener referencias a los elementos de la interfaz de usuario
        mCircleImageView = findViewById(R.id.circleimage)
        mTextInputUserName = findViewById(R.id.TextInputUserNameR)
        mTextInputEmail = findViewById(R.id.TextViewInputEmailR)
        mTextInputPassword = findViewById(R.id.TextViewInputPasswordR)
        mTextInputConfirmPassword = findViewById(R.id.TextViewInputConfirmPassword)
        mButtonRegister = findViewById(R.id.BtnRegister)

        // Obtener instancia de FirebaseAuth y FirebaseFirestore
        mAuth = FirebaseAuth.getInstance()
        mFirestore = FirebaseFirestore.getInstance()

        // Configurar el evento onClick del círculo de imagen (volver atrás)
        mCircleImageView.setOnClickListener {
            finish()
        }

        // Configurar el evento onClick del botón "Registrar"
        mButtonRegister.setOnClickListener {
            register()
        }
    }

    // Método para realizar el registro del usuario
    private fun register() {
        val username = mTextInputUserName.text.toString()
        val email = mTextInputEmail.text.toString()
        val password = mTextInputPassword.text.toString()
        val confirmPassword = mTextInputConfirmPassword.text.toString()

        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
            if (isEmailValid(email)) {
                if (password.equals(confirmPassword)) {
                    if (password.length >= 6) {
                        // Crear el usuario en Firebase Authentication y almacenar la información en Firestore
                        createUser(email, password, username)
                    } else {
                        Toast.makeText(this, "Las contraseñas deben tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Has insertado todos los campos pero el correo no es válido", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Para continuar, inserta todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    // Método para crear el usuario en Firebase Authentication y almacenar la información en Firestore
    private fun createUser(email: String, password: String, username: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val id = mAuth.currentUser?.uid
                    val map = hashMapOf<String, Any>()
                    map["email"] = email
                    map["username"] = username
                    map["password"] = password

                    // Almacenar la información del usuario en la colección "Users" de FirebaseFirestore
                    mFirestore.collection("Users").document(id!!).set(map).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@RegistroActivity, "El usuario se almacenó correctamente", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@RegistroActivity, HomeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        } else {
                            Toast.makeText(this@RegistroActivity, "No se pudo almacenar en la base de datos", Toast.LENGTH_SHORT).show()
                        }
                    }

                    Toast.makeText(this@RegistroActivity, "El usuario se registró correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@RegistroActivity, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Método para verificar si un correo electrónico es válido
    private fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@[\\w\\.-]+\\.[A-Za-z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }
}
