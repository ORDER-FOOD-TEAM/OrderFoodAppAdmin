package com.example.orderfoodappadmin.model

data class Order (
    var id: String,
    var total: Double,
    var num: Int,
    var time: String,
    var status: String,
)