package com.polatechno.loginskeleton.domain.use_case


import com.polatechno.loginskeleton.common.Resource
import com.polatechno.loginskeleton.domain.repository.MyRepository
import com.polatechno.loginskeleton.domain.model.CodeResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCodeUseCase @Inject constructor(
    private val repository: MyRepository
) {
    operator fun invoke(phoneNumber: String): Flow<Resource<CodeResult>> = flow {

        if (phoneNumber.trim().isEmpty()) {
            emit(Resource.Error<CodeResult>("Phone number can not be empty"))
        } else {

            try {
                emit(Resource.Loading<CodeResult>())
                val response = repository.getCode(phoneNumber)

                if (response.isSuccessful && response.body() != null) {
                    emit(Resource.Success<CodeResult>(response.body()!!))
                } else {
                    try {
                        val jsonErrorObject = JSONObject(response.errorBody()!!.string())
                        emit(Resource.Error<CodeResult>(jsonErrorObject.getString("error")))
                    } catch (e: Exception) {
                        emit(
                            Resource.Error<CodeResult>(
                                e.message ?: "Exception in error response processing..."
                            )
                        )
                    }
                }
            } catch (e: HttpException) {
                emit(
                    Resource.Error<CodeResult>(
                        e.localizedMessage ?: "An unexpected error occured"
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error<CodeResult>("Couldn't reach server. Check your internet connection."))
            }
        }
    }
}