package ge.itodadze.todoapp.viewmodel.models

import ge.itodadze.todoapp.data.entity.Item

data class ViewItem(val text: String, val checked: Boolean) {

    companion object {
        fun getFromItem(item: Item): ViewItem {
            return ViewItem(item.text, item.checked)
        }
    }
}