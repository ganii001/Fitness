package com.example.fitness.ui.login

import DataState
import android.Manifest.permission
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.app.ActivityCompat.*
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitness.ConstantClass
import com.example.fitness.MyPreferenses
import com.example.fitness.network.api.repository.ApiRepository
import com.example.fitness.util.Utils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.util.*

class LoginViewModel @ViewModelInject constructor(
    @ApplicationContext val context: Context,
    private val apiRepository: ApiRepository,
) :
    ViewModel() {

    // private val loginRepository: LoginRepository = LoginRepository(context, apiService, this)
    var isLoginSuccessful: MutableLiveData<Boolean> = MutableLiveData()

    fun onLogin(email: String, password: String) {
        viewModelScope.launch {
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("logname", email)
                .addFormDataPart("logpass", password)
                .build()
            apiRepository.authenticate(requestBody).let {
                DataState.loading(null)
                if (it.isSuccessful) {
                    if (it.body()?.success.equals("0")) {
                        MyPreferenses(context).setString(ConstantClass.USER_ID, it.body()?.id)
                        MyPreferenses(context).setString(ConstantClass.NAME, it.body()?.name)
                        MyPreferenses(context).setString(ConstantClass.MOB_NO, it.body()?.contact)
                        MyPreferenses(context).setString(ConstantClass.EMAIL, it.body()?.email)
                        MyPreferenses(context).setString(ConstantClass.AGE, it.body()?.age)
                        MyPreferenses(context).setString(ConstantClass.GENDER, it.body()?.sex)
                        MyPreferenses(context).setString(ConstantClass.HEIGHT, it.body()?.height)
                        MyPreferenses(context).setString(ConstantClass.WEIGHT, it.body()?.weight)
                        MyPreferenses(context).setString(ConstantClass.MOTIVE, it.body()?.motive)
                        MyPreferenses(context).setString(ConstantClass.USERNAME, email)
                        MyPreferenses(context).setString(ConstantClass.PASSWORD, password)
                        isLoginSuccessful.value = true
                        Utils.showToast(context, "Login Successfully!")
                    } else {
                        isLoginSuccessful.value = false
                        Utils.showToast(context, it.body()?.message.toString())
                    }
                } else {
                    isLoginSuccessful.value = false
                    Utils.showToast(context, it.errorBody().toString())
                }
            }
        }
    }

    fun onSignUp(
        name: String,
        mob: String,
        email: String,
        password: String,
        gender: String,
        age: String,
        height: String,
        weight: String,
        motive: String,
    ) {
        viewModelScope.launch {
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", name)
                .addFormDataPart("mob", mob)
                .addFormDataPart("mail", email)
                .addFormDataPart("age", age)
                .addFormDataPart("pass", password)
                .addFormDataPart("sex", gender)
                .addFormDataPart("height", height)
                .addFormDataPart("weight", weight)
                .addFormDataPart("motive", motive)
                .build()
            apiRepository.signUp(requestBody).let {
                DataState.loading(null)
                if (it.isSuccessful) {
                    if (it.body()?.success.equals("0")) {
                        isLoginSuccessful.value = true
                        Utils.showToast(context, "User Registered Successfully !")
                    } else {
                        isLoginSuccessful.value = false
                        Utils.showToast(context, it.body()?.message.toString())
                    }
                } else {
                    isLoginSuccessful.value = false
                    Utils.showToast(context, it.errorBody().toString())
                }
            }
        }
    }

}