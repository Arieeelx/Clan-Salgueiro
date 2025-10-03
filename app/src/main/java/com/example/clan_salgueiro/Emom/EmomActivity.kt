package com.example.clan_salgueiro.Emom

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.clan_salgueiro.R
import com.example.clan_salgueiro.TemporizadorManager


class EmomActivity: AppCompatActivity() {


    // Se declaran variables para el uso del cronómetro de la app
    private lateinit var tiempoTextViewRonda: TextView
    private lateinit var tiempoTextViewTiempo: TextView
    private lateinit var barraProgreso: ProgressBar
    private lateinit var imagenPausa: ImageView
    private lateinit var temporizador: TemporizadorManager

    private var rondasRecorridas = 0
    private var rondasTotales = 0
    private var milisPorRonda: Long = 0
    private var sonidoRonda: MediaPlayer? = null
    private var sonidoCompletado: MediaPlayer? = null
    private var sonidoMitad: MediaPlayer? = null
    private var sonidoSegundosFinales: MediaPlayer? = null
    private var pausado = false

    override fun onCreate(savedInstanceState: Bundle?) {
        //Aplica para que sea en modo oscuro toda la app
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        super.onCreate(savedInstanceState)
        // Se declara que el color del statusbar sea negro
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.decorView.systemUiVisibility = 0
        setContentView(R.layout.activity_emom)

        //Implementacion del pause

        imagenPausa = findViewById(R.id.img_pausa_emom)
        tiempoTextViewTiempo = findViewById(R.id.text_temp_emom)
        tiempoTextViewRonda = findViewById(R.id.textbox1_emom)
        barraProgreso = findViewById(R.id.barraprogreso_emom)

        // Listener para pausar desde el TextView
        tiempoTextViewTiempo.setOnClickListener {
            if (!pausado) {
                temporizador.pausar()
                pausado = true
                tiempoTextViewTiempo.visibility = View.GONE
                imagenPausa.visibility = View.VISIBLE
                Toast.makeText(this, "Temporizador pausado", Toast.LENGTH_SHORT).show()
            }
        }

        imagenPausa.setOnClickListener {
            if (pausado) {
                pausado = false
                imagenPausa.visibility = View.GONE
                tiempoTextViewTiempo.visibility = View.VISIBLE

                Toast.makeText(this, "Temporizador reanudado", Toast.LENGTH_SHORT).show()
                temporizador.renaudar()
            }
        }


        //Se declara variable para el toolbar superior de poder volver atras
        val toolbarEmom = findViewById<Toolbar>(R.id.toolbar_emom)
        setSupportActionBar(toolbarEmom)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        //Se declara variables para la conexión intent de las rondas, minutos y segundos

        val rondas = intent.getIntExtra("EMOM_RONDAS", 1)
        val minutos = intent.getIntExtra("EMOM_MINUTOS", 0)
        val segundos = intent.getIntExtra("EMOM_SEGUNDOS", 0)

        sonidoRonda = MediaPlayer.create(this, R.raw.terminoronda)
        sonidoCompletado = MediaPlayer.create(this, R.raw.victoria)
        sonidoMitad = MediaPlayer.create(this, R.raw.mitad)
        sonidoSegundosFinales = MediaPlayer.create(this, R.raw.segundos)

        rondasTotales = rondas
        milisPorRonda = ((minutos * 60) + segundos) * 1000L

        tiempoTextViewRonda = findViewById(R.id.textbox1_emom)
        tiempoTextViewTiempo = findViewById(R.id.text_temp_emom)
        barraProgreso = findViewById<ProgressBar>(R.id.barraprogreso_emom)

        barraProgreso.max = milisPorRonda.toInt()
        barraProgreso.progress = 0
        rondasRecorridas = 0

        iniciarTemporizador()
    }

    private fun iniciarTemporizador() {
        if (::temporizador.isInitialized) {
            temporizador.cancelar()
        }

        temporizador = TemporizadorManager(
            context = this,
            duracion = milisPorRonda,
            sonidoMitad = sonidoMitad,
            sonidoSegundosFinales = sonidoSegundosFinales,
            sonidoRonda = sonidoRonda,
            sonidoCompletado = sonidoCompletado,

            onTick = { min, seg, progreso ->
                tiempoTextViewTiempo.text = String.format("%02d:%02d", min, seg)
                tiempoTextViewRonda.text = "Ronda ${rondasRecorridas + 1} de $rondasTotales"
                barraProgreso.progress = progreso
            },

            onNuevaRonda = {
                rondasRecorridas++
                iniciarTemporizador()
            },
            onFinalizado = {
            Toast.makeText(this, "EMOM Terminado", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, PrevEmomActivity::class.java))
            finish()
            }
        )
        temporizador.configurarRondas(rondasTotales, rondasRecorridas)
        temporizador.iniciar()
    }

    // Se aplica el procedimiento para volver atras
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    //Destruccion de audios al reproducirse para no generar cache
    override fun onDestroy() {
        super.onDestroy()
        if (::temporizador.isInitialized) {
            temporizador.cancelar()
        }
        sonidoRonda?.release()
        sonidoRonda = null
        sonidoMitad?.release()
        sonidoMitad = null
        sonidoSegundosFinales?.release()
        sonidoSegundosFinales = null
        sonidoCompletado?.release()
        sonidoCompletado = null
    }


}