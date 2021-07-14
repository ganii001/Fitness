package com.example.fitness.network.responses


data class LoginResponse(
    val success: String?,
    val message: String?,
    val name: String?,
    val contact: String?,
    val email: String?,
    val id: String?,
    val user_id: String?,
    val time: String?,
    val height: String?,
    val weight: String?,
    val sex: String?,
    val motive: String?,
    val age: String?,
    val schedule_desc: String?,
    val scheduleList: ArrayList<LoginResponse>?,

)
