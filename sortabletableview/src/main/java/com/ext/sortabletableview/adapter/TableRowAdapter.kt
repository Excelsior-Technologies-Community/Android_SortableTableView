package com.ext.sortabletableview.adapter

import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ext.sortabletableview.R
import com.ext.sortabletableview.model.TableRowData

class TableRowAdapter(
    private val textColor: Int,
    private val textSizePx: Float,
    private val paddingPx: Int,
    private val rowBackground: Drawable?
) : RecyclerView.Adapter<TableRowAdapter.RowViewHolder>() {


    private var data: List<TableRowData> = emptyList()

    fun submitData(newData: List<TableRowData>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_table_row, parent, false) as LinearLayout
        return RowViewHolder(layout)
    }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    inner class RowViewHolder(private val rowLayout: LinearLayout) :
        RecyclerView.ViewHolder(rowLayout) {

        fun bind(rowData: TableRowData) {
            rowLayout.removeAllViews()

            rowData.cells.forEach { cell ->
                val tv = TextView(rowLayout.context).apply {
                    text = cell.toString()
                    setTextColor(textColor)
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx)
                    setPadding(paddingPx, paddingPx, paddingPx, paddingPx)
                    layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f)
                }
                rowLayout.addView(tv)
                rowBackground?.let {
                    rowLayout.background = it
                }
            }
        }
    }
}
