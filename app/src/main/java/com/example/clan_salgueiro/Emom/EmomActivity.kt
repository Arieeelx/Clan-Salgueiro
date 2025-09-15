package com.example.clan_salgueiro.Emom

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.clan_salgueiro.R


class EmomActivity: AppCompatActivity() {


    // Se declaran variables para el uso del cronómetro de la app
    private lateinit var tiempoTextViewRonda: TextView
    private lateinit var tiempoTextViewTiempo: TextView
    private lateinit var barraProgreso: ProgressBar

    private lateinit var imagenPausa: ImageView

    private var rondasRecorridas = 0
    private var rondasTotales = 0
    private var milisPorRonda: Long = 0
    private var cuentaRegresiva: CountDownTimer? = null
    private var sonidoRonda: MediaPlayer? = null
    private var sonidoCompletado: MediaPlayer? = null
    private var sonidoMitad: MediaPlayer? = null

    private var sonidoMitadReproducido = false
    private var sonidoSegundosFinales: MediaPlayer? = null

    private var ultimoSonidoSegundos: Long = 0L

    private var pausado = false
    private var tiempoRestante: Long = 0

    private var tiempoInicio: Long = 0L

    private var duracionActual: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
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
                cuentaRegresiva?.cancel()
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

                if (tiempoRestante > 1000) {
                    reanudarTemporizador(tiempoRestante)
                    Toast.makeText(this, "Temporizador reanudado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Tiempo agotado", Toast.LENGTH_SHORT).show()
                    startEmomRound()
                }
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

        startEmomRound()

    }

    private fun startEmomRound(tiempoInicial: Long = milisPorRonda, esRenaudacion: Boolean = false) {

        tiempoInicio = System.currentTimeMillis()
        duracionActual = tiempoInicial

        sonidoMitadReproducido = false

        if (rondasRecorridas >= rondasTotales) {
            sonidoCompletado?.start()
            Toast.makeText(this, "EMOM Terminado", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PrevEmomActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        if (!esRenaudacion) {
            rondasRecorridas++
        }

        cuentaRegresiva?.cancel()

        cuentaRegresiva = object : CountDownTimer(tiempoInicial, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val progreso = (milisPorRonda - millisUntilFinished).toInt()
                barraProgreso.progress = progreso

                //Deteccion de tiempos

                tiempoRestante = duracionActual - (System.currentTimeMillis() - tiempoInicio)


                val segundosFaltantes = (millisUntilFinished / 1000) % 60
                val minutosFaltantes = (millisUntilFinished / 1000) / 60

                tiempoTextViewRonda.text = String.format("Ronda %d de %d", rondasRecorridas, rondasTotales)
                tiempoTextViewTiempo.text = String.format("%02d:%02d", minutosFaltantes, segundosFaltantes)

                // Se agrega deteccion de mitad del tiempo
                val mitadTiempo = milisPorRonda / 2
                if (!sonidoMitadReproducido && millisUntilFinished in (mitadTiempo - 500)..(mitadTiempo + 500)) {
                    sonidoMitad?.start()
                    sonidoMitadReproducido = true
                }

                //Deteccion de los segundos finales
                if (millisUntilFinished in 800..3200) {
                    sonidoSegundosFinales?.let {
                        if (it.isPlaying) it.seekTo(0)
                        it.start()
                    }
                }

            }

                override fun onFinish() {
                barraProgreso.progress = barraProgreso.max

                // Solo reproducir sonido de ronda si NO es la ultima
                if (rondasRecorridas < rondasTotales) {
                    sonidoRonda?.start()
                }
                startEmomRound()
            }

        }.start()
    }

    private fun reanudarTemporizador(tiempo: Long) {
        tiempoInicio = System.currentTimeMillis()
        duracionActual = tiempo

        sonidoMitadReproducido = false
        ultimoSonidoSegundos = 0L

        cuentaRegresiva?.cancel()

        cuentaRegresiva = object : CountDownTimer(tiempo, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tiempoRestante = duracionActual - (System.currentTimeMillis() - tiempoInicio)

                val progreso = (milisPorRonda - tiempoRestante).toInt()
                barraProgreso.progress = progreso

                val segundosFaltantes = (tiempoRestante / 1000) % 60
                val minutosFaltantes = (tiempoRestante / 1000) / 60

                tiempoTextViewRonda.text = String.format("Ronda %d de %d", rondasRecorridas, rondasTotales)
                tiempoTextViewTiempo.text = String.format("%02d:%02d", minutosFaltantes, segundosFaltantes)

                //Deteccion de mitad del tiempo
                val mitadTiempo = milisPorRonda / 2
                if (!sonidoMitadReproducido && millisUntilFinished in (mitadTiempo - 500)..(mitadTiempo + 500)) {
                    sonidoMitad?.start()
                    sonidoMitadReproducido = true
                }

                //Deteccion de los segundos finales
                val ahora = System.currentTimeMillis()
                if (millisUntilFinished in 800..3200 && ahora - ultimoSonidoSegundos > 1000) {
                    sonidoSegundosFinales?.let {
                        if (it.isPlaying) it.seekTo(0)
                        it.start()
                        ultimoSonidoSegundos = ahora
                    }
                }



            }

            override fun onFinish() {
                barraProgreso.progress = barraProgreso.max

                if (rondasRecorridas < rondasTotales) {
                    sonidoRonda?.start()
                }

                startEmomRound()
            }
        }.start()
    }


    // Se aplica el procedimiento para volver atras
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    //Destruccion de audios al reproducirse para no generar cache
    override fun onDestroy() {
        super.onDestroy()
        cuentaRegresiva?.cancel()
        sonidoRonda?.release()
        sonidoRonda = null
        sonidoMitad?.release()
        sonidoMitad = null
        sonidoSegundosFinales?.release()
        sonidoSegundosFinales = null
    }

}