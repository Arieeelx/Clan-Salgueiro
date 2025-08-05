package com.example.clan_salgueiro.Emom

import android.content.IntentSender
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

    private lateinit var tiempoTextViewRonda: TextView
    private lateinit var tiempoTextViewTiempo: TextView
    private lateinit var barraProgreso: ProgressBar

    private var rondasRecorridas = 0
    private var rondasTotales = 0
    private var milisPorRonda: Long = 0
    private var cuentaRegresiva: CountDownTimer? = null

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

        val rondas = intent.getIntExtra("EMOM_RONDAS", 1)
        val minutos = intent.getIntExtra("EMOM_MINUTOS", 0)
        val segundos = intent.getIntExtra("EMOM_SEGUNDOS", 0)


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
            Toast.makeText(this, "EMOM Terminado", Toast.LENGTH_SHORT).show()
            return
        }

        rondasRecorridas++

        cuentaRegresiva?.cancel()

        cuentaRegresiva = object : CountDownTimer(milisPorRonda, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val progreso = (milisPorRonda - millisUntilFinished).toInt()
                barraProgreso.progress = progreso

                val segundosFaltantes = (millisUntilFinished / 1000) % 60
                val minutosFaltantes = (millisUntilFinished / 1000) / 60
                tiempoTextViewRonda.text = String.format("Ronda %d de %d", rondasRecorridas, rondasTotales)
                tiempoTextViewTiempo.text = String.format("%02d:%02d", minutosFaltantes, segundosFaltantes)
            }

            override fun onFinish() {
                barraProgreso.progress = barraProgreso.max
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
    }

}