package com.ext.sortabletableview

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
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
    private var headerTextColor = Color.BLACK
    private var rowTextColor = Color.DKGRAY
    private var textSizePx: Float = 14f
    private var headerBackground: Drawable? = null
    private var rowBackground: Drawable? = null
    private var cellPaddingPx = 16
    private lateinit var tableAdapter: TableRowAdapter




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

        tableAdapter = TableRowAdapter(
            rowTextColor,
            textSizePx,
            cellPaddingPx,
            rowBackground
        )

        recyclerView.adapter = tableAdapter

        addView(headerLayout)
        addView(recyclerView)
    }


    private fun readAttributes(context: Context, attrs: AttributeSet?) {
        if (attrs == null) return

        val ta = context.obtainStyledAttributes(
            attrs,
            R.styleable.SortableTableView
        )

        headerTextColor = ta.getColor(
            R.styleable.SortableTableView_stv_headerTextColor,
            Color.BLACK
        )

        rowTextColor = ta.getColor(
            R.styleable.SortableTableView_stv_rowTextColor,
            Color.DKGRAY
        )

        textSizePx = ta.getDimension(
            R.styleable.SortableTableView_stv_textSize,
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                14f,
                resources.displayMetrics
            )
        )

        cellPaddingPx = ta.getDimensionPixelSize(
            R.styleable.SortableTableView_stv_cellPadding,
            16
        )

        headerBackground = ta.getDrawable(
            R.styleable.SortableTableView_stv_headerBackground
        )

        rowBackground = ta.getDrawable(
            R.styleable.SortableTableView_stv_rowBackground
        )

        ta.recycle()
    }


    fun setHeaders(headers: List<String>) {
        headerLayout.removeAllViews()
        headerViews.clear()

        headers.forEachIndexed { index, title ->
            val textView = TextView(context).apply {
                text = title
                setTextColor(headerTextColor)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx)
                setPadding(cellPaddingPx, cellPaddingPx, cellPaddingPx, cellPaddingPx)
                layoutParams = LayoutParams(0, WRAP_CONTENT, 1f)
                setTypeface(typeface, Typeface.BOLD)
                setOnClickListener {
                    handleSort(index)
                    updateSortIcons()
                }
            }

            headerViews.add(textView)
            headerLayout.addView(textView)
        }
        headerBackground?.let {
            headerLayout.background = it
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
