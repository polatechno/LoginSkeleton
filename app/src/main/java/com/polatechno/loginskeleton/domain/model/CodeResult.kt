package com.polatechno.loginskeleton.domain.model

data class CodeResult(
    val code: String,
    val status: String,
) {

    override fun toString(): String {
        return "(code='$code' |  status='$status')"
    }
}