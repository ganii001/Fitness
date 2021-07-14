package com.example.fitness.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import com.example.fitness.ConstantClass
import com.example.fitness.MyPreferenses
import com.example.fitness.R
import com.example.fitness.ui.login.LoginActivity
import com.example.fitness.ui.req_diet_plan.ReqDietActivity
import com.example.fitness.util.Utils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.edit_name


class DashboardActivity : AppCompatActivity() {

    lateinit var activity: Activity

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        activity = this
        val card_profile: CardView = findViewById(R.id.card_profile)
        val card_req_workout: CardView = findViewById(R.id.card_req_workout)
        val card_req_diet: CardView = findViewById(R.id.card_req_diet)
        val card_view_plan: CardView = findViewById(R.id.card_view_plan)
        val btn_update_profile: AppCompatButton = findViewById(R.id.btn_update_profile)

        txt_name.text =
            Utils.getDayState() + " , " + MyPreferenses(this).getString(ConstantClass.NAME, "")
        txt_weight.text =
            MyPreferenses(this).getString(ConstantClass.WEIGHT, "") + " (Weight in Kgs.)"
        txt_motive.text = MyPreferenses(this).getString(ConstantClass.MOTIVE, "") + " (Motive)"


        btn_update_profile.setOnClickListener {

        }

        card_req_workout.setOnClickListener {

        }

        card_req_diet.setOnClickListener {
            startActivity(Intent(this, ReqDietActivity::class.java))
        }

        card_view_plan.setOnClickListener {

        }

        btn_logout.setOnClickListener {
            MyPreferenses(this).clear()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }
}