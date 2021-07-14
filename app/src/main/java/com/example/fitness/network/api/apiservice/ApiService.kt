package com.example.fitness.network.api.apiservice

import com.example.fitness.network.responses.LoginResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("login.php")
    suspend fun authenticate(
        @Body requestBody: RequestBody,
    ): Response<LoginResponse>

    @POST("register.php")
    suspend fun signUp(
        @Body requestBody: RequestBody,
    ): Response<LoginResponse>

    @POST("add_schedule.php")
    suspend fun addSchedule(
        @Body requestBody: RequestBody,
    ): Response<LoginResponse>

    @GET("getSchedule_details.php")
    suspend fun getScheduleDetails(
        @Query("user_id") user_id: String,
    ): Response<LoginResponse>

    @GET("modifySchedule.php")
    suspend fun modifySchedule(
        @Query("flag") flag: String,
        @Query("user_id") user_id: String,
        @Query("id") schedule_id: String,
        @Query("time") time: String,
        @Query("schedule_desc") schedule_desc: String,
    ): Response<LoginResponse>

    @GET("sendRequests.php")
    suspend fun sendRequests(
        @Query("user_id") user_id: String,
        @Query("req_type") req_type: String,
    ): Response<LoginResponse>
}