package com.polatechno.loginskeleton.presentation.GetTokenScreen

data class ComfirmScreenState(
    val isLoading: Boolean = false,
    val token: String? = null,
    val error: String = "",
    val regeneratedCode: String? = null,
    val canRegenerate: Boolean = false
)
