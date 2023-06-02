package com.example.clickshop

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button

class FavsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var agregarMasProductosFavButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favs)

        // Inicializa la variable agregarMasProductosFavButton con el Button definido en el archivo XML
        agregarMasProductosFavButton = findViewById(R.id.agregarMasProductosFav)

        // Asigna un listener al Button para detectar cuando se hace clic
        agregarMasProductosFavButton.setOnClickListener {
            // Crea un intento para abrir la actividad HomeActivity
            val intent = Intent(this@FavsActivity, HomeActivity::class.java)
            startActivity(intent) // Inicia la actividad HomeActivity
        }
    }
}
