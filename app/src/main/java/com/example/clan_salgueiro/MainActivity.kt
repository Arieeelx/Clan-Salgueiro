package com.example.clan_salgueiro

import androidx.core.content.ContextCompat
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.decorView.systemUiVisibility = 0
        setContentView(R.layout.activity_menu)

        val toolbar =findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.main)
        val navegadorMenu =findViewById<NavigationView>(R.id.navigation_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

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
    }
}
