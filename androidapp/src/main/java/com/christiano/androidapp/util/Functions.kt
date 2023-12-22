package com.christiano.androidapp.util

import java.text.DateFormat
import java.util.Date

fun Long.convertLongToDate(): String {
    return DateFormat.getDateInstance().format(Date(this))
}