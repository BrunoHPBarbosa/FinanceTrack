package com.hacksprint.financetrack

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExpenseViewModel : ViewModel() {
// aqui e a propriedade que contem a lista dos daods do expense

    private val _expenses = MutableLiveData<List<ExpenseUiData>?>()
    val expenses: MutableLiveData<List<ExpenseUiData>?> get() = _expenses

    private val _categories = MutableLiveData<List<CategoryUiData>?>()
    val categories: MutableLiveData<List<CategoryUiData>?> get() = _categories

    private val _totalExpenses = MutableLiveData<Double?>()
    val totalExpenses: MutableLiveData<Double?> get() = _totalExpenses

    fun loadTotalExpenses(expenseDao: ExpenseDao) {
        viewModelScope.launch {
            _totalExpenses.value = withContext(Dispatchers.IO) {
                expenseDao.getSumOfExpenses()
            }
        }
    }
// aqui ele pega os dados do expense
    // o map transforma ele em objeto , que da para ser usado em outros lugares
fun loadExpenses(expenseDao: ExpenseDao) {
    viewModelScope.launch {
        _expenses.value = withContext(Dispatchers.IO) {
            val allExpenses = expenseDao.getAll()
            val sortedExpenses = allExpenses.sortedBy { it.date }
            sortedExpenses.map { expense ->
                ExpenseUiData(
                    id = expense.id,
                    amount = expense.amount,
                    description = expense.description,
                    category = expense.category,
                    date = expense.date,
                    iconResId = expense.iconResId,
                    dueDate = expense.dueDate
                )
            }
        }
    }
}

    fun loadCategories(categoryDao: CategoryDao) {
        viewModelScope.launch {
            _categories.value = withContext(Dispatchers.IO) {
                val categoriesFromDb = categoryDao.getAll()
                val categoriesUiData = categoriesFromDb.map {
                    CategoryUiData(
                        name = it.name,
                        isSelected = it.isSelected
                    )
                }.toMutableList()
                val tempCategoryList = mutableListOf(
                    CategoryUiData(
                        name = "ALL",
                        isSelected = true
                    )
                )
                tempCategoryList.addAll(categoriesUiData)
                tempCategoryList
            }
        }
    }
}
