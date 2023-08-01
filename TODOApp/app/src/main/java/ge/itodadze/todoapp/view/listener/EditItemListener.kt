package ge.itodadze.todoapp.view.listener

interface EditItemListener {
    fun itemChecked(position: Int)
    fun itemActivated(position: Int)
    fun itemDeleteRequest(position: Int)
    fun itemTextChanged(position: Int, text: String)
    fun itemDeactivated()
}