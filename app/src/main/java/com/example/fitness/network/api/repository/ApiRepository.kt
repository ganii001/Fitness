package com.example.fitness.network.api.repository

import com.example.fitness.network.api.apihelper.ApiHelper
import okhttp3.RequestBody
import retrofit2.http.Body
import javax.inject.Inject

/* class which will call the function of com.example.fitness.network.Api.apihelper.ApiHelper to get the response. */
class ApiRepository @Inject constructor(private val apiHelper: ApiHelper) {

    suspend fun authenticate(@Body requestBody: RequestBody) = apiHelper.authenticate(requestBody)

    suspend fun signUp(requestBody: RequestBody) = apiHelper.signUp(requestBody)

    suspend fun addSchedule(requestBody: RequestBody) = apiHelper.addSchedule(requestBody)

    suspend fun getScheduleDetails(user_id: String) = apiHelper.getScheduleDetails(user_id)

    suspend fun modifySchedule(
        flag: String,
        user_id: String,
        schedule_id: String,
        time: String,
        schedule_desc: String,
    ) = apiHelper.modifySchedule(flag, user_id, schedule_id, time, schedule_desc)

    suspend fun sendRequests(user_id: String, req_type: String) =
        apiHelper.sendRequests(user_id, req_type)
}