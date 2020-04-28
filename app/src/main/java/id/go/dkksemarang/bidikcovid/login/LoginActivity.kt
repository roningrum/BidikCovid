package id.go.dkksemarang.bidikcovid.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import id.go.dkksemarang.bidikcovid.R
import id.go.dkksemarang.bidikcovid.home.MainMenuActivity
import id.go.dkksemarang.bidikcovid.login.viewmodel.LoginViewModel
import id.go.dkksemarang.bidikcovid.util.SessionManager
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val sessionManager = SessionManager(applicationContext)
        loginViewModel= ViewModelProviders.of(this).get(LoginViewModel::class.java)
        loginViewModel.getLogin().observe(this, Observer {login ->
            if(login != null){
                sessionManager.saveAuthToken(login.token)
                Log.d("TokenLogin", "Your Token : ${login.token}")

            }
        })
        btn_login.setOnClickListener {
            loginViewModel.getLoginResponse()
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }

    }
}
