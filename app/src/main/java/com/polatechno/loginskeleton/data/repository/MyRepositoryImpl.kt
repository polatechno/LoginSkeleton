package com.polatechno.loginskeleton.data.repository

import com.polatechno.loginskeleton.data.remote.PravoeDeloApi
import com.polatechno.loginskeleton.domain.repository.MyRepository
import com.polatechno.loginskeleton.domain.model.CodeResult
import retrofit2.Response
import retrofit2.http.Query

class MyRepositoryImpl(private val api: PravoeDeloApi) : MyRepository {

    override suspend fun getCode(loginNumber: String): Response<CodeResult> {
        return api.getCode(loginNumber)
    }

    override suspend fun getToken(
        @Query("login") loginNumber: String,
        @Query("password") password: String
    ): Response<String> {
        return api.getToken(loginNumber, password)
    }

    override suspend fun regenerateCode(loginNumber: String): Response<String> {
        return api.regenerateCode(loginNumber)
    }
}