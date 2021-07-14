package com.example.fitness.ui.req_diet_plan

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness.R
import com.example.fitness.databinding.ActivityReqDietBinding
import com.example.fitness.ui.req_diet_plan.adapter.AdapterReqDietPlan
import com.example.fitness.util.Utils
import com.example.fitness.util.Validations
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_req_diet.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.toolbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class ReqDietActivity : AppCompatActivity() {
    private lateinit var activity: Activity
    val viewModel: ReqDietPlanViewModel by viewModels()
    private lateinit var binding: ActivityReqDietBinding
    private lateinit var pDialog: Dialog
    private val sdf = SimpleDateFormat("HH:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        binding = DataBindingUtil.setContentView(this, R.layout.activity_req_diet)
        binding.executePendingBindings()
        binding.viewModel = viewModel
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {  finish() }
        pDialog = Utils.generateProgressDialog(activity)!!

        getScheduleDetails()
        onScheduleListShowed()

        val bottomSheetDialog = com.google.android.material.bottomsheet.BottomSheetDialog(
            this,
            R.style.MyBottomSheetDialogTheme
        )
        val bottomSheetView = LayoutInflater.from(applicationContext)
            .inflate(R.layout.fragment_bottomsheet_dialog, findViewById(R.id.root_layout))
        bottomSheetDialog.setContentView(bottomSheetView)
        val btn_add: Button = bottomSheetView.findViewById(R.id.btn_add)
        val btn_cancel: Button = bottomSheetView.findViewById(R.id.btn_cancel)
        val select_time: SingleDateAndTimePicker = bottomSheetView.findViewById(R.id.select_time)
        val edit_desc: EditText = bottomSheetView.findViewById(R.id.edit_desc)

        btn_add.setOnClickListener {
            if (!Validations.edtValidation(edit_desc, "Enter Schedule description"))
                return@setOnClickListener
            else
                pDialog.show()

            if (Utils.isDataConnected(activity)) {
                viewModel.addSchedule(
                    sdf.format(select_time.date),
                    edit_desc.text.toString()
                )
            } else {
                Utils.showSnackBarNO_INTERNAET(activity, activity.localClassName)
            }
        }
        btn_cancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        binding.btnAdd.setOnClickListener {
            bottomSheetDialog.show()
        }

        binding.fabAdd.setOnClickListener {
            bottomSheetDialog.show()
        }

        binding.fabSend.setOnClickListener {
            if (Utils.isDataConnected(activity)) {
                viewModel.sendRequest()
            } else {
                Utils.showSnackBarNO_INTERNAET(activity, activity.localClassName)
            }
        }
    }

    private fun getScheduleDetails() {
        pDialog.show()
        if (Utils.isDataConnected(activity)) {
            viewModel.getScheduleDetails()
        } else {
            Utils.showSnackBarNO_INTERNAET(activity, activity.localClassName)
        }
    }

    @SuppressLint("NewApi")
    private fun onScheduleListShowed() {

        viewModel.scheduleList.observe(this, { list ->
            if (list?.isNotEmpty() == true) {
                binding.rvDietRecords.layoutManager = LinearLayoutManager(this)
                binding.rvDietRecords.itemAnimator = DefaultItemAnimator()
                binding.rvDietRecords.setHasFixedSize(true)

                val adapterReqDietPlan = AdapterReqDietPlan(this,
                    object : AdapterReqDietPlan.MenuListener {
                        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                        override fun onOptionSelect(
                            v: View?,
                            timer: SingleDateAndTimePicker?,
                            edit_desc: EditText?,
                            id: String?,
                            img_done: ImageView,
                        ) {
                            val inflater =
                                getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                            val view: View = inflater.inflate(R.layout.menu, null)
                            val window = PopupWindow(
                                view,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                true
                            )
                            window.elevation = 15f
                            window.showAsDropDown(v, 0, 0)
                            val menu_edit = view.findViewById<LinearLayout>(R.id.menu_edit)
                            val menu_delete = view.findViewById<LinearLayout>(R.id.menu_delete)

                            menu_edit.setOnClickListener {
                                img_done.visibility = View.VISIBLE
                                fab_send.visibility = View.INVISIBLE
                                timer!!.isEnabled = true
                                edit_desc!!.isEnabled = true
                                window.dismiss()
                            }

                            menu_delete.setOnClickListener {
                                img_done.visibility = View.GONE
                                timer!!.isEnabled = false
                                edit_desc!!.isEnabled = false
                                window.dismiss()

                                Utils.customDialog(activity,
                                    "Are you sure you want to delete this record permanantly ?",
                                    "Yes",
                                    "No",
                                    { dialogInterface, i ->
                                        modifySchedule(
                                            "delete", id!!, sdf.format(timer.date),
                                            edit_desc.text.toString()
                                        )

                                        getScheduleDetails()
                                    },
                                    { dialogInterface, i -> dialogInterface.dismiss() })
                            }

                            img_done.setOnClickListener {
                                fab_send.visibility = View.VISIBLE
                                modifySchedule(
                                    "edit", id!!, sdf.format(timer?.date),
                                    edit_desc?.text.toString()
                                )

                                getScheduleDetails()
                            }
                        }
                    })
                binding.rvDietRecords.adapter = adapterReqDietPlan
                adapterReqDietPlan.refresh(list)
            }
            pDialog.dismiss()
        })
    }

    private fun modifySchedule(flag: String, id: String, date: String, desc: String) {
        pDialog.show()
        if (Utils.isDataConnected(activity)) {
            viewModel.modifySchedule(
                flag,
                id,
                date,
                desc
            )
        } else {
            Utils.showSnackBarNO_INTERNAET(
                activity,
                activity.localClassName
            )
        }
    }

}