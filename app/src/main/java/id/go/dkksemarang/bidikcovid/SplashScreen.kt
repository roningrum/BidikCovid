package id.go.dkksemarang.bidikcovid

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import id.go.dkksemarang.bidikcovid.login.LoginActivity

class SplashScreen : AppCompatActivity() {
    private lateinit var pbLoading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        pbLoading = findViewById(R.id.pbloading_screen)
        pbLoading.visibility = View.VISIBLE
        Thread(Runnable {
            doLoading()
            startApp()
            finish()
        }).start()
    }

    private fun startApp() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun doLoading() {
            var progress = 0
            while (progress < 100) {
                try {
                    Thread.sleep(1000)
                    pbLoading.progress = progress
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                progress += 10
            }
    }
}
