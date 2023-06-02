package com.example.clickshop

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import dmax.dialog.SpotsDialog

class MainActivity : AppCompatActivity() {
    private lateinit var mTextInputEditEmail: TextInputEditText
    private lateinit var mTextInputEditPassword: TextInputEditText
    private lateinit var mButtonLogin: Button
    private lateinit var mFirestore: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val REQUEST_CODE_GOOGLE = 1
    var mSignInButtonGooogle: SignInButton? = null
    var mDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTextInputEditEmail = findViewById(R.id.TextViewEmail)
        mTextInputEditPassword = findViewById(R.id.TextViewPassword)
        mButtonLogin = findViewById(R.id.BtnLogin)
        mSignInButtonGooogle = findViewById(R.id.BtnLoginGoogle)

        mDialog = SpotsDialog.Builder()
            .setContext(this)
            .setMessage("Espere un momento")
            .setCancelable(false).build()

        mAuth = FirebaseAuth.getInstance()
        mFirestore = FirebaseFirestore.getInstance()

        // Configurar Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mSignInButtonGooogle?.setOnClickListener(View.OnClickListener { signInGoogle() })

        mButtonLogin.setOnClickListener {
            login()
        }
    }

    // Método para ir a la pantalla de inicio (temporal, para pruebas)
    fun IrIniciar(view: View) {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    // Método para ir a la pantalla de registro (temporal, para pruebas)
    fun IrRegistrarse(view: View) {
        val intent = Intent(this, RegistroActivity::class.java)
        startActivity(intent)
    }

    // Método para iniciar sesión con Google
    private fun signInGoogle() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE)
    }

    // Método para manejar el resultado de la actividad de inicio de sesión de Google
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_GOOGLE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val idToken = account.idToken
                    if (idToken != null) {
                        firebaseAuthWithGoogle(idToken)
                    } else {
                        Log.w("ERROR", "Google ID token is null")
                    }
                } else {
                    Log.w("ERROR", "Google account is null")
                }
            } catch (e: ApiException) {
                Log.w("ERROR", "Google sign in failed", e)
            }
        }
    }

    // Método para autenticar con Firebase utilizando el token de inicio de sesión de Google
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val id = mAuth.currentUser!!.uid
                    checkUserExist(id)
                } else {
                    Log.w("ERROR", "signInWithCredential:failure", task.exception)
                }
            }
    }

    // Método para verificar si el usuario ya existe en la base de datos de Firestore
    private fun checkUserExist(id: String) {
        mFirestore.collection("Users").document(id).get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                mDialog!!.dismiss()
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                startActivity(intent)
            } else {
                val email = mAuth.currentUser!!.email
                val map: MutableMap<String, Any?> = HashMap()
                map["email"] = email
                mFirestore.collection("Users").document(id).set(map).addOnCompleteListener { task ->
                    mDialog!!.dismiss()
                    if (task.isSuccessful) {
                        val intent = Intent(this@MainActivity, CompleteProfileActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "No se pudo almacenar en la base de datos", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // Método para realizar el inicio de sesión con email y contraseña
    private fun login() {
        val email = mTextInputEditEmail.text.toString()
        val password = mTextInputEditPassword.text.toString()
        mDialog!!.show()

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                mDialog!!.dismiss()

                if (task.isSuccessful) {
                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@MainActivity, "El email y la contraseña no son correctos", Toast.LENGTH_SHORT).show()
                }
            }

        Log.d("Campo", "email: $email")
        Log.d("Campo", "password: $password")
    }
}
