package com.polatechno.loginskeleton.presentation.GetCodeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polatechno.loginskeleton.common.LogManager
import com.polatechno.loginskeleton.common.Resource
import com.polatechno.loginskeleton.domain.use_case.GetCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class GetCodeScreenModel @Inject constructor(
    private val getCodeUseCase: GetCodeUseCase
) : ViewModel() {


    private val _state = MutableStateFlow(GetCodeScreenState())
    val state: StateFlow<GetCodeScreenState> = _state

    fun getCode(loginNumber: String) {
        getCodeUseCase(loginNumber).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    LogManager.print("Resource.Success")
                    _state.value = GetCodeScreenState(codeResult = result.data)

                }

                is Resource.Error -> {
                    LogManager.print("Resource.Error")
                    _state.value = GetCodeScreenState(
                        error = result.message ?: "An unexpected error occured"
                    )
                }

                is Resource.Loading -> {
                    LogManager.print("Resource.Loading")
                    _state.value = GetCodeScreenState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}