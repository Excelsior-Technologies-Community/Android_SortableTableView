package com.ext.sortabletableview

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ext.sortabletableview.adapter.TableRowAdapter
import com.ext.sortabletableview.model.TableRowData

class SortableTableView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var tableData: List<TableRowData> = emptyList()

    private var currentSortColumn = -1
    private var currentSortAscending = true

    private val headerViews = mutableListOf<TextView>()
    private val tableAdapter = TableRowAdapter()


    private val headerLayout: LinearLayout by lazy {
        LinearLayout(context).apply {
            orientation = HORIZONTAL
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        }
    }

    private val recyclerView: RecyclerView by lazy {
        RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context)
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        }
    }




    init {
        orientation = VERTICAL
        readAttributes(context, attrs)
        addView(headerLayout)
        addView(recyclerView)
        recyclerView.adapter = tableAdapter
    }

    private fun readAttributes(context: Context, attrs: AttributeSet?) {
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.SortableTableView
        )

        val textColor = typedArray.getColor(
            R.styleable.SortableTableView_stv_textColor,
            Color.BLACK
        )

        typedArray.recycle()
    }

    fun setHeaders(headers: List<String>) {
        headerLayout.removeAllViews()
        headerViews.clear()

        headers.forEachIndexed { index, title ->
            val textView = TextView(context).apply {
                text = title
                setPadding(16, 16, 16, 16)
                layoutParams = LayoutParams(
                    0,
                    LayoutParams.WRAP_CONTENT,
                    1f
                )
                setTypeface(typeface, Typeface.BOLD)

                setOnClickListener {
                    handleSort(index)
                    updateSortIcons()
                }
            }

            headerViews.add(textView)
            headerLayout.addView(textView)
        }
    }

    fun setData(rows: List<TableRowData>) {
        tableData = rows
        tableAdapter.submitData(tableData)
    }


    private fun handleSort(columnIndex: Int) {
        currentSortAscending = if (currentSortColumn == columnIndex) {
            !currentSortAscending
        } else {
            true
        }

        currentSortColumn = columnIndex

        sortData(columnIndex, currentSortAscending)
    }

    private fun sortData(columnIndex: Int, ascending: Boolean) {
        tableData = tableData.sortedWith { row1, row2 ->
            val v1 = row1.cells[columnIndex]
            val v2 = row2.cells[columnIndex]

            when {
                v1 is Number && v2 is Number -> {
                    if (ascending) {
                        v1.toDouble().compareTo(v2.toDouble())
                    } else {
                        v2.toDouble().compareTo(v1.toDouble())
                    }
                }
                else -> {
                    if (ascending) {
                        v1.toString().compareTo(v2.toString(), true)
                    } else {
                        v2.toString().compareTo(v1.toString(), true)
                    }
                }
            }
        }

        tableAdapter.submitData(tableData)
    }

    private fun updateSortIcons() {
        headerViews.forEachIndexed { index, textView ->
            val baseText = textView.text.toString()
                .replace(" ↑", "")
                .replace(" ↓", "")

            if (index == currentSortColumn) {
                val arrow = if (currentSortAscending) " ↑" else " ↓"
                textView.text = baseText + arrow
            } else {
                textView.text = baseText
            }
        }
    }

}
