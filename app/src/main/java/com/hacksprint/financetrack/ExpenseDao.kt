package com.hacksprint.financetrack

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expenseentity")
    fun getAll(): List<ExpenseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(expenseEntity: List<ExpenseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(expenseEntity: ExpenseEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(expenseEntity: ExpenseEntity)

    @Delete
    fun delete(expenseEntity: ExpenseEntity)

    @Query("Select * FROM expenseentity where category is :categoryName" )
    fun getAllByCategoryName(categoryName: String): List<ExpenseEntity>

    @Delete
    fun deleteAll(expenseEntity: List<ExpenseEntity>)
}