package com.hacksprint.financetrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.ParseException
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class ExpenseListAdapter :
    ListAdapter<ExpenseUiData, ExpenseListAdapter.ExpenseViewHolder>(
    diffCallback()
) {
    private lateinit var callBack: (ExpenseUiData) -> Unit

    fun setOnClickListener(onClick: (ExpenseUiData) -> Unit) {
        callBack = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_recycler_list, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category, callBack)
    }

    class ExpenseViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val tvCategoryName: TextView = view.findViewById(R.id.tv_category_name)
        private val tvExpenseName: TextView = view.findViewById(R.id.tv_expense_name)
        private val tvValueAmount: TextView = view.findViewById(R.id.tv_value_amount)
        private val tvValueDate: TextView = view.findViewById(R.id.tv_value_date)

        fun bind(expense: ExpenseUiData, callBack: (ExpenseUiData) -> Unit) {
            tvCategoryName.text = expense.category
            tvExpenseName.text = expense.description
            tvValueAmount.text = expense.amount
            tvValueDate.text = formatDate(expense.date)

            view.setOnClickListener {
                callBack.invoke(expense)
            }
        }

    fun formatDate(dateString: String): String {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            return try {
                val date = inputFormat.parse(dateString)
                outputFormat.format(date!!)
            } catch (e: ParseException) {
                ""
                }
            }
    }

    class diffCallback : DiffUtil.ItemCallback<ExpenseUiData>() {
        override fun areItemsTheSame(oldItem: ExpenseUiData, newItem: ExpenseUiData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ExpenseUiData, newItem: ExpenseUiData): Boolean {
            return oldItem.amount == newItem.amount
                && oldItem.category == newItem.category
                && oldItem.date == newItem.date
                && oldItem.description == newItem.description
        }
    }
}
