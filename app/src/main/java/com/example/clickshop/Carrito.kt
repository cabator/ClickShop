package com.example.clickshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clickshop.adapters.CarritoAdapter
import com.example.clickshop.modelos.ItemCarrito

class Carrito : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var siguiente: Button
    private lateinit var totalPrecio: TextView
    private lateinit var agregarMasProductos: Button

    private var listaCarrito = mutableListOf<ItemCarrito>()
    private var precioTotalId = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        recyclerView = findViewById(R.id.listaCarrito)
        recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        siguiente = findViewById(R.id.siguiente_proceso)
        totalPrecio = findViewById(R.id.precio_total)
        agregarMasProductos = findViewById(R.id.agregarMasProductos)

        siguiente.setOnClickListener {
            // Obtén los datos del primer producto en la lista de carrito
            val nombreProducto = listaCarrito[0].nombre
            val descripcionProducto = listaCarrito[0].descripcion
            val precioProducto = listaCarrito[0].precio
            val cantidadProducto = listaCarrito[0].cantidad

            // Crea un intent para iniciar la actividad SetupActivity y pasa los datos del producto
            val intent = Intent(this@Carrito, SetupActivity::class.java)
            intent.putExtra("nombreProducto", nombreProducto)
            intent.putExtra("descripcionProducto", descripcionProducto)
            intent.putExtra("precioProducto", precioProducto)
            intent.putExtra("cantidadProducto", cantidadProducto)
            startActivity(intent)
            finish()
        }

        agregarMasProductos.setOnClickListener {
            // Abre la actividad HomeActivity para agregar más productos
            val intent = Intent(this@Carrito, HomeActivity::class.java)
            startActivityForResult(intent, AGREGAR_PRODUCTOS_REQUEST_CODE)
        }

        // Obtiene los datos del producto desde el intent
        val nombre = intent.getStringExtra("nombre")
        val precio = intent.getStringExtra("precio")
        val descripcion = intent.getStringExtra("descripcion")
        val imagen = intent.getIntExtra("imagen", 0)
        val cantidad = intent.getIntExtra("cantidad", 0)

        // Crea un objeto ItemCarrito con los datos del producto y la cantidad
        val itemCarrito = ItemCarrito(nombre, precio, descripcion, imagen, cantidad)

        // Agrega el objeto ItemCarrito a la lista de items de carrito
        listaCarrito.add(itemCarrito)

        // Configura el adaptador y muestra la lista de items de carrito en el RecyclerView
        val adapter = CarritoAdapter(listaCarrito as ArrayList<ItemCarrito>)
        recyclerView.adapter = adapter

        // Calcula el precio total sumando los precios de los items de carrito
        calcularPrecioTotal()

        // Actualiza el texto del precio total
        totalPrecio.text = String.format("%.3f", precioTotalId)
    }

    private fun calcularPrecioTotal() {
        // Suma los precios de todos los items de carrito
        precioTotalId = listaCarrito.sumByDouble { it.precio?.toDouble() ?: 0.0 }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AGREGAR_PRODUCTOS_REQUEST_CODE && resultCode == RESULT_OK) {
            // Obtiene los datos del producto desde el intent
            val nombre = data?.getStringExtra("nombre")
            val precio = data?.getStringExtra("precio")
            val descripcion = data?.getStringExtra("descripcion")
            val imagen = data?.getIntExtra("imagen", 0) ?: 0
            val cantidad = data?.getIntExtra("cantidad", 0) ?: 0

            // Crea un objeto ItemCarrito con los datos del producto y la cantidad
            val itemCarrito = ItemCarrito(nombre, precio, descripcion, imagen, cantidad)

            // Agrega el objeto ItemCarrito a la lista de items de carrito
            listaCarrito.add(itemCarrito)

            // Actualiza el adaptador y la vista del RecyclerView
            recyclerView.adapter?.notifyDataSetChanged()

            // Recalcula el precio total
            calcularPrecioTotal()

            // Actualiza el texto del precio total
            totalPrecio.text = String.format("%.3f", precioTotalId)
        }
    }

    companion object {
        private const val AGREGAR_PRODUCTOS_REQUEST_CODE = 1
    }
}
