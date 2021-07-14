package com.example.fitness.network.api.apihelper

import com.example.fitness.network.responses.LoginResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Query

/* com.example.fitness.network.Api.apihelper.ApiHelper will help it to be accessed via repository maintaining encapsulation */
interface ApiHelper {

    suspend fun authenticate(@Body requestBody: RequestBody): Response<LoginResponse>

    suspend fun signUp(@Body requestBody: RequestBody): Response<LoginResponse>

    suspend fun addSchedule(@Body requestBody: RequestBody): Response<LoginResponse>

    suspend fun getScheduleDetails(@Query("user_id") user_id: String): Response<LoginResponse>

    suspend fun modifySchedule(
        @Query("flag") flag: String,
        @Query("user_id") user_id: String,
        @Query("id") schedule_id: String,
        @Query("time") time: String,
        @Query("schedule_desc") schedule_desc: String,
    ): Response<LoginResponse>

    suspend fun sendRequests(
        @Query("user_id") user_id: String,
        @Query("req_type") req_type: String,
    ): Response<LoginResponse>
}