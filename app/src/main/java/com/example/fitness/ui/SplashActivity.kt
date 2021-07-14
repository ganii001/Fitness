package com.example.fitness.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil

import androidx.lifecycle.ViewModelProvider
import com.example.fitness.ConstantClass
import com.example.fitness.MyPreferenses
import com.example.fitness.R
import com.example.fitness.databinding.ActivityMainBinding
import com.example.fitness.ui.login.LoginActivity
import com.example.fitness.ui.login.LoginViewModel
import com.example.fitness.util.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    lateinit var activity: Activity
    val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.executePendingBindings()
        binding.shimmerViewContainer.startShimmer()

        onLoggedIn()

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            if (Utils.isDataConnected(activity)) {
                MyPreferenses(activity).getString(ConstantClass.PASSWORD, "")?.let {
                    MyPreferenses(activity).getString(ConstantClass.USERNAME, "")?.let { it1 ->
                        viewModel.onLogin(it1, it)
                    }
                }
            } else {
                Utils.showSnackBarNO_INTERNAET(activity, activity.localClassName)
            }
        }, 1000)
    }

    private fun onLoggedIn() {
        viewModel.isLoginSuccessful.observe(this, androidx.lifecycle.Observer { isLogSuccess ->
            if (isLogSuccess) {
                startActivity(Intent(activity, DashboardActivity::class.java))
                finish()
            } else {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                    binding.imgLogo,
                    "img")
                startActivity(Intent(activity, LoginActivity::class.java), options.toBundle())
                finish()
            }
            binding.shimmerViewContainer.stopShimmer()
        })
    }
}