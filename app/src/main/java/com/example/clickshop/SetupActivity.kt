package com.example.clickshop

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView

class SetupActivity : AppCompatActivity() {

    private var compraRealizada = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        showPaymentNoticeDialog()

        // Obtener datos del producto de la actividad anterior
        val nombreProducto = intent.getStringExtra("nombreProducto")
        val descripcionProducto = intent.getStringExtra("descripcionProducto")
        val precioProducto = intent.getStringExtra("precioProducto")

        // Configurar el campo de producto con los datos obtenidos
        val campoProducto = findViewById<EditText>(R.id.campo_producto)
        campoProducto.text = SpannableStringBuilder()
            .append("Nombre: $nombreProducto\n")
            .append("Precio: $precioProducto\n")
            .append("Descripción: $descripcionProducto")

        // Obtener referencias a los campos de entrada de datos del usuario
        val setupNombre = findViewById<EditText>(R.id.setup_nombre)
        val setupDireccion = findViewById<EditText>(R.id.setup_direccion)
        val setupTelefono = findViewById<EditText>(R.id.setup_telefono)
        val setupCorreo = findViewById<EditText>(R.id.setup_correo)

        // Obtener una instancia de la base de datos Firestore
        val db = FirebaseFirestore.getInstance()

        // Obtener referencia al botón de configuración
        val setupBoton = findViewById<Button>(R.id.setup_boton)
        setupBoton.setOnClickListener {
            if (!compraRealizada) {
                // Obtener los valores ingresados por el usuario
                val nombre = setupNombre.text.toString().trim()
                val direccion = setupDireccion.text.toString().trim()
                val telefono = setupTelefono.text.toString().trim()
                val correo = setupCorreo.text.toString().trim()

                if (nombre.isNotEmpty() && direccion.isNotEmpty() && telefono.isNotEmpty() && correo.isNotEmpty()) {
                    // Crear un mapa de datos con la información de la compra
                    val datosCompra = hashMapOf(
                        "nombre" to nombre,
                        "direccion" to direccion,
                        "telefono" to telefono,
                        "correo" to correo,
                        "producto" to campoProducto.text.toString()
                    )

                    // Guardar los datos de la compra en Firestore
                    db.collection("compras")
                        .add(datosCompra)
                        .addOnSuccessListener { documentReference ->
                            // Registro exitoso
                            // Mostrar mensaje de éxito y sugerencia de revisar el correo
                            Toast.makeText(this, "Compra realizada con éxito. Revisa tu correo.", Toast.LENGTH_SHORT).show()
                            compraRealizada = true
                            setupBoton.isEnabled = false // Desactivar el botón
                        }
                        .addOnFailureListener { e ->
                            // Error al guardar los datos
                            // Mostrar mensaje de error
                            Toast.makeText(this, "Error al realizar la compra. Inténtalo nuevamente.", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Mostrar mensaje de campos vacíos
                    Toast.makeText(this, "Debes llenar todos los campos.", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Mostrar mensaje de compra ya realizada
                Toast.makeText(this, "La compra ya ha sido realizada.", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar el botón de retorno a la página principal
        val returnHomeButton = findViewById<CircleImageView>(R.id.return_home)
        returnHomeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    // Método para mostrar el diálogo de aviso de pago
    private fun showPaymentNoticeDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)

        val title = SpannableString("Aviso")
        title.setSpan(ForegroundColorSpan(Color.WHITE), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        builder.setTitle(title)
        builder.setMessage("Únicamente pago contra entrega")
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            // Acción al presionar el botón "Aceptar" del aviso
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE)?.setTextColor(resources.getColor(android.R.color.white))
            dialog.findViewById<TextView>(android.R.id.message)?.setTextAppearance(R.style.CustomAlertDialogText)
        }

        dialog.show()
    }
}
