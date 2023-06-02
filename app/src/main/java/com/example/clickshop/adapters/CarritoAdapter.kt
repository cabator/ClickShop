package com.example.clickshop.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clickshop.R
import com.example.clickshop.VerProducto
import com.example.clickshop.modelos.ItemCarrito

class CarritoAdapter(private val productos: ArrayList<ItemCarrito>) : RecyclerView.Adapter<CarritoAdapter.ViewHolder>() {

    // Crea una instancia de ViewHolder inflando el diseño del elemento de lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_carrito, parent, false)
        return ViewHolder(vista)
    }

    // Vincula los datos del producto con los elementos de la vista del ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(productos[position])

        // Asigna un listener al elemento de la vista para detectar cuando se hace clic
        holder.itemView.setOnClickListener {
            // Crea un intento para abrir la actividad VerProducto
            val intent = Intent(holder.itemView.context, VerProducto::class.java)
            // Agrega datos adicionales al intento, como el nombre, precio e imagen del producto
            intent.putExtra("nombre", productos[position].nombre)
            intent.putExtra("precio", productos[position].precio)
            intent.putExtra("imagen", productos[position].imagenResId)
            holder.itemView.context.startActivity(intent) // Inicia la actividad VerProducto
        }
    }

    // Devuelve la cantidad de elementos en la lista de productos
    override fun getItemCount(): Int {
        return productos.size
    }

    // Define la clase ViewHolder que contiene los elementos de la vista del producto en el carrito
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Vincula los elementos de la vista con los datos del producto
        fun bindItems(producto: ItemCarrito) {
            val nombre = itemView.findViewById<TextView>(R.id.nombreCarrito)
            nombre.text = producto.nombre

            val precio = itemView.findViewById<TextView>(R.id.precioCarrito)
            precio.text = producto.precio

            val desc = itemView.findViewById<TextView>(R.id.descripcionCarrito)
            desc.text = producto.descripcion

            val imagen = itemView.findViewById<ImageView>(R.id.item_imagen)
            imagen.setImageResource(producto.imagenResId)
        }
    }
}
