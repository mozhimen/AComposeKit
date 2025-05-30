package com.mozhimen.composek.bar.test.mos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContentBean(
    val id: Long,
    val pic: Int,
    val title: String,
    val user: UserBean,
    val likeNum: Int,
    val isVideo: Boolean
) : Parcelable