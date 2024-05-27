package com.hacksprint.financetrack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_layout)



        val btnHome = findViewById<Button>(R.id.btn_list)
        btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val recyclerViewHome: RecyclerView = findViewById(R.id.rv_home)


        val firstFourExpenses = MainActivity.expenses.take(4)



        val adapter = AdapterRvHome(firstFourExpenses)
        recyclerViewHome.layoutManager = GridLayoutManager(this, 2)
        recyclerViewHome.adapter = adapter
    }
}
