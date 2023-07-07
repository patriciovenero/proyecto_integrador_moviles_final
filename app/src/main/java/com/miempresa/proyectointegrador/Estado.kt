package com.miempresa.proyectointegrador

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import org.json.JSONArray
import org.json.JSONException

class Estado : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estado)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        listView = findViewById(R.id.list_view)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_ayuda -> {
                    val intent = Intent(this, sobrenosotros1::class.java)
                    startActivity(intent)
                }
                R.id.nav_team -> {
                    val intent = Intent(this, teammanager::class.java)
                    startActivity(intent)
                }
                R.id.nav_empresa -> {
                    val intent = Intent(this, Empresa::class.java)
                    startActivity(intent)
                }
                R.id.nav_estacionamiento -> {
                    val intent = Intent(this, Estacionamiento::class.java)
                    startActivity(intent)
                }
                R.id.nav_estado -> {
                    val intent = Intent(this, Estado::class.java)
                    startActivity(intent)
                }
                R.id.nav_miperfil -> {
                    val intent = Intent(this, miperfil::class.java)
                    startActivity(intent)
                }
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_logout -> {
                    val intent = Intent(this, loginb::class.java)

                    // Limpiar las preferencias de usuario
                    val sharedPreferences = getSharedPreferences("my_app_prefs", Context.MODE_PRIVATE)
                    sharedPreferences.edit().clear().apply()

                    // Cerrar todas las actividades existentes y comenzar la actividad de inicio de sesión
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                    Toast.makeText(this, "Cierre de sesión correctamente", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    finish()
                }
            }

            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Realizar solicitud a la API utilizando Volley
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val urlAPI = getString(R.string.urlapi)
        val urlEstado = "$urlAPI/estado"
        val urlEstacionamiento = "$urlAPI/estacionamiento"

        val jsonArrayRequestEstado = JsonArrayRequest(
            Request.Method.GET, urlEstado, null,
            Response.Listener { response ->
                // Procesar la respuesta JSON de estados
                val estadoItems = parseEstadoItems(response)

                // Obtener la lista de estacionamientos
                val jsonArrayRequestEstacionamiento = JsonArrayRequest(
                    Request.Method.GET, urlEstacionamiento, null,
                    Response.Listener { response ->
                        // Procesar la respuesta JSON de estacionamientos
                        val estacionamientoItems = parseEstacionamientoItems(response)

                        // Crear un adaptador personalizado y asignar la lista de elementos
                        val adapter = EstadoListAdapter(this, R.layout.es_list_item, estadoItems, estacionamientoItems)

                        // Asignar el adaptador al ListView
                        listView.adapter = adapter
                    },
                    Response.ErrorListener { error ->
                        // Manejar errores de la solicitud de estacionamientos
                    }
                )

                // Agregar la solicitud de estacionamientos a la cola de solicitudes
                requestQueue.add(jsonArrayRequestEstacionamiento)
            },
            Response.ErrorListener { error ->
                // Manejar errores de la solicitud de estados
            }
        )

        // Agregar la solicitud de estados a la cola de solicitudes
        requestQueue.add(jsonArrayRequestEstado)
    }

    private fun parseEstadoItems(response: JSONArray): List<EstadoItem> {
        val estadoItems = mutableListOf<EstadoItem>()

        try {
            for (i in 0 until response.length()) {
                val jsonObject = response.getJSONObject(i)
                val id = jsonObject.getInt("id")
                val horaIngreso = jsonObject.getString("hora_ingreso")
                val horaSalida = jsonObject.getString("hora_salida")
                val idEstacionamiento = jsonObject.getInt("id_estacionamiento")

                val estadoItem = EstadoItem(id, horaIngreso, horaSalida, idEstacionamiento)
                estadoItems.add(estadoItem)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return estadoItems
    }

    private fun parseEstacionamientoItems(response: JSONArray): List<EstacionamientoItem> {
        val estacionamientoItems = mutableListOf<EstacionamientoItem>()

        try {
            for (i in 0 until response.length()) {
                val jsonObject = response.getJSONObject(i)
                val id = jsonObject.getInt("id")
                val lugar = jsonObject.getString("lugar")
                val estado = jsonObject.getBoolean("estado")
                val idEmpresa = jsonObject.getInt("id_empresa")

                val estacionamientoItem = EstacionamientoItem(id, lugar, estado, idEmpresa)
                estacionamientoItems.add(estacionamientoItem)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return estacionamientoItems
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}