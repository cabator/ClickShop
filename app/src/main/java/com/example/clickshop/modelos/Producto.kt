package com.example.clickshop.modelos
import java.io.Serializable

class Producto(
    val nombre: String?,
    val precio: String?,
    val descripcion: String?,
    val imagenResourceId: Int,
    val cantidad: Int
) : Serializable
