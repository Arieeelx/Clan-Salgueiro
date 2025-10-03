package com.example.clan_salgueiro.Emom

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.clan_salgueiro.R

class PrevEmomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //SE MANTIENE ACTIVITY PREDETERMINADO MODO OSCURO
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)

        //STATUS BAR EN NEGRO
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.decorView.systemUiVisibility = 0

        //ACTIVIDAD A MOSTRAR
        setContentView(R.layout.activity_emom_prev)

        //SE CONFIGURA TOOLBAR PARA PODER RETROCEDER DE PAG
        val toolbarEmom = findViewById<Toolbar>(R.id.toolbar_emom_prev)
        setSupportActionBar(toolbarEmom)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        //SE DECLARAN VARIABLES DE BOTON Y NUMBER PICKER Y SU VALOR
        val botonEmomPrev = findViewById<Button>(R.id.bottom1_emom_prev)
        val rondasPicker = findViewById<NumberPicker>(R.id.numpickrondas_emom_prev)
        val minutosPicker = findViewById<NumberPicker>(R.id.numpickminutos_emom_prev)
        val segundosPicker = findViewById<NumberPicker>(R.id.numpicksegundos_emom_prev)

        rondasPicker.minValue = 1
        rondasPicker.maxValue = 15
        minutosPicker.minValue = 0
        minutosPicker.maxValue = 60
        segundosPicker.minValue = 0
        segundosPicker.maxValue = 60

        //SE CONFIGURA FUNCION DE BOTON Y EL GUARDADO DE LOS RESPECTIVO VALORES
        botonEmomPrev.setOnClickListener {
            val rondas = rondasPicker.value
            val minutos = minutosPicker.value
            val segundos = segundosPicker.value
            val intent = Intent(this, EmomActivity::class.java)
            intent.putExtra("EMOM_RONDAS", rondas)
            intent.putExtra("EMOM_MINUTOS", minutos)
            intent.putExtra("EMOM_SEGUNDOS", segundos)
            startActivity(intent)
        }
    }

    //FUNCION PARA VOLVER ATRAS
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



}