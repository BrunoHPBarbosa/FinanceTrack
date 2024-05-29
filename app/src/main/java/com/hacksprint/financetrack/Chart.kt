package com.hacksprint.financetrack

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart)
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
        // Configure o Grafico de pizza
        val pieChart: PieChart = findViewById(R.id.pieChart)
        configurePieChart(pieChart)

        // Configure o Grafico de barra
        val barChart: BarChart = findViewById(R.id.barChart)
        configureBarChart(barChart)
    }
//lista 3 itens com diferentes valores
    private fun configurePieChart(pieChart: PieChart) {
        val pieEntries = mutableListOf<PieEntry>()
        pieEntries.add(PieEntry(30f, "Item 1"))
        pieEntries.add(PieEntry(20f, "Item 2"))
        pieEntries.add(PieEntry(50f, "Item 3"))

        val pieDataSet = PieDataSet(pieEntries, "Items")
        pieDataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        val pieData = PieData(pieDataSet)
        pieChart.data = pieData

        // Personalizações
        pieChart.description.isEnabled = false
        pieChart.setCenterText("Meu Grafico de pizza")
        pieChart.setCenterTextSize(16f)
        pieChart.setCenterTextColor(Color.BLACK)
        pieChart.legend.isEnabled = true

        // Adiciona animação ao Grafico de pizza com duracao de 5seg
        pieChart.animateXY(5000, 5000)

        pieChart.invalidate() // Atualiza o gráfico
    }
// configura  grafico de barra
    private fun configureBarChart(barChart: BarChart) {
        val barEntries = mutableListOf<BarEntry>()
        barEntries.add(BarEntry(1f, 30f))
        barEntries.add(BarEntry(2f, 20f))
        barEntries.add(BarEntry(3f, 50f))

        val barDataSet = BarDataSet(barEntries, "Items")
        barDataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        val barData = BarData(barDataSet)
        barChart.data = barData


        barChart.animateXY(5000, 5000)

        barChart.invalidate() // Atualiza o gráfico
    }
}
