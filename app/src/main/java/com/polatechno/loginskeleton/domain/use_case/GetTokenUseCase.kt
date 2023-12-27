package com.polatechno.loginskeleton.domain.use_case


import com.polatechno.loginskeleton.common.LogManager
import com.polatechno.loginskeleton.common.Resource
import com.polatechno.loginskeleton.domain.repository.MyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class GetTokenUseCase @Inject constructor(
    private val repository: MyRepository
) {
    operator fun invoke(phoneNumber: String, password: String): Flow<Resource<String>> = flow {

        if (password.trim().isEmpty()) {
            emit(Resource.Error<String>("Password number can not be empty"))
        } else {

            try {
                emit(Resource.Loading<String>())
                val response = repository.getToken(phoneNumber, password)
                LogManager.print(response.body().toString())

                if (response.isSuccessful && response.body() != null) {
                    emit(Resource.Success<String>(response.body()!!))
                } else {
                    try {
                        val jsonErrorObject = JSONObject(response.errorBody()!!.string())
                        emit(Resource.Error<String>(jsonErrorObject.getString("error")))
                    } catch (e: Exception) {
                        emit(
                            Resource.Error<String>(
                                e.message ?: "Exception in error response processing..."
                            )
                        )
                    }
                }
            } catch (e: HttpException) {
                emit(Resource.Error<String>(e.localizedMessage ?: "An unexpected error occured"))
            } catch (e: IOException) {
                emit(Resource.Error<String>("Couldn't reach server. Check your internet connection." + e.localizedMessage))
            }
        }
    }
}