package com.example.clickshop

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clickshop.databinding.ActivityHomeBinding
import com.example.clickshop.modelos.ItemCarrito
import com.example.clickshop.modelos.Producto
import com.google.android.material.navigation.NavigationView
import kotlin.random.Random

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var navigationView: NavigationView
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductoAdapter
    private lateinit var binding: ActivityHomeBinding
    private lateinit var productosList: MutableList<Producto>
    private val AGREGAR_PRODUCTOS_REQUEST_CODE = 1
    private val listaCarrito = ArrayList<ItemCarrito>()

    // Generar un producto aleatorio de la lista de productos predefinidos
    // Función para generar un producto aleatorio de la lista de productos predefinidos
    private fun generarProductoAleatorio(productosPredefinidos: List<Producto>, productosGenerados: List<Producto>): Producto? {
        // Filtrar los productos predefinidos que no han sido generados aún
        val productosDisponibles = productosPredefinidos.filter { producto ->
            !productosGenerados.contains(producto)
        }
        return if (productosDisponibles.isNotEmpty()) {
            // Generar un índice aleatorio dentro del rango de los productos disponibles
            val indiceAleatorio = Random.nextInt(productosDisponibles.size)
            // Devolver el producto correspondiente al índice aleatorio
            productosDisponibles[indiceAleatorio]
        } else {
            null
        }
    }
    // Configurar el listener para la barra de búsqueda
    private fun setupSearchListener() {
        val editTextSearch = findViewById<EditText>(R.id.EditTextSearch)

        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se necesita implementar
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Filtrar la lista de productos con el texto de búsqueda
                val searchText = s.toString().toLowerCase()

                adapter.filterList { producto ->
                    producto.nombre?.toLowerCase()?.contains(searchText) ?: false
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // No se necesita implementar
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        toolbar = binding.toolbar
        navigationView = binding.navigationView
        recyclerView = binding.listaProductos

        setSupportActionBar(toolbar)

        drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)

        productosList = mutableListOf(
            Producto("Cuadros sala", "123.000", "Nuevo", R.drawable.producto1, 1),
            Producto("SkateBoard", "123.000", "Nuevo", R.drawable.producto2, 1),
            Producto("Pelota de tennis", "555.000", "Nuevo", R.drawable.producto3, 1),
            Producto("Patines negros", "305.000", "Nuevo", R.drawable.producto4, 1),
            Producto("Gorro", "223.000", "Nuevo", R.drawable.producto5, 1),
            Producto("Patones blancos", "155.000", "Nuevo", R.drawable.producto6, 1),
            Producto("Reloj Casio", "30.000", "Nuevo", R.drawable.producto7, 1),
            Producto("Reloj dorado", "23.000", "Nuevo", R.drawable.producto8, 1),
            Producto("Set de donacion", "55.000", "Nuevo", R.drawable.producto9, 1),
            Producto("Reloj Rolex", "130.000", "Nuevo", R.drawable.producto10, 1),
            Producto("Microfono sencillo", "223.000", "Nuevo", R.drawable.producto11, 1),
            Producto("Control TV", "355.000", "Nuevo", R.drawable.producto12, 1),
            Producto("Ropa", "430.000", "Nuevo", R.drawable.producto13, 1),
            Producto("Zapatillas Adidas I", "523.000", "Nuevo", R.drawable.producto14, 1),
            Producto("Zapatillas Nike I", "55.000", "Nuevo", R.drawable.producto15, 1),
            Producto("Guantes rojos", "30.000", "Nuevo", R.drawable.producto16, 1),
            Producto("Set de tecnologia", "823.000", "Nuevo", R.drawable.producto17, 1),
            Producto("Bafle 40cm", "955.000", "Nuevo", R.drawable.producto18, 1),
            Producto("Diadema Gato", "530.000", "Nuevo", R.drawable.producto19, 1),
            Producto("Control Swicht", "723.000", "Nuevo", R.drawable.producto20, 1),
            Producto("Celular Xiomi", "555.000", "Nuevo", R.drawable.producto21, 1),
            Producto("Reloj inteligente", "300.000", "Nuevo", R.drawable.producto22, 1),
            Producto("Muñeco Powerbank", "23.000", "Nuevo", R.drawable.producto23, 1),
            Producto("Ropa Vintage", "55.000", "Nuevo", R.drawable.producto24, 1),
            Producto("Bafle JBL", "380.000", "Nuevo", R.drawable.producto25, 1),
            Producto("Impresora instantania", "239.000", "Nuevo", R.drawable.producto26, 1),
            Producto("Ropa Beisbolera", "558.000", "Nuevo", R.drawable.producto27, 1),
            Producto("Wii mini", "307.000", "Nuevo", R.drawable.producto28, 1),
            Producto("Ropa Buso capotero", "234.000", "Nuevo", R.drawable.producto29, 1),
            Producto("AirPods", "551.000", "Nuevo", R.drawable.producto30, 1),
            Producto("Adidas SuperStar", "301.000", "Nuevo", R.drawable.producto31, 1),
            Producto("Buso estampado", "13.000", "Nuevo", R.drawable.producto32, 1),
            Producto("Reloj inteligente negro", "55.000", "Nuevo", R.drawable.producto33, 1),
            Producto("Adidas SuperStar blancas", "320.000", "Nuevo", R.drawable.producto34, 1),
            Producto("Gafas Vintage", "253.000", "Nuevo", R.drawable.producto35, 1),
            Producto("SkateBoarding", "585.000", "Nuevo", R.drawable.producto36, 1),
            Producto("Patines una linea", "310.000", "Nuevo", R.drawable.producto37, 1),
            Producto("Set de guantes", "283.000", "Nuevo", R.drawable.producto38, 1),
            Producto("Rolex V1", "525.000", "Nuevo", R.drawable.producto39, 1),
            Producto("Casio II", "310.000", "Nuevo", R.drawable.producto40, 1),
            Producto("Cuadro de tenis", "223.000", "Nuevo", R.drawable.producto41, 1),
            Producto("Reloj sencillo", "535.000", "Nuevo", R.drawable.producto42, 1),
            Producto("Set de gorros", "310.000", "Nuevo", R.drawable.producto43, 1),
            Producto("Reloj amarillo", "213.000", "Nuevo", R.drawable.producto44, 1),
            Producto("Gorra Vintage", "525.000", "Nuevo", R.drawable.producto45, 1),
            Producto("Cuadro Jordan", "320.000", "Nuevo", R.drawable.producto46, 1),
            Producto("PowerBank", "253.000", "Nuevo", R.drawable.producto47, 1),
            Producto("AirPods II", "55.000", "Nuevo", R.drawable.producto48, 1),
            Producto("Lapiz Tablet", "350.000", "Nuevo", R.drawable.producto49, 1),
            Producto("Iphone 11 Pro", "283.000", "Nuevo", R.drawable.producto50, 1),
            Producto("PowerBank Gris", "558.000", "Nuevo", R.drawable.producto51, 1),
            Producto("MP3", "307.000", "Nuevo", R.drawable.producto52, 1),
            Producto("PC Gamer", "283.000", "Nuevo", R.drawable.producto53, 1),
            Producto("Encendedor", "515.000", "Nuevo", R.drawable.producto54, 1),
            Producto("AirDrop", "340.000", "Nuevo", R.drawable.producto55, 1),
            Producto("CarroControl", "243.000", "Nuevo", R.drawable.producto56, 1),
            Producto("Sandalias Mario", "515.000", "Nuevo", R.drawable.producto57, 1),
            Producto("Cactus Repite", "310.000", "Nuevo", R.drawable.producto58, 1),
            Producto("Silla comedor", "123.000", "Nuevo", R.drawable.producto59, 1),
            Producto("Silla oficina", "155.000", "Nuevo", R.drawable.producto60, 1),
            Producto("Casco ICH", "130.000", "Nuevo", R.drawable.producto61, 1),
            Producto("Casco obra", "123.000", "Nuevo", R.drawable.producto62, 1),
            Producto("Guantes moto", "55.000", "Nuevo", R.drawable.producto63, 1),
            Producto("Proteccion codo", "30.000", "Nuevo", R.drawable.producto64, 1),
            Producto("Set de coleccion", "213.000", "Nuevo", R.drawable.producto65, 1),
            Producto("Reloj de mesa", "55.000", "Nuevo", R.drawable.producto66, 1),
            Producto("Dados rojos", "301.000", "Nuevo", R.drawable.producto67, 1),
            Producto("Sombrilla roja", "231.000", "Nuevo", R.drawable.producto68, 1),
            Producto("Core i7", "555.000", "Nuevo", R.drawable.producto69, 1),
            Producto("SofaCama", "309.000", "Nuevo", R.drawable.producto70, 1),
            Producto("Repisa", "233.000", "Nuevo", R.drawable.producto71, 1),
            Producto("Estatua cupido", "551.000", "Nuevo", R.drawable.producto72, 1),
            Producto("Estatua Buda", "302.000", "Nuevo", R.drawable.producto73, 1),
            Producto("Estatua arte", "233.000", "Nuevo", R.drawable.producto74, 1),
            Producto("Muñeco Sherk", "554.000", "Nuevo", R.drawable.producto75, 1),
            Producto("Muñecos Sherk", "308.000", "Nuevo", R.drawable.producto76, 1),
            Producto("Muñeca Barbie", "239.000", "Nuevo", R.drawable.producto77, 1),
            Producto("Juguete cocina", "155.000", "Nuevo", R.drawable.producto78, 1),
            Producto("Muñeco rojo", "130.000", "Nuevo", R.drawable.producto79, 1),
            Producto("Gafas de sol", "123.000", "Nuevo", R.drawable.producto80, 1),
            Producto("Conjunto de ropa", "255.000", "Nuevo", R.drawable.producto81, 1),
            Producto("Falda verde", "230.000", "Nuevo", R.drawable.producto82, 1),
            Producto("Conjunto de ropa II", "223.000", "Nuevo", R.drawable.producto83, 1),
            Producto("Buso estampado II", "355.000", "Nuevo", R.drawable.producto84, 1),
            Producto("Camisas estampadas", "330.000", "Nuevo", R.drawable.producto85, 1),
            Producto("Adidas Sport I", "323.000", "Nuevo", R.drawable.producto86, 1),
            Producto("Adidas NiceI", "555.000", "Nuevo", R.drawable.producto87, 1),
            Producto("Nike AirII", "530.000", "Nuevo", R.drawable.producto88, 1),
            Producto("Adidas Razul", "523.000", "Nuevo", R.drawable.producto89, 1),
            Producto("Zapatillas chinas", "555.000", "Nuevo", R.drawable.producto90, 1),
            Producto("Teni chino", "630.000", "Nuevo", R.drawable.producto91, 1),
            Producto("Anillos Love", "623.000", "Nuevo", R.drawable.producto92, 1),
            Producto("Reloj mecanico", "655.000", "Nuevo", R.drawable.producto93, 1),
            Producto("Lija Skate", "630.000", "Nuevo", R.drawable.producto94, 1),
            Producto("Rodamientos Skate", "623.000", "Nuevo", R.drawable.producto95, 1),
            Producto("Gorra gris", "755.000", "Nuevo", R.drawable.producto96, 1),
            Producto("Celular de juguete", "830.000", "Nuevo", R.drawable.producto97, 1),
            Producto("Balon Americano", "923.000", "Nuevo", R.drawable.producto98, 1),
            Producto("Ropa elegante", "55.000", "Nuevo", R.drawable.producto99, 1),
            Producto("Balon Basquet", "30.000", "Nuevo", R.drawable.producto100, 1),
            Producto("Sombreros PlayaW", "23.000", "Nuevo", R.drawable.producto101, 1),
            Producto("Patines Cazul", "55.000", "Nuevo", R.drawable.producto102, 1),
            Producto("Gorros tejidos", "30.000", "Nuevo", R.drawable.producto103, 1),
            Producto("Tabla Skate", "23.000", "Nuevo", R.drawable.producto104, 1),
            Producto("Adidas SportA", "55.000", "Nuevo", R.drawable.producto105, 1),
            Producto("Set de tablets", "999.000", "Nuevo", R.drawable.producto106, 1),
            Producto("Reloj Diamont", "223.000", "Nuevo", R.drawable.producto107, 1),
            Producto("Reloj playa ", "552.000", "Nuevo", R.drawable.producto108, 1),
            Producto("Ropa para tenis", "301.000", "Nuevo", R.drawable.producto109, 1),
            Producto("Reloj amarillo I", "235.000", "Nuevo", R.drawable.producto110, 1),
            Producto("Conjunto de ropa", "559.000", "Nuevo", R.drawable.producto111, 1),
            Producto("Ropa black", "309.000", "Nuevo", R.drawable.producto112, 1),
            Producto("Sombrero Big", "239.000", "Nuevo", R.drawable.producto113, 1),
            Producto("Perrito", "559.000", "Nuevo", R.drawable.producto114, 1),
            Producto("Casio Plateado", "309.000", "Nuevo", R.drawable.producto115, 1),
            Producto("Microfono reportero", "239.000", "Nuevo", R.drawable.producto116, 1),
            Producto("Nike en bota", "559.000", "Nuevo", R.drawable.producto117, 1),
            Producto("NikeSport I", "309.000", "Nuevo", R.drawable.producto118, 1),
            Producto("Set Futbol", "239.000", "Nuevo", R.drawable.producto119, 1),
            Producto("Set FutbolP", "559.000", "Nuevo", R.drawable.producto120, 1),
            Producto("Cuadro Skate", "309.000", "Nuevo", R.drawable.producto121, 1),
            Producto("Raqueta tenis", "239.000", "Nuevo", R.drawable.producto122, 1),
            Producto("Balon+Guantes", "559.000", "Nuevo", R.drawable.producto123, 1),
            Producto("Bolsos women", "309.000", "Nuevo", R.drawable.producto124, 1),
            Producto("Gorros tejidos", "239.000", "Nuevo", R.drawable.producto125, 1),
            Producto("Balones Futbol", "309.000", "Nuevo", R.drawable.producto126, 1),
            Producto("Sombreros playaMen", "309.000", "Nuevo", R.drawable.producto127, 1),
            Producto("Pelotas de tenis", "239.000", "Nuevo", R.drawable.producto128, 1),
            Producto("Reloj Black", "559.000", "Nuevo", R.drawable.producto129, 1),
            Producto("Cojunto elegante", "309.000", "Nuevo", R.drawable.producto130, 1),
            Producto("RelojBlack I", "239.000", "Nuevo", R.drawable.producto131, 1)
        )

        // Generar una lista de productos aleatorios sin duplicados
        val productosAleatorios = ArrayList<Producto>()
        val productosGenerados = mutableListOf<Producto>()
        for (i in 0 until 131) { // Cambia el número 10 según la cantidad de productos aleatorios que deseas mostrar
            val productoAleatorio = generarProductoAleatorio(productosList, productosGenerados)
            if (productoAleatorio != null) {
                productosAleatorios.add(productoAleatorio)
                productosGenerados.add(productoAleatorio)
            } else {
                break // No quedan más productos disponibles, salir del bucle
            }
        }
        // Crear el adaptador y configurar el RecyclerView
        adapter = ProductoAdapter(productosAleatorios)
        recyclerView.layoutManager = GridLayoutManager(this, 2) // Configurar GridLayoutManager con 2 columnas
        recyclerView.adapter = adapter
        // Configurar el listener para la barra de búsqueda
        setupSearchListener()
    }
    // Función para manejar los elementos seleccionados en el NavigationView
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_first_fragment -> {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                setTitle("Home")
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }
            R.id.nav_second_fragment -> {
                val intent = Intent(this, FavsActivity::class.java)
                startActivity(intent)
                setTitle("Favs")
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }
            R.id.nav_third_fragment -> {
                val intent = Intent(this, Carrito::class.java)
                intent.putExtra("productos", listaCarrito)
                startActivityForResult(intent, AGREGAR_PRODUCTOS_REQUEST_CODE)
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }
            R.id.nav_fifth_fragment -> {
                val intent = Intent(this, ConfigActivity::class.java)
                startActivity(intent)
                setTitle("Setting")
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }
            R.id.nav_sixt_fragment -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                setTitle("Exit")
                drawerLayout.closeDrawer(GravityCompat.START)
                finishAffinity()
                return true
            }
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AGREGAR_PRODUCTOS_REQUEST_CODE && resultCode == RESULT_OK) {
            val productosSeleccionados = data?.getSerializableExtra("productos") as? ArrayList<ItemCarrito>

            if (productosSeleccionados != null) {
                listaCarrito.addAll(productosSeleccionados)
            }
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    // Clase adaptador para los productos
    class ProductoAdapter(private val productos: ArrayList<Producto>) :
        RecyclerView.Adapter<ProductoAdapter.ViewHolder>() {

        private var filteredList: MutableList<Producto> = mutableListOf()

        init {
            filteredList.addAll(productos)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return filteredList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val producto = filteredList[position]
            holder.bindItems(producto)
        }
        // Función para filtrar la lista de productos
        fun filterList(filterPredicate: (Producto) -> Boolean) {
            filteredList.clear()
            filteredList.addAll(productos.filter(filterPredicate))
            notifyDataSetChanged()
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val btnFav: ImageButton = itemView.findViewById(R.id.btnFav)

            fun bindItems(producto: Producto) {
                val nombre = itemView.findViewById<TextView>(R.id.item_nombre)
                val precio = itemView.findViewById<TextView>(R.id.item_precio)
                val desc = itemView.findViewById<TextView>(R.id.item_descripcion)
                val imagen = itemView.findViewById<ImageView>(R.id.item_imagen)

                nombre.text = producto.nombre
                precio.text = producto.precio
                desc.text = producto.descripcion
                imagen.setImageResource(producto.imagenResourceId)

                btnFav.setOnClickListener {
                    val position = adapterPosition
                    val producto = filteredList[position]
                    val intent = Intent(itemView.context, FavsActivity::class.java)
                    intent.putExtra("producto", producto) // Envía el producto a FavsActivity
                    itemView.context.startActivity(intent)
                }

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, VerProducto::class.java)
                    intent.putExtra("nombre", producto.nombre)
                    intent.putExtra("precio", producto.precio)
                    intent.putExtra("descripcion", producto.descripcion)
                    intent.putExtra("imagen", producto.imagenResourceId)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}
