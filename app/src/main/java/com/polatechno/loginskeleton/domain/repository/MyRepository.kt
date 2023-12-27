package com.polatechno.loginskeleton.domain.repository

import com.polatechno.loginskeleton.domain.model.CodeResult
import retrofit2.Response
import retrofit2.http.Query

interface MyRepository {

    suspend fun getCode(loginNumber: String): Response<CodeResult>

    suspend fun regenerateCode(loginNumber: String): Response<String>

    suspend fun getToken(
        @Query("login") loginNumber: String,
        @Query("password") password: String
    ): Response<String>


}