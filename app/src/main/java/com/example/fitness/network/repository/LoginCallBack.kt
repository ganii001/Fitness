package com.example.fitness.network.repository

interface LoginCallBack {
    fun onSuccess();
    fun onError(error: String);

}