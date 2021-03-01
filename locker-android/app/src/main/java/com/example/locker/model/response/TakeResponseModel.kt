package com.example.locker.model.response

import java.security.cert.CertPathValidatorException

data class TakeResponseModel(
    val lockerId : Int?,
    val reason: String = ""
)