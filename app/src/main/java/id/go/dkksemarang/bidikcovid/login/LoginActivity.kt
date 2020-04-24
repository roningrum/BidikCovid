package id.go.dkksemarang.bidikcovid.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import id.go.dkksemarang.bidikcovid.R
import id.go.dkksemarang.bidikcovid.home.MainMenuActivity
import id.go.dkksemarang.bidikcovid.login.model.LoginResponse
import id.go.dkksemarang.bidikcovid.login.viewmodel.LoginViewModel
import id.go.dkksemarang.bidikcovid.util.SessionManager
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginViewModel= ViewModelProviders.of(this).get(LoginViewModel::class.java)
        loginViewModel.getLogin().observe(this, Observer {login ->
            if(login != null){
                accessLogin(login)
            }
        })
        loginViewModel.getLoginResponse()
    }

    private fun accessLogin(login: LoginResponse) {
        btn_login.setOnClickListener {view->
            val sessionManager = SessionManager(view.context)
            sessionManager.saveAuthToken(login.token)
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
            Log.d("Token", "Your Token : ${login.token}")
        }
    }
}
