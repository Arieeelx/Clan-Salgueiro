package com.example.clan_salgueiro.Amrap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.clan_salgueiro.R

class AmrapActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Se declara que el color del statusbar sea negro
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.decorView.systemUiVisibility = 0
        setContentView(R.layout.activity_amrap)

        //Se declara variable para el toolbar superior de poder volver atras
        val toolbarAmrap = findViewById<Toolbar>(R.id.toolbar_amrap)
        setSupportActionBar(toolbarAmrap)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    // Se aplica el procedimiento para volver atras
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}