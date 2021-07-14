package com.example.fitness.network.repository.req_diet

interface ReqDietCallBack {
    fun onListSuccess();
    fun onListError(error: String);

    fun onSuccess();
    fun onError(error: String);

    fun onModificationSuccess(message: String);
    fun onModificationError(error: String);

    fun onReqSuccess(message: String);
    fun onReqError(error: String);
}