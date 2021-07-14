package com.example.fitness.ui.req_diet_plan

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitness.ConstantClass
import com.example.fitness.MyPreferenses
import com.example.fitness.R
import com.example.fitness.network.api.repository.ApiRepository
import com.example.fitness.network.responses.LoginResponse
import com.example.fitness.ui.req_diet_plan.adapter.AdapterReqDietPlan
import com.example.fitness.util.Utils
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.android.synthetic.main.activity_req_diet.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.text.SimpleDateFormat

class ReqDietPlanViewModel @ViewModelInject constructor(
    @ApplicationContext val context: Context,
    private val apiRepository: ApiRepository,
) : ViewModel() {

    /*private val reqDietRepository: ReqDietRepository =
        ReqDietRepository(context, apiService, this)*/
    val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isModificationSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isReqSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val scheduleList = MutableLiveData<List<LoginResponse>>()

    val isVisible: ObservableInt = ObservableInt(View.VISIBLE)
    val isVisibleLable: ObservableInt = ObservableInt(View.VISIBLE)
    val isEnable: ObservableBoolean = ObservableBoolean(true)

    fun addSchedule(time: String, schedule_desc: String) {
        viewModelScope.launch {
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id",
                    MyPreferenses(context).getString(ConstantClass.USER_ID, "")!!)
                .addFormDataPart("time", time)
                .addFormDataPart("schedule_desc", schedule_desc)
                .build()
            apiRepository.addSchedule(requestBody).let {
                DataState.loading(false)
                if (it.isSuccessful) {
                    if (it.body()?.success.equals("0")) {
                        isSuccess.value = true
                        Utils.showToast(context, "Schedule Added!")
                        getScheduleDetails()
                    } else {
                        isSuccess.value = false
                        Utils.showToast(context, it.body()?.message.toString())
                    }
                } else {
                    isSuccess.value = false
                    Utils.showToast(context, it.errorBody().toString())
                }
            }
        }

    }

    fun getScheduleDetails() {
        viewModelScope.launch {
            apiRepository.getScheduleDetails(MyPreferenses(context).getString(ConstantClass.USER_ID,
                "")!!).let {
                DataState.loading(false)
                if (it.isSuccessful) {
                    if (it.body()?.success.equals("0")) {
                        scheduleList.value = it.body()?.scheduleList!!
                        isEnable.set(true)
                        isVisibleLable.set(View.GONE)
                    } else {
                        isSuccess.value = false
                        Utils.showToast(context, it.body()?.message.toString())
                        isEnable.set(false)
                        isVisibleLable.set(View.VISIBLE)
                    }
                } else {
                    isEnable.set(false)
                    isVisibleLable.set(View.VISIBLE)
                    isSuccess.value = false
                    Utils.showToast(context, it.errorBody().toString())
                }
            }
        }
    }

    fun modifySchedule(flag: String, id: String?, time: String?, schedule_desc: String?) {
        viewModelScope.launch {
            apiRepository.modifySchedule(flag,
                MyPreferenses(context).getString(ConstantClass.USER_ID,
                    "")!!, id!!, time!!, schedule_desc!!).let {
                DataState.loading(false)
                if (it.isSuccessful) {
                    if (it.body()?.success.equals("0")) {
                        isModificationSuccess.value = true
                        getScheduleDetails()
                    } else {
                        isModificationSuccess.value = false
                        Utils.showToast(context, it.body()?.message.toString())
                    }
                } else {
                    isModificationSuccess.value = false
                    Utils.showToast(context, it.errorBody().toString())
                }
            }
        }
    }

    fun sendRequest() {
        viewModelScope.launch {
            apiRepository.sendRequests(MyPreferenses(context).getString(ConstantClass.USER_ID,
                "")!!, "diet").let {
                DataState.loading(false)
                if (it.isSuccessful) {
                    if (it.body()?.success.equals("0")) {
                        DataState.success(Utils.showToast(context,
                            it.body()?.message.toString()))
                    } else {
                        DataState.error(null, it.body()?.message.toString())
                    }
                } else {
                    isReqSuccess.value = false
                    Utils.showToast(context, it.errorBody().toString())
                }
            }
        }
    }

/*
    override fun onSuccess() {
        isSuccess.value = true
    }

    override fun onError(error: String) {
        isSuccess.value = false
        Utils.showToast(context, error)
    }

    override fun onListSuccess() {
        scheduleList.value = reqDietRepository.getScheduleList().value
    }

    override fun onListError(error: String) {
        Utils.showToast(context, error)
    }

    override fun onModificationSuccess(message: String) {
        isModificationSuccess.value = true
        Utils.showToast(context, message)
    }

    override fun onModificationError(error: String) {
        isModificationSuccess.value = false
        Utils.showToast(context, error)
    }

    override fun onReqSuccess(message: String) {
        isReqSuccess.value = true
        Utils.showToast(context, message)
    }

    override fun onReqError(error: String) {
        isReqSuccess.value = false
        Utils.showToast(context, error)
    }*/
}