package id.go.dkksemarang.bidikcovid.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import id.go.dkksemarang.bidikcovid.R
import id.go.dkksemarang.bidikcovid.ui.login.LoginUserActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        startApp()
    }

    private fun startApp() {
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, LoginUserActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000)
        overridePendingTransition(
            R.anim.splash_in_animation,
            R.anim.splash_out_animation
        )

    }

}
