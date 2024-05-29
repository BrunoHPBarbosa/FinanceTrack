package com.hacksprint.financetrack

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        var expenses:
                List<ExpenseUiData> = listOf()
    }

    private var categories = listOf<CategoryUiData>()
    private var expenses = listOf<ExpenseUiData>()
    private var categoriesEntity = listOf<CategoryEntity>()
    private lateinit var onDeleteClicked: (ExpenseUiData) -> Unit
    private lateinit var ctnContent: ImageView
    // chamei o view model
    private lateinit var viewModel: ExpenseViewModel
    private val categoryAdapter = CategoryListAdapter()
    private val expenseAdapter by lazy {
        ExpenseListAdapter()
    }

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            FinanceTrackDataBase::class.java,
            "finance_track_db"
        ).build()
    }

    private val categoryDao by lazy {
        db.getCategoryDao()
    }

    private val expenseDao by lazy {
        db.getExpenseDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val btnHome = findViewById<Button>(R.id.btn_home)
        btnHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }


        ctnContent = findViewById(R.id.ctn_content)

        val rvCategory = findViewById<RecyclerView>(R.id.rv_categories)
        val rvExpense = findViewById<RecyclerView>(R.id.rv_expenses)
        val fabCreateExpense = findViewById<ImageView>(R.id.btn_add_expense)
        val fabCreateCategory = findViewById<ImageView>(R.id.btn_add_categorie)


        val deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_delete)!!
        val swipeBackground = ColorDrawable(Color.RED)

// aqui inicia o view model usando o viewmodel provider
        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)

// aqui e um live data , ele observa as lista do expense , se mudar a lista tbm mudara na outra tela
        viewModel.expenses.observe(this) { expenses ->
            expenseAdapter.submitList(expenses)
        }

        viewModel.categories.observe(this) { categories ->
            categoryAdapter.submitList(categories)
        }

        // Carregar dados da  criação
        viewModel.loadExpenses(expenseDao)
        viewModel.loadCategories(categoryDao)


        fabCreateCategory.setOnClickListener {
            CreateCategoryBottomSheet(
                onCreateClicked = { categoryName ->
                    val categoryEntity = CategoryEntity(
                        name = categoryName,
                        isSelected = false
                    )

                    insertCategory(categoryEntity)
                }
            ).show(
                supportFragmentManager, "create_category"
            )
        }

        fabCreateExpense.setOnClickListener {
            showCreateUpdateExpenseBottomSheet()
        }

        expenseAdapter.setOnClickListener { expense ->
            showCreateUpdateExpenseBottomSheet(expense)
        }

        categoryAdapter.setOnLongClickListener { categoryToBeDeleted ->
            if (categoryToBeDeleted.name != "ALL") {
                val title = this.getString(R.string.category_delete_title)
                val message = this.getString(R.string.category_delete_message)
                val btnAction = this.getString(R.string.delete)

                showInfoDialog(
                    title = title,
                    message = message,
                    action = btnAction,
                ) {
                    val categoryEntityToBeDeleted = CategoryEntity(
                        name = categoryToBeDeleted.name,
                        isSelected = categoryToBeDeleted.isSelected
                    )
                    deleteCategory(categoryEntityToBeDeleted)
                    Toast.makeText(this, "Category deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }

        categoryAdapter.setOnClickListener { selected ->
            filterExpensesByCategoryName(selected.name)

            val categoryTemp = categories.map { item ->
                when {
                    item.name == selected.name && item.isSelected -> item.copy(isSelected = true)
                    item.name == selected.name && !item.isSelected -> item.copy(isSelected = true)
                    item.name != selected.name && item.isSelected -> item.copy(isSelected = false)
                    else -> item
                }
            }

            categoryAdapter.submitList(categoryTemp)

            if (selected.name == "ALL") {
                GlobalScope.launch(Dispatchers.IO) {
                    getExpensesFromDatabase()
                }
            }
        }

        rvCategory.adapter = categoryAdapter
        rvExpense.adapter = expenseAdapter

        onDeleteClicked = { expense ->
            val expenseEntityToBeDeleted = ExpenseEntity(
                id = expense.id,
                amount = expense.amount,
                description = expense.description,
                category = expense.category,
                date = expense.date,
                iconResId = expense.iconResId,
                dueDate = expense.dueDate

            )
            deleteExpense(expenseEntityToBeDeleted)
            Toast.makeText(this, "Expense deleted", Toast.LENGTH_SHORT).show()
        }

        val itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val task = expenses[position]
                    onDeleteClicked.invoke(task)

                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )

                    val itemView = viewHolder.itemView
                    val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2
                    val iconTop =
                        itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
                    val iconBottom = iconTop + deleteIcon.intrinsicHeight

                    if (dX < 0) {
                        val iconLeft = itemView.right - iconMargin - deleteIcon.intrinsicWidth
                        val iconRight = itemView.right - iconMargin
                        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                        swipeBackground.setBounds(
                            itemView.right + dX.toInt(),
                            itemView.top,
                            itemView.right,
                            itemView.bottom
                        )
                    } else {
                        swipeBackground.setBounds(0, 0, 0, 0)
                        deleteIcon.setBounds(0, 0, 0, 0)
                    }

                    swipeBackground.draw(c)
                    deleteIcon.draw(c)
                }
            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rvExpense)
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch(Dispatchers.IO) {
            getCategoriesFromDatabase()
            getExpensesFromDatabase()

        }
    }

    private fun showInfoDialog(
        title: String,
        message: String,
        action: String,
        onActionClicked: () -> Unit
    ) {
        val infoBottomSheet = InfoBottomSheet(
            title = title,
            message = message,
            action = action,
            onActionClicked = onActionClicked
        )
        infoBottomSheet.show(
            supportFragmentManager,
            "infoBottomSheet"
        )
    }

    private fun getCategoriesFromDatabase() {
        val categoriesFromDb: List<CategoryEntity> = categoryDao.getAll()
        categoriesEntity = categoriesFromDb
        val categoriesUiData = categoriesFromDb.map {
            CategoryUiData(
                name = it.name,
                isSelected = it.isSelected
            )
        }
            .toMutableList()

        val tempCategoryList = mutableListOf(
            CategoryUiData(
                name = "ALL",
                isSelected = true
            )
        )

        tempCategoryList.addAll(categoriesUiData)
        GlobalScope.launch(Dispatchers.Main) {
            categories = tempCategoryList
            categoryAdapter.submitList(categories)
        }

    }

    private fun getExpensesFromDatabase() {
        val expensesFromDb: List<ExpenseEntity> = expenseDao.getAll()
        val expensesUiData = expensesFromDb.map {
            ExpenseUiData(
                id = it.id,
                amount = it.amount,
                category = it.category,
                description = it.description,
                date = it.date,
                iconResId = it.iconResId,
                dueDate = it.dueDate
            )
        }

        GlobalScope.launch(Dispatchers.Main) {
            expenses = expensesUiData
            expenseAdapter.submitList(expensesUiData)

            if (expenses.isEmpty()) {
                ctnContent.visibility = android.view.View.VISIBLE
            } else {
                ctnContent.visibility = android.view.View.GONE
            }
        }
    }

    private fun insertCategory(categoryEntity: CategoryEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            categoryDao.insert(categoryEntity)
            getCategoriesFromDatabase()
        }
    }

    private fun insertExpense(expenseEntity: ExpenseEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            expenseDao.insert(expenseEntity)
            getExpensesFromDatabase()
        }
    }

    private fun updateExpense(expenseEntity: ExpenseEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            expenseDao.update(expenseEntity)
            getExpensesFromDatabase()
        }
    }

    private fun deleteExpense(expenseEntity: ExpenseEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            expenseDao.delete(expenseEntity)
            getExpensesFromDatabase()
        }
    }

    private fun deleteCategory(categoryEntity: CategoryEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            val expensesToBeDeleted = expenseDao.getAllByCategoryName(categoryEntity.name)
            expenseDao.deleteAll(expensesToBeDeleted)
            categoryDao.delete(categoryEntity)
            getCategoriesFromDatabase()
            getExpensesFromDatabase()
        }
    }

    private fun filterExpensesByCategoryName(categoryName: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val expensesFromDb: List<ExpenseEntity> = expenseDao.getAllByCategoryName(categoryName)
            val expensesUiData = expensesFromDb.map {
                ExpenseUiData(
                    id = it.id,
                    amount = it.amount,
                    category = it.category,
                    description = it.description,
                    date = it.date,
                    iconResId = it.iconResId,
                    dueDate = it.dueDate
                )
            }

            GlobalScope.launch(Dispatchers.Main) {
                expenseAdapter.submitList(expensesUiData)
            }
        }
    }

    private fun showCreateUpdateExpenseBottomSheet(expenseUiData: ExpenseUiData? = null) {
        val createExpenseBottomSheet = CreateOrUpdateExpenseBottomSheet(
            expense = expenseUiData,
            categoryList = categoriesEntity,
            onCreateClicked = { expenseToBeCreated ->
                val expenseEntityToBeInserted = ExpenseEntity(
                    amount = expenseToBeCreated.amount,
                    category = expenseToBeCreated.category,
                    description = expenseToBeCreated.description,
                    date = expenseToBeCreated.date,
                    iconResId = expenseToBeCreated.iconResId,
                    dueDate = expenseToBeCreated.dueDate

                )
                insertExpense(expenseEntityToBeInserted)

            },
            onUpdateClicked = { expenseToBeUpdated ->
                val expenseEntityToBeUpdated = ExpenseEntity(
                    id = expenseToBeUpdated.id,
                    amount = expenseToBeUpdated.amount,
                    category = expenseToBeUpdated.category,
                    description = expenseToBeUpdated.description,
                    date = expenseToBeUpdated.date,
                    iconResId = expenseToBeUpdated.iconResId,
                    dueDate = expenseToBeUpdated.dueDate

                )
                updateExpense(expenseEntityToBeUpdated)
            },
            onDeleteClicked = onDeleteClicked
        )
        createExpenseBottomSheet.show(
            supportFragmentManager,
            "create_expense"
        )
    }
}
