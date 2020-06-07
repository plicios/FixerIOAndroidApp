package pl.pgorny.fixerioapidisplay.data.model

import androidx.recyclerview.widget.DiffUtil

abstract class ListItem {
    companion object{
        val diffUtilCallback: DiffUtil.ItemCallback<ListItem> = object : DiffUtil.ItemCallback<ListItem>() {
            override fun areItemsTheSame(listItem1: ListItem, listItem2: ListItem): Boolean {
                return listItem1 === listItem2
            }

            override fun areContentsTheSame(listItem1: ListItem, listItem2: ListItem): Boolean {
                return listItem1 == listItem2
            }
        }
    }
}