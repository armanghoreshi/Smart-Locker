package com.example.locker.model.response

import android.provider.ContactsContract
import java.time.LocalDateTime

data class UserInfoResponseModel(
    val id :Int?,
    val username : String?,
    val nickName: String?,
    val signUpAt : String?,
    val lastLoginAt : String?,
    val credit : Double? = 0.0,
    val currentLocker : LockerInfo?
)
