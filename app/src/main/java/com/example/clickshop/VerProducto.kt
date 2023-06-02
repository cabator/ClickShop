package com.example.clickshop
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import android.widget.ImageView
import com.example.clickshop.modelos.ItemCarrito

class VerProducto: AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var btnMas: Button
    private lateinit var btnMenos: Button
    private lateinit var btnComprar: Button
    private lateinit var txtCantidad: TextView
    private lateinit var nombreTextView: TextView
    private lateinit var precioTextView: TextView
    private lateinit var descripcionTextView: TextView
    private lateinit var imagenImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_producto)

        toolbar = findViewById(R.id.toolbar)
        btnMas = findViewById(R.id.btn_Mas)
        btnMenos = findViewById(R.id.btn_menos)
        btnComprar = findViewById(R.id.btnComprar)
        txtCantidad = findViewById(R.id.txtCantidad)
        nombreTextView = findViewById(R.id.nombreVer)
        precioTextView = findViewById(R.id.precioVer)
        descripcionTextView = findViewById(R.id.descripcionVer)
        imagenImageView = findViewById(R.id.imagenVer)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Resta 1 a la cantidad cuando se presiona el botón "Menos"
        btnMenos.setOnClickListener {
            txtCantidad.text = (txtCantidad.text.toString().toInt() - 1).toString()
        }
        // Incrementa o decrementa la cantidad según el valor actual cuando se presiona el botón "Más"
        btnMas.setOnClickListener {
            val cantidadActual = txtCantidad.text.toString().toInt()
            if (cantidadActual > 2) {
                txtCantidad.text = (cantidadActual - 1).toString()
            } else {
                txtCantidad.text = (cantidadActual + 1).toString()
            }
        }
        // Crea un objeto ItemCarrito con los datos del producto y lo envía a la actividad del carrito
        btnComprar.setOnClickListener {
            val nombre = nombreTextView.text.toString()
            val precio = precioTextView.text.toString()
            val descripcion = descripcionTextView.text.toString()
            val imagenResId = intent.getIntExtra("imagen", 0)
            val cantidad = txtCantidad.text.toString().toInt()

            val itemCarrito = ItemCarrito(nombre, precio, descripcion, imagenResId, cantidad)

            val intent = Intent(this, Carrito::class.java)
            intent.putExtra("nombre", nombre)
            intent.putExtra("precio", precio)
            intent.putExtra("descripcion", descripcion)
            intent.putExtra("imagen", imagenResId)
            intent.putExtra("cantidad", cantidad)
            intent.putExtra("itemCarrito", itemCarrito) // Agregar el objeto itemCarrito al intent

            startActivity(intent)
        }
        // Obtiene los datos del producto de la intención y los muestra en la pantalla
        val nombre = intent.getStringExtra("nombre")
        val precio = intent.getStringExtra("precio")
        val descripcion = intent.getStringExtra("descripcion")
        val imagenResId = intent.getIntExtra("imagen", 0)

        nombreTextView.text = nombre
        precioTextView.text = precio
        descripcionTextView.text = descripcion
        imagenImageView.setImageResource(imagenResId)
    }
    // Controla el evento de seleccionar un elemento del menú de la barra de herramientas
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
private fun Intent.putExtra(s: String, itemCarrito: ItemCarrito) {
}
