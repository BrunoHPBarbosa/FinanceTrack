package com.hacksprint.financetrack

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class SplashActivity : AppCompatActivity() {

    private val splashTime: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        installSplashScreen()
        Handler().postDelayed({
            startActivity(Intent(this, Presentation::class.java))
            finish()
        }, splashTime)
    }
}
