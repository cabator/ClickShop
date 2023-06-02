package com.example.clickshop.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clickshop.FavsActivity
import com.example.clickshop.R
import com.example.clickshop.VerProducto
import com.example.clickshop.modelos.Producto

class ProductoAdapter(private val productosList: MutableList<Producto>) : RecyclerView.Adapter<ProductoAdapter.ViewHolder>() {

    private var filteredList: MutableList<Producto> = mutableListOf()

    // Inicializa la lista filtrada con todos los productos al crear una instancia del adaptador
    init {
        filteredList.addAll(productosList)
    }

    // Crea una instancia de ViewHolder inflando el diseño del elemento de lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return ViewHolder(vista)
    }

    // Vincula los datos del producto con los elementos de la vista del ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(filteredList[position])
    }

    // Devuelve la cantidad de elementos en la lista filtrada
    override fun getItemCount(): Int {
        return filteredList.size
    }

    // Actualiza la lista filtrada con una nueva lista de productos y notifica los cambios
    fun filterList(filteredList: MutableList<Producto>) {
        this.filteredList.clear()
        this.filteredList.addAll(filteredList)
        notifyDataSetChanged()
    }

    // Define la clase ViewHolder que contiene los elementos de la vista del producto
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val btnFav: Button = itemView.findViewById(R.id.btnFav) // Referencia al botón
        private var filteredList: MutableList<Producto> = mutableListOf()

        init {
            // Asigna un listener al botón para detectar cuando se hace clic
            btnFav.setOnClickListener {
                // Obtiene el producto correspondiente a la posición del adaptador
                val producto = filteredList[adapterPosition]
                // Crea un intento para abrir la actividad FavsActivity
                val intent = Intent(itemView.context, FavsActivity::class.java)
                // Agrega los datos del producto como extras en el intento
                intent.putExtra("nombre", producto.nombre)
                intent.putExtra("precio", producto.precio)
                intent.putExtra("descripcion", producto.descripcion)
                intent.putExtra("imagenResourceId", producto.imagenResourceId)
                itemView.context.startActivity(intent) // Inicia la actividad FavsActivity
            }
        }

        // Vincula los elementos de la vista con los datos del producto
        fun bindItems(producto: Producto) {
            val nombre = itemView.findViewById<TextView>(R.id.item_nombre)
            nombre.text = producto.nombre

            val precio = itemView.findViewById<TextView>(R.id.item_precio)
            precio.text = producto.precio

            val desc = itemView.findViewById<TextView>(R.id.item_descripcion)
            desc.text = producto.descripcion

            val imagen = itemView.findViewById<ImageView>(R.id.item_imagen)
            imagen.setImageResource(producto.imagenResourceId)
        }
    }
}
