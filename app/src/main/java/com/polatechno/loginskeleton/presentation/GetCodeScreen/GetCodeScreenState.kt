package com.polatechno.loginskeleton.presentation.GetCodeScreen

import com.polatechno.loginskeleton.domain.model.CodeResult

data class GetCodeScreenState(
    val isLoading: Boolean = false,
    val codeResult: CodeResult? = null,
    val error: String = ""
)
