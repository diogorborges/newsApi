package com.x0.newsapi.data.model.types

import androidx.annotation.StringDef
import com.x0.newsapi.data.model.types.StatusType.Companion.STATUS_ERROR
import com.x0.newsapi.data.model.types.StatusType.Companion.STATUS_OK

@StringDef(STATUS_OK, STATUS_ERROR)
annotation class StatusType {
    companion object {
        const val STATUS_OK = "OK"
        const val STATUS_ERROR = "ERROR"
    }
}
