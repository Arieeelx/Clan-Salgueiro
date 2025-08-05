package com.example.clan_salgueiro

import android.content.Intent
import androidx.core.content.ContextCompat
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.clan_salgueiro.Amrap.AmrapActivity
import com.example.clan_salgueiro.Bilbo.BilboActivity
import com.example.clan_salgueiro.Emom.EmomActivity
import com.example.clan_salgueiro.Emom.PrevEmomActivity
import com.example.clan_salgueiro.Tabata.TabataActivity
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    // Se declaran variables para que queden inicializadas antes del proceso del menu
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Se declara que el color del statusbar sea negro
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.decorView.systemUiVisibility = 0
        setContentView(R.layout.activity_menu)

        //Se declara variable para el menu lateral
        val toolbarMenu = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_menu)
        setSupportActionBar(toolbarMenu)
        drawerLayout = findViewById(R.id.menu)
        val navegadorMenu =findViewById<NavigationView>(R.id.navigation_view)

        //Se aplica el funcionamiento del menu lateral
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbarMenu,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //Especificacion para cada ruta del menu
        navegadorMenu.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_1 -> {
                   //VA LO QUE TRABAJA EL MENU
                }

                R.id.nav_6 -> {
                    //VA LO QUE TRABAJA EL MENU
            }
        }
            drawerLayout.closeDrawers()
            true
        }

        //Se declaran variables para los botones
        val botonBILBO = findViewById<Button>(R.id.bottom1_menu)
        val botonTABATA = findViewById<Button>(R.id.bottom2_menu)
        val botonEMOM = findViewById<Button>(R.id.bottom3_menu)
        val botonAMRAP = findViewById<Button>(R.id.bottom4_menu)

        //Se aplica la direccion
        botonBILBO.setOnClickListener {
            val intent = Intent(this, BilboActivity::class.java)
            startActivity(intent)
        }
        botonTABATA.setOnClickListener {
            val intent = Intent(this, TabataActivity::class.java)
            startActivity(intent)
        }
        botonEMOM.setOnClickListener {
            val intent = Intent(this, PrevEmomActivity::class.java)
            startActivity(intent)
        }
        botonAMRAP.setOnClickListener {
            val intent = Intent(this, AmrapActivity::class.java)
            startActivity(intent)
        }

    }
}
