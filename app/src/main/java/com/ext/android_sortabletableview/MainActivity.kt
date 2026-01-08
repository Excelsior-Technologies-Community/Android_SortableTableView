package com.ext.android_sortabletableview

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ext.sortabletableview.SortableTableView
import com.ext.sortabletableview.model.TableRowData

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val table = findViewById<SortableTableView>(R.id.sortableTable)

        table.setHeaders(listOf("Name", "Age", "City"))

        table.setData(
            listOf(
                TableRowData(listOf("Amit", 25, "Delhi")),
                TableRowData(listOf("Ravi", 30, "Mumbai")),
                TableRowData(listOf("Neha", 22, "Pune")),
                TableRowData(listOf("Zara", 28, "Ahmedabad")),
                TableRowData(listOf("Kunal", 35, "Bangalore"))
            )
        )


    }
}