package com.ext.sortabletableview.adapter

import com.ext.sortabletableview.model.TableRowData

class TableAdapter {
    var data: MutableList<TableRowData> = mutableListOf()

    fun sort(columnIndex: Int, ascending: Boolean) {
        data.sortWith { a, b ->
            val v1 = a.cells[columnIndex].toString()
            val v2 = b.cells[columnIndex].toString()
            if (ascending) v1.compareTo(v2) else v2.compareTo(v1)
        }
    }
}
