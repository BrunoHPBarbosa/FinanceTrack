package com.hacksprint.financetrack

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "key")
    val name: String,
    @ColumnInfo(name = "is_selected")
    val isSelected: Boolean
)
