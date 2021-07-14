package com.example.fitness.ui.login

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fitness.R
import com.example.fitness.databinding.ActivityLoginBinding

import com.example.fitness.ui.DashboardActivity
import com.example.fitness.ui.signup.SignUpActivity
import com.example.fitness.util.Utils
import com.example.fitness.util.Validations
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var activity: Activity
    val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var pDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.executePendingBindings()
        pDialog = Utils.generateProgressDialog(activity)!!

        onLoggedIn()

        binding.editEmail.afterTextChanged {
            Validations.emailValidation(edit_email)
        }

        binding.editPass.afterTextChanged {
            Validations.edtValidation(edit_pass, "Enter Password")
        }

        binding.btnLogin.setOnClickListener {
            if (!Validations.emailValidation(edit_email))
                return@setOnClickListener
            else if (!Validations.edtValidation(edit_pass, "Enter Password"))
                return@setOnClickListener
            else
                loginUser()
        }

        binding.txtSignup.setOnClickListener {
            startActivity(Intent(activity, SignUpActivity::class.java))
        }
    }

    private fun onLoggedIn() {
        viewModel.isLoginSuccessful.observe(this, Observer { isLogSuccess ->
            if (isLogSuccess) {
                startActivity(Intent(activity, DashboardActivity::class.java))
                finish()
            }
            pDialog.dismiss()
        })
    }

    private fun loginUser() {
        if (Utils.isDataConnected(activity)) {
            pDialog.show()
            viewModel.onLogin(binding.editEmail.text.toString(), binding.editPass.text.toString())
        } else {
            Utils.showSnackBarNO_INTERNAET(activity, activity.localClassName)
        }
    }
}

/* Extension function to simplify setting an afterTextChanged action to EditText components.
*/
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}


