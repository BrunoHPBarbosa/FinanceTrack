package com.hacksprint.financetrack

import androidx.annotation.DrawableRes


data class ExpenseUiData(
    val id: Int,
    val description: String,
    val amount: String,
    val category: String,
    val date: String,
    @DrawableRes val iconResId: Int
)
