package id.go.dkksemarang.bidikcovid.ui.login

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
import id.go.dkksemarang.bidikcovid.ui.home.BidikMainActivity
import id.go.dkksemarang.bidikcovid.util.SessionManager
import id.go.dkksemarang.bidikcovid.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login_user.*

class LoginUserActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)
        sessionManager = SessionManager(applicationContext)
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        observeViewModel()
        val username = edt_username.text
        val password = edt_password.text

        btn_login_user.setOnClickListener {
//            btn_login_user.isEnabled = false
//            btn_login_user.text = "Loading..."
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
                    btn_login_user.isEnabled = true
                    loginViewModel.loginUser(username.toString(), password.toString(), this)
                    sessionManager.saveAuthUsername(username.toString())
                    Log.d("Username", "Your Username : ${sessionManager.fetchAuthUsername()}")
                }
            }

        }
    }

    private fun observeViewModel() {
        loginViewModel.loadError.observe(this, Observer { isError ->
            isError.let {
                if (it) {
                    Toast.makeText(
                        this,
                        "Gagal Login. Silakan cek koneksi Internet Anda",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
        loginViewModel.loading.observe(this, Observer {
            if (it) {
                btn_login_user.text = "Loading..."
            }
        })
        loginViewModel.loadZero.observe(this, Observer {
            if (it) {
                Toast.makeText(
                    this,
                    "Username dan password salah. Silahkan cek kembali",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        loginViewModel.loginResponses.observe(this, Observer {
            it.let {
                sessionManager.saveAuthToken(it.token)
                Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, BidikMainActivity::class.java)
                startActivity(intent)
                finish()
            }

        })
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
