package com.hacksprint.financetrack


data class ExpenseUiData(
    val id: Int,
    val description: String,
    val amount: String,
    val category: String,
    val date: String,
    val iconResId: Int
)
