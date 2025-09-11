package com.example.clan_salgueiro

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.clan_salgueiro.MainActivity
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.clan_salgueiro.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.decorView.systemUiVisibility = 0
        setContentView(R.layout.splash_scaled)

        val imageView = findViewById<ImageView>(R.id.splashImageView)
        val animationDrawable = imageView.drawable as AnimationDrawable
        animationDrawable.start()

        imageView.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000)
    }
}