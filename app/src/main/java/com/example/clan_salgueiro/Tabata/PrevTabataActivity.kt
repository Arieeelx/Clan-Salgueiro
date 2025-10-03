package com.example.clan_salgueiro.Tabata

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.example.clan_salgueiro.R

class PrevTabataActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //SE MANTIENE ACTIVITY PREDETERMINADO MODO OSCURO
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)

        //STATUS BAR EN NEGRO
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.decorView.systemUiVisibility = 0

        //ACTIVIDAD A MOSTRAR
        setContentView(R.layout.activity_tabata_prev)

        //SE CONFIGURA TOOLBAR PARA PODER RETROCEDER DE PAG
        val toolbarTabata = findViewById<Toolbar>(R.id.toolbar_tabata_prev)
        setSupportActionBar(toolbarTabata)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        //SE DECLARAN VARIABLES DE BOTON Y NUMBER PICKER Y SU VALOR
        val botonTabataPrev = findViewById<Button>(R.id.bottom1_tabata_prev)
        val rondasPicker = findViewById<NumberPicker>(R.id.numpickrondas_tabata_prev)
        val segundosTrabajoPicker = findViewById<NumberPicker>(R.id.numpicksegundos_trabajo_tabata_prev)
        val segundosDescansoPicker = findViewById<NumberPicker>(R.id.numpicksegundos_descanso_tabata_prev)

        rondasPicker.minValue = 1
        rondasPicker.maxValue = 20
        segundosTrabajoPicker.minValue = 0
        segundosTrabajoPicker.maxValue = 60
        segundosDescansoPicker.minValue = 0
        segundosDescansoPicker.maxValue = 60

        //SE CONFIGURA FUNCION DE BOTON Y EL GUARDADO DE LOS RESPECTIVO VALORES
        botonTabataPrev.setOnClickListener {
            val rondas = rondasPicker.value
            val segundosTrabajo = segundosTrabajoPicker.value
            val segundosDescanso = segundosDescansoPicker.value
            val intent = Intent(this, TabataActivity::class.java)
            intent.putExtra("TABATA_RONDAS", rondas)
            intent.putExtra("SEGUNDOS_TRABAJO", segundosTrabajo)
            intent.putExtra("SEGUNDOS_DESCANSO", segundosDescanso)
            startActivity(intent)
        }
    }

    //FUNCION PARA VOLVER ATRAS
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}