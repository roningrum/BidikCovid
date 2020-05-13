package id.go.dkksemarang.bidikcovid.login

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import id.go.dkksemarang.bidikcovid.R
import id.go.dkksemarang.bidikcovid.home.MainMenuActivity
import id.go.dkksemarang.bidikcovid.login.viewmodel.LoginViewModel
import id.go.dkksemarang.bidikcovid.util.SessionManager
import kotlinx.android.synthetic.main.activity_login_user.*

class LoginUserActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)
        sessionManager = SessionManager(applicationContext)
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        loginViewModel.getLogin().observe(this, Observer { login ->
            if (login != null) {
                sessionManager.saveAuthToken(login.token)
                Log.d("TokenLogin", "Your Token : ${login.token}")

            }
        })

        val username = edt_username.text
        val password = edt_password.text


        btn_login_user.setOnClickListener {
            btn_login_user.isEnabled = false
            btn_login_user.text = "Loading..."



            if (TextUtils.isEmpty(username)) {
                Toast.makeText(
                    applicationContext,
                    "Username tidak boleh kosong",
                    Toast.LENGTH_SHORT
                ).show()
                btn_login_user.isEnabled = true
                btn_login_user.text = "Login"

            } else {
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(
                        applicationContext,
                        "Password tidak boleh kosong",
                        Toast.LENGTH_SHORT
                    ).show()
                    btn_login_user.isEnabled = true
                    btn_login_user.text = "Login"
                } else {
                    loginViewModel.getLoginUserResponse(username.toString(), password.toString())
                    sessionManager.saveAuthUsername(username.toString())
                    val intent = Intent(this, MainMenuActivity::class.java)
                    startActivity(intent)
                    finish()
                    Toast.makeText(applicationContext, "Berhasil Masuk", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("Username", "Your Username : ${sessionManager.fetchAuthUsername()}")
                }
            }

        }
    }

    override fun getResources(): Resources {
        return super.getResources().apply {
            configuration.fontScale = 1F
            updateConfiguration(configuration, displayMetrics)
        }
    }

    override fun onBackPressed() {
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMain)

    }
}
