package com.example.clan_salgueiro

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.os.Looper
import android.os.Handler
import androidx.core.os.postDelayed

class TemporizadorManager(
    private val context: Context,
    private val duracion: Long,
    private val sonidoMitad: MediaPlayer?,
    private val sonidoSegundosFinales: MediaPlayer?,
    private val sonidoRonda: MediaPlayer?,
    private val sonidoCompletado: MediaPlayer?,
    private val onTick: (min: Int, seg: Int, progreso: Int) -> Unit,
    private val onNuevaRonda: () -> Unit,
    private val onFinalizado: () -> Unit
) {
    private var timer: CountDownTimer? = null
    private var tiempoInicio: Long = 0L
    private var tiempoRestante: Long = duracion
    private var sonidoMitadReproducido = false
    private var ultimoSonidoSegundos = 0L
    private var rondasTotales: Int = 1
    private var rondasRecorridas: Int = 0

    fun configurarRondas(totales: Int, recorridas: Int) {
        rondasTotales = totales
        rondasRecorridas = recorridas
    }

    fun iniciar() {
        iniciarConDuracion(tiempoRestante)
    }

    private fun iniciarConDuracion(duracionPersonalizada: Long) {
        tiempoInicio = System.currentTimeMillis()
        sonidoMitadReproducido = false
        timer?.cancel()
        timer = object : CountDownTimer(duracionPersonalizada, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tiempoRestante = millisUntilFinished

                val segundos = (millisUntilFinished / 1000) % 60
                val minutos = (millisUntilFinished / 1000) / 60
                val progreso = (duracion - millisUntilFinished).toInt()
                onTick(minutos.toInt(), segundos.toInt(), progreso)

                val mitadTiempo = duracion / 2
                if (!sonidoMitadReproducido && millisUntilFinished in (mitadTiempo - 500)..(mitadTiempo + 500)) {
                    sonidoMitad?.start()
                    sonidoMitadReproducido = true
                }

                val ahora = System.currentTimeMillis()
                if (millisUntilFinished in 800..5200 && ahora - ultimoSonidoSegundos > 900) {
                    sonidoSegundosFinales?.let {
                        it.seekTo(0)
                        it.start()
                    }
                    ultimoSonidoSegundos = ahora
                }
            }

            override fun onFinish() {
                if (rondasRecorridas < rondasTotales - 1) {
                    sonidoRonda?.let {
                        it.seekTo(0)
                        it.start()
                    }
                    rondasRecorridas++
                    onNuevaRonda?.invoke()
                } else {
                    val sonido = sonidoCompletado
                    if (sonido != null) {
                        sonido.seekTo(0)
                        sonido.start()

                        // Usamos un fallback de 2000 ms si la duración no está lista
                        val duracionSegura = try {
                            val d = sonido.duration
                            if (d > 0) d.toLong() else 2000L
                        } catch (e: Exception) {
                            2000L
                        }
                        
                        Handler(Looper.getMainLooper()).postDelayed({
                            onFinalizado?.invoke()
                        }, duracionSegura)
                    } else {
                        onFinalizado?.invoke()
                    }
                }
            }
        }.start()
    }

    fun pausar() {
        timer?.cancel()
    }

    fun renaudar() {
        iniciarConDuracion(tiempoRestante)
    }

    fun cancelar() {
        timer?.cancel()
    }
}
