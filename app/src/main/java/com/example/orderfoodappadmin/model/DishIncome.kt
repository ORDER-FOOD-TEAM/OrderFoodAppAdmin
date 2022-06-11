package com.example.orderfoodappadmin.model
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class DishIncome(
    var id: String = "",
    var totalIncome: Double = 0.0,
    var SInCome: Double = 0.0,
    var MIncome: Double = 0.0,
    var LIncome: Double = 0.0,
    var amountS: Int = 0,
    var amountM: Int = 0,
    var amountL: Int = 0,
)
