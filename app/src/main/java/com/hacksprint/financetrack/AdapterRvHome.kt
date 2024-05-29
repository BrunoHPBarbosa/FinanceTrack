package com.hacksprint.financetrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdapterRvHome(private val itemList: List<ExpenseUiData>) : RecyclerView.Adapter<AdapterRvHome.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_recycler_home, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < itemList.size) {
            val item = itemList[position]
            holder.bind(item)
            val layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            holder.itemView.layoutParams = layoutParams
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCategoryName_home: TextView = itemView.findViewById(R.id.tv_category_name_home)
        private val tvExpenseName_home: TextView = itemView.findViewById(R.id.tv_expense_name_home)
        private val tvValueAmount_home: TextView = itemView.findViewById(R.id.tv_value_amount_home)
        private val tvValueDate_home: TextView = itemView.findViewById(R.id.tv_value_date_home)
        private val tvValueIcon_home: ImageView = itemView.findViewById(R.id.iv_category_home)
        private val ivStatus_home: ImageView = itemView.findViewById(R.id.iv_status_home)
        fun bind(expense: ExpenseUiData) {
            tvCategoryName_home.text = expense.category
            tvExpenseName_home.text = expense.description
            tvValueAmount_home.text = expense.amount
            tvValueDate_home.text = formatDate(expense.date)
            tvValueIcon_home.setImageResource(expense.iconResId)
            val dueDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(expense.date)
            val currentDate = Date()
            if (dueDate != null) {
                if (dueDate.before(currentDate)) {
                   ivStatus_home.setImageResource(R.drawable.baseline_circle_red_24)
                } else {
                   ivStatus_home.setImageResource(R.drawable.baseline_circle_green_24)
                }
            }

        }

        private fun formatDate(dateString: String): String {
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
}
