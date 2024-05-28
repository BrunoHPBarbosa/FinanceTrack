package com.hacksprint.financetrack

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView

// Classe responsável por exibir a tela de splash ao iniciar a aplicação
class Splash : AppCompatActivity() {

    // Define o tempo de exibição antes de iniciar a tela FrameInicial
    private val splashTime: Long = 300

    // Define a velocidade de reprodução do Lottie
    private val lottieSpeed: Float = 0.6f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash) // Define o layout a ser exibido durante a tela de splash

        // Obtém a referência do componente LottieAnimationView do layout
        val lottie: LottieAnimationView = findViewById(R.id.lottie)

        // Define a animação a ser exibida no componente LottieAnimationView
        lottie.setAnimation(R.raw.logo)

        // Define a velocidade de reprodução da animação do Lottie
        lottie.speed = lottieSpeed

        // Adiciona um ouvinte para detectar o término da animação do Lottie
        lottie.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                // Método chamado quando a animação do Lottie começa
            }

            override fun onAnimationEnd(animation: Animator) {
                // Método chamado quando a animação do Lottie termina

                // Cria um Handler para executar uma ação após um intervalo de tempo
                Handler(Looper.getMainLooper()).postDelayed({
                    // Cria um Intent para iniciar a próxima atividade após o término da animação
                    val intent = Intent(this@Splash, Presentation::class.java)
                    startActivity(intent) // Inicia a próxima atividade
                    finish() // Finaliza a atividade atual (tela de splash)
                }, splashTime) // Define o tempo de espera antes de iniciar a próxima atividade
            }

            override fun onAnimationCancel(animation: Animator) {
                // Método chamado quando a animação do Lottie é cancelada
            }

            override fun onAnimationRepeat(animation: Animator) {
                // Método chamado quando a animação do Lottie é repetida
            }
        })
    }
}
