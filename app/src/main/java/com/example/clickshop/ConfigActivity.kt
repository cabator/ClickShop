package com.example.clickshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar

class ConfigActivity : AppCompatActivity() {
    private lateinit var mCircleImageConfig: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        mCircleImageConfig = findViewById(R.id.circleimageConfig)

        // Asigna un listener al ImageView para detectar cuando se hace clic
        mCircleImageConfig.setOnClickListener {
            finish() // Finaliza la actividad actual (vuelve a la actividad anterior)
        }
    }
}
