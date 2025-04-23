package com.mozhimen.composek.bar.test.mos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserBean(
    val headImg: String,
    val userName: String
) : Parcelable
