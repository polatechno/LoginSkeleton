package com.polatechno.loginskeleton.domain.model

data class CodeResult(
    val code: String,
    val status: String,
) {

    fun shouldConfirmCode(): Boolean {
        return ("new" == status)
    }

    override fun toString(): String {
        return "(code='$code' |  status='$status')"
    }


}