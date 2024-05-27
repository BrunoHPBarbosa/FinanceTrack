package com.hacksprint.financetrack

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class CreateOrUpdateExpenseBottomSheet(
    private val categoryList: List<CategoryEntity>,
    private val expense: ExpenseUiData? = null,
    private val onCreateClicked: (ExpenseUiData) -> Unit,
    private val onUpdateClicked: (ExpenseUiData) -> Unit,
    private val onDeleteClicked: (ExpenseUiData) -> Unit
) : BottomSheetDialogFragment(), ListIconsAdapter.IconClickListener {

    private var icons = listOf(
        R.drawable.ic_home,
        R.drawable.baseline_wifi_24,
        R.drawable.baseline_water_drop_24,
        R.drawable.baseline_videogame_asset_24,
        R.drawable.baseline_shopping_cart_24,
        R.drawable.baseline_local_gas_station_24,
        R.drawable.baseline_local_airport_24,
        R.drawable.baseline_electric_bolt_24,
        R.drawable.baseline_directions_car_24,
        R.drawable.baseline_credit_card_24
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.create_or_update_expense_bottom_sheet, container, false)

        val recyclerViewIcons = view.findViewById<RecyclerView>(R.id.recyclerListIcons)

        val listIconsAdapter = ListIconsAdapter(icons, this) // Passa esta classe como ouvinte de clique de ícone
        recyclerViewIcons.adapter = listIconsAdapter
        recyclerViewIcons.layoutManager = GridLayoutManager(context, 5)

        val tvTitle = view.findViewById<TextView>(R.id.tv_expense_title)
        val edtExpenseName = view.findViewById<TextInputEditText>(R.id.et_expense_name)
        val edtExpenseAmount = view.findViewById<TextInputEditText>(R.id.et_expense_amount)
        val edtExpenseDate = view.findViewById<TextInputEditText>(R.id.et_expense_date)
        val btnCreateOrUpdate = view.findViewById<Button>(R.id.btn_expense_create)
        val spinner: Spinner = view.findViewById(R.id.category_spinner)

        var expenseCategory: String? = null
        val categoryListTemp = mutableListOf("Select a category")
        categoryListTemp.addAll(categoryList.map { it.name })

        edtExpenseDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                edtExpenseDate.setText(formattedDate)
            }, year, month, day)

            datePickerDialog.show()
        }

        ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_item,
            categoryListTemp.toList()
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                expenseCategory = categoryListTemp[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                showMessages("Category is required")
            }
        }

        if (expense == null) {
            tvTitle.setText(R.string.create_expense_title)
            btnCreateOrUpdate.setText(R.string.create)
        } else {
            tvTitle.setText(R.string.update_expense_title)
            btnCreateOrUpdate.setText(R.string.update)
            edtExpenseName.setText(expense.description)
            edtExpenseAmount.setText(expense.amount)
            edtExpenseDate.setText(expense.date)

            val currentCategory = categoryList.first { it.name == expense.category }
            val index = categoryList.indexOf(currentCategory)
            spinner.setSelection(index)

        }

        btnCreateOrUpdate.setOnClickListener {
            val name = edtExpenseName.text.toString().trim()
            val amount = edtExpenseAmount.text.toString().trim()
            val date = edtExpenseDate.text.toString().trim()
            if(expenseCategory != "Select a category" && name.isNotEmpty()) {

                val iconResId = listIconsAdapter.selectedIconPosition
                if (iconResId != null && iconResId < icons.size) {
                    val expenseUiData = ExpenseUiData(
                        id = expense?.id ?: 0,
                        description = name,
                        amount = amount,
                        date = date,
                        category = requireNotNull(expenseCategory),
                        iconResId = icons[iconResId]

                    )

                    if (expense == null) {
                        onCreateClicked.invoke(expenseUiData)

                        dismiss()
                        showMessages("Expense created")

                    } else {
                        onUpdateClicked.invoke(expenseUiData)

                        dismiss()
                        showMessages("Expense updated")
                    }

                } else {
                    showMessages("Fields are required")
                }
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as? ViewGroup
        val behavior = bottomSheet?.let { BottomSheetBehavior.from(it) }
        if (behavior != null) {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        if (behavior != null) {
            behavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO
        }
    }

    private fun showMessages(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onIconClicked(iconResId: Int) {
        val listIconsAdapter = ListIconsAdapter(icons = icons, this)
        listIconsAdapter.updateSelectedIconPosition(iconResId)
    }
}
