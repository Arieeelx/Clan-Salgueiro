package com.example.clan_salgueiro.Emom

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.clan_salgueiro.R


class EmomActivity: AppCompatActivity() {


    // Se declaran variables para el uso del cronómetro de la app
    private lateinit var tiempoTextViewRonda: TextView
    private lateinit var tiempoTextViewTiempo: TextView
    private lateinit var barraProgreso: ProgressBar

    private var rondasRecorridas = 0
    private var rondasTotales = 0
    private var milisPorRonda: Long = 0
    private var cuentaRegresiva: CountDownTimer? = null
    private var sonidoRonda: MediaPlayer? = null
    private var sonidoCompletado: MediaPlayer? = null

    private var sonidoMitad: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Se declara que el color del statusbar sea negro
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.decorView.systemUiVisibility = 0
        setContentView(R.layout.activity_emom)

        //Se declara variable para el toolbar superior de poder volver atras
        val toolbarEmom = findViewById<Toolbar>(R.id.toolbar_emom)
        setSupportActionBar(toolbarEmom)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        //Se declara variables para la conexión intent de las rondas, minutos y segundos

        val rondas = intent.getIntExtra("EMOM_RONDAS", 1)
        val minutos = intent.getIntExtra("EMOM_MINUTOS", 0)
        val segundos = intent.getIntExtra("EMOM_SEGUNDOS", 0)

        sonidoRonda = MediaPlayer.create(this, R.raw.alarm1)
        sonidoCompletado = MediaPlayer.create(this, R.raw.victoria)
        sonidoMitad = MediaPlayer.create(this, R.raw.mitad)

        rondasTotales = rondas
        milisPorRonda = ((minutos * 60) + segundos) * 1000L

        tiempoTextViewRonda = findViewById(R.id.textbox1_emom)
        tiempoTextViewTiempo = findViewById(R.id.text_temp_emom)
        barraProgreso = findViewById<ProgressBar>(R.id.barraprogreso_emom)

        barraProgreso.max = milisPorRonda.toInt()
        barraProgreso.progress = 0

        startEmomRound()

    }

    private fun startEmomRound() {
        if (rondasRecorridas >= rondasTotales) {
            sonidoCompletado?.start()
            Toast.makeText(this, "EMOM Terminado", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PrevEmomActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        rondasRecorridas++

        cuentaRegresiva?.cancel()

        cuentaRegresiva = object : CountDownTimer(milisPorRonda, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val progreso = (milisPorRonda - millisUntilFinished).toInt()
                barraProgreso.progress = progreso

                //Deteccion de tiempos
                val segundosFaltantes = (millisUntilFinished / 1000) % 60
                val minutosFaltantes = (millisUntilFinished / 1000) / 60
                tiempoTextViewRonda.text = String.format("Ronda %d de %d", rondasRecorridas, rondasTotales)
                tiempoTextViewTiempo.text = String.format("%02d:%02d", minutosFaltantes, segundosFaltantes)

                // Se agrega deteccion de mitad del tiempo
                val mitadTiempo = milisPorRonda / 2
                if (millisUntilFinished in (mitadTiempo - 500)..(mitadTiempo + 500)) {
                    sonidoMitad?.start()

                }
            }

            override fun onFinish() {
                barraProgreso.progress = barraProgreso.max
                sonidoRonda?.start()
                startEmomRound()
            }
        }.start()
    }

    // Se aplica el procedimiento para volver atras
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        cuentaRegresiva?.cancel()
        sonidoRonda?.release()
        sonidoRonda = null
        sonidoMitad?.release()
        sonidoMitad = null
    }

}