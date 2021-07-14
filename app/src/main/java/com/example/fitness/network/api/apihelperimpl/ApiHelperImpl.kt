package com.example.fitness.network.api.apihelperimpl

import com.example.fitness.network.api.apihelper.ApiHelper
import com.example.fitness.network.api.apiservice.ApiService
import com.example.fitness.network.responses.LoginResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import javax.inject.Inject


/* com.example.fitness.network.Api.apihelperimpl.ApiHelperImpl will be the class which will implement com.example.fitness.network.Api.apihelper.ApiHelper to provide functionality to com.example.fitness.network.Api.apihelper.ApiHelper functions */
class ApiHelperImpl @Inject constructor(private val apiService: ApiService) :
    ApiHelper {
    override suspend fun authenticate(@Body requestBody: RequestBody): Response<LoginResponse> =
        apiService.authenticate(requestBody)

    override suspend fun signUp(requestBody: RequestBody): Response<LoginResponse> =
        apiService.signUp(requestBody)

    override suspend fun addSchedule(requestBody: RequestBody): Response<LoginResponse> =
        apiService.addSchedule(requestBody)

    override suspend fun getScheduleDetails(user_id: String): Response<LoginResponse> =
        apiService.getScheduleDetails(user_id)

    override suspend fun modifySchedule(
        flag: String,
        user_id: String,
        schedule_id: String,
        time: String,
        schedule_desc: String,
    ): Response<LoginResponse> =
        apiService.modifySchedule(flag, user_id, schedule_id, time, schedule_desc)

    override suspend fun sendRequests(user_id: String, req_type: String): Response<LoginResponse> =
        apiService.sendRequests(user_id, req_type)

}