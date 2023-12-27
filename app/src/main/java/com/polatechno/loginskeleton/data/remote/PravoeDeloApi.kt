package com.polatechno.loginskeleton.data.remote

import com.polatechno.loginskeleton.domain.model.CodeResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PravoeDeloApi {

    @GET("api/v1/getCode")
    suspend fun getCode(@Query("login") loginNumber: String): Response<CodeResult>

    @GET("api/v1/regenerateCode")
    suspend fun regenerateCode(@Query("login") loginNumber: String): Response<String>

    @GET("api/v1/getToken")
    suspend fun getToken(
        @Query("login") loginNumber: String,
        @Query("password") password: String
    ): Response<String>
}