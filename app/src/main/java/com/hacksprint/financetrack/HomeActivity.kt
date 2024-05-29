package com.hacksprint.financetrack

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import java.text.NumberFormat
import java.util.Locale


class HomeActivity : AppCompatActivity() {
    private lateinit var viewModel: ExpenseViewModel
    private val expenseAdapter by lazy { ExpenseListAdapter() }
    private lateinit var totalExpensesTextView: TextView

    // chama novamente o banco de dados
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
        setContentView(R.layout.home_layout)

        totalExpensesTextView = findViewById(R.id.valor_despesas)
        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)

        viewModel.totalExpenses.observe(this) { total ->
            val format = NumberFormat.getCurrencyInstance(Locale.ITALY)
            totalExpensesTextView.text = format.format(total)
        }

        viewModel.loadTotalExpenses(expenseDao)


        val btnGrafic = findViewById<ImageView>(R.id.btn_grafic)
        btnGrafic.setOnClickListener {
        val intent = Intent(this, Chart::class.java)
            startActivity(intent)
        }

        val btnHome = findViewById<Button>(R.id.btn_list)
        btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val recyclerViewHome: RecyclerView = findViewById(R.id.rv_home)
        recyclerViewHome.layoutManager = GridLayoutManager(this, 2)
        val vazioImg: ImageView = findViewById(R.id.vazio_home)
// daqui , o view model observa as mudanças na lista e repassa para o adpter da home, passa no max 6 itens
        viewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)

        viewModel.expenses.observe(this) { expenses ->
            if (expenses.isNullOrEmpty()) {
                vazioImg.visibility = View.VISIBLE

            } else {
                vazioImg.visibility = View.GONE


                val firstSixExpenses = expenses?.take(6) ?: emptyList()
                val adapter = AdapterRvHome(firstSixExpenses)
                recyclerViewHome.adapter = adapter
            }
        }
            // Carregar despesas ao iniciar a HomeActivity
            viewModel.loadExpenses(expenseDao)
        }

    }
