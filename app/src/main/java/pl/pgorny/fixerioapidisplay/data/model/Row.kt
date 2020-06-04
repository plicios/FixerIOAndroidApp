package pl.pgorny.fixerioapidisplay.data.model

import androidx.recyclerview.widget.DiffUtil

abstract class Row {
    companion object{
        val diffUtilCallback: DiffUtil.ItemCallback<Row> = object : DiffUtil.ItemCallback<Row>() {
            override fun areItemsTheSame(row1: Row, row2: Row): Boolean {
                return row1 === row2
            }

            override fun areContentsTheSame(row1: Row, row2: Row): Boolean {
                return row1 == row2
            }
        }
    }
}