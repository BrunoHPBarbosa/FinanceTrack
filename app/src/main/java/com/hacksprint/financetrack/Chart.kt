package com.hacksprint.financetrack

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class Chart : AppCompatActivity() {
    private lateinit var viewModel: ExpenseViewModel
    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart

// chama novamente outra isntancia da base de dados
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            FinanceTrackDataBase::class.java,
            "finance_track_db"
        ).build()
    }

    private val expenseDao by lazy { db.getExpenseDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart)

        pieChart = findViewById(R.id.pieChart)
        barChart = findViewById(R.id.barChart)

        val btnMain = findViewById<Button>(R.id.btn_list)
        btnMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val btnHome = findViewById<Button>(R.id.btn_home)
        btnHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
// view model esta pegando os daods do expese para passar para os charts
        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)
        viewModel.expenses.observe(this) { expenses ->
            if (expenses != null) {
                configurePieChart(expenses)
                configureBarChart(expenses)
            }
        }

        // Carregar despesas ao iniciar a ChartActivity
        viewModel.loadExpenses(expenseDao)
    }

    private fun configurePieChart(expenses: List<ExpenseUiData>) {
        val pieEntries = mutableListOf<PieEntry>()
        expenses.forEach { expense ->
            pieEntries.add(PieEntry(expense.amount.toFloat(), expense.description))
        }

        val pieDataSet = PieDataSet(pieEntries, "")
        pieDataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        pieDataSet.valueTextSize = 10f
        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(10f)
        pieChart.data = pieData

        // Personalizações
        pieChart.description.isEnabled = false
        pieChart.setCenterText("Expenses")
        pieChart.setCenterTextSize(16f)
        pieChart.setCenterTextColor(Color.BLACK)
        pieChart.legend.isEnabled = true

        // Animação
        pieChart.animateXY(5000, 5000)

        pieChart.invalidate() // Atualiza o gráfico
    }

    private fun configureBarChart(expenses: List<ExpenseUiData>) {
        val barEntries = mutableListOf<BarEntry>()
        expenses.forEachIndexed { index, expense ->
            barEntries.add(BarEntry(index.toFloat(), expense.amount.toFloat()))
        }
//personalizacao do chart

        val barDataSet = BarDataSet(barEntries, "Expenses")
        barDataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        barDataSet.valueTextSize = 10f
        val barData = BarData(barDataSet)
        barDataSet.setValueTextSize(10f)
        barChart.data = barData

        // Personalizações
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = true

        // Animação
        barChart.animateXY(5000, 5000)

        barChart.invalidate() // Atualiza o gráfico
    }
}
