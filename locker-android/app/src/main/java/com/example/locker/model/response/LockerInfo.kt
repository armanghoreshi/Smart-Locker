package com.example.locker.model.response

import java.time.LocalDateTime

data class LockerInfo(
    val id : Int?,
    val status : String?,
    val ownerInfo : UserInfo?,
    val maxEndurance : Double?,
    val lastModified : String?
)

