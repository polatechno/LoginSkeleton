package com.polatechno.loginskeleton.presentation.GetTokenScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polatechno.loginskeleton.common.LogManager
import com.polatechno.loginskeleton.common.Resource
import com.polatechno.loginskeleton.domain.use_case.GetTokenUseCase
import com.polatechno.loginskeleton.domain.use_case.RegenerateCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ConfirmScreenViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val regenerateCodeUseCase: RegenerateCodeUseCase
) : ViewModel() {

    private val _stateConfirmScreen = MutableStateFlow(ComfirmScreenState())
    val stateConfirmScreen: StateFlow<ComfirmScreenState> = _stateConfirmScreen


    fun getToken(phoneNumber: String, password: String) {
        getTokenUseCase(phoneNumber, password).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    LogManager.print("Resource.Success")
                    _stateConfirmScreen.value = ComfirmScreenState(token = result.data)
                }

                is Resource.Error -> {
                    LogManager.print("Resource.Error")
                    _stateConfirmScreen.value = ComfirmScreenState(
                        error = result.message ?: "An unexpected error occured",
                        canRegenerate = true
                    )
                }

                is Resource.Loading -> {
                    LogManager.print("Resource.Loading")
                    _stateConfirmScreen.value = ComfirmScreenState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }


    fun regenerateCode(phoneNumber: String) {
        regenerateCodeUseCase(phoneNumber).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    LogManager.print("Resource.Success")
                    _stateConfirmScreen.value = ComfirmScreenState(
                        regeneratedCode = result.data,
                        canRegenerate = false
                    )
                }

                is Resource.Error -> {
                    LogManager.print("Resource.Error")
                    _stateConfirmScreen.value = ComfirmScreenState(
                        error = result.message ?: "An unexpected error occured",

                        )
                }

                is Resource.Loading -> {
                    LogManager.print("Resource.Loading")
                    _stateConfirmScreen.value = ComfirmScreenState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}