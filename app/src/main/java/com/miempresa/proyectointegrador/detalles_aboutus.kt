package com.miempresa.proyectointegrador

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class detalles_aboutus : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_aboutus)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

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
            true
        }

        val memberName = intent.getStringExtra("memberName")
        val memberRole = intent.getStringExtra("memberRole")
        val memberPhoto = intent.getIntExtra("memberPhoto", 0)
        val memberEmail = intent.getStringExtra("memberEmail")
        val memberGitHub = intent.getStringExtra("memberGitHub")
        val memberDetails = intent.getStringExtra("memberDetails")

        val nameTextView = findViewById<TextView>(R.id.nombredeta)
        val roleTextView = findViewById<TextView>(R.id.roldeta)
        val photoImageView = findViewById<ImageView>(R.id.foto)
        val emailTextView = findViewById<TextView>(R.id.correodeta)
        val githubTextView = findViewById<TextView>(R.id.githubdeta)
        val detailsTextView = findViewById<TextView>(R.id.desdeta)

        nameTextView.text = memberName
        roleTextView.text = memberRole
        photoImageView.setImageResource(memberPhoto)
        emailTextView.text = memberEmail
        githubTextView.text = memberGitHub
        detailsTextView.text = memberDetails

        // ... resto del código ...
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
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}