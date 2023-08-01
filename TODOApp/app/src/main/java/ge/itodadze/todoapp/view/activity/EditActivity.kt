package ge.itodadze.todoapp.view.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import ge.itodadze.todoapp.R
import ge.itodadze.todoapp.databinding.ActivityEditBinding
import ge.itodadze.todoapp.viewmodel.EditViewModel
import ge.itodadze.todoapp.view.adapter.CheckedEditItemsAdapter
import ge.itodadze.todoapp.view.adapter.UncheckedEditItemsAdapter
import ge.itodadze.todoapp.view.listener.EditItemListener

class EditActivity : AppCompatActivity(), EditItemListener {

    private val viewModel: EditViewModel by viewModels {
        EditViewModel.getEditViewModelFactory(
            applicationContext
        )
    }

    private lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerAdapters()

        registerObservers()

        registerListeners()

        loadCurrentItem()
    }

    override fun itemChecked(position: Int) {
        viewModel.checkAtPosition(position)
    }

    override fun itemActivated(position: Int) {
        (binding.uncheckedItems.adapter as UncheckedEditItemsAdapter).update(position)
    }

    override fun itemDeleteRequest(position: Int) {
        (binding.uncheckedItems.adapter as UncheckedEditItemsAdapter).update(null)
        viewModel.deleteAtPosition(position)
    }

    override fun itemTextChanged(position: Int, text: String) {
        viewModel.updateTextAt(position, text)
    }

    override fun itemDeactivated() {
        viewModel.itemSaved()
    }

    private fun registerAdapters() {
        binding.uncheckedItems.adapter = UncheckedEditItemsAdapter(ArrayList(), this, null)
        binding.checkedItems.adapter = CheckedEditItemsAdapter(ArrayList())
    }

    private fun registerObservers() {
        viewModel.title.observe(this) {
            binding.todoName.setText(it)
        }

        viewModel.pin.observe(this) {
            if (it) {
                binding.pinButton.setImageResource(R.drawable.ic_pinned)
            } else {
                binding.pinButton.setImageResource(R.drawable.ic_pin)
            }
        }

        viewModel.unchecked.observe(this) {
            binding.uncheckedItems.layoutManager?.removeAllViews()
            (binding.uncheckedItems.adapter as UncheckedEditItemsAdapter).update(it)
        }

        viewModel.checked.observe(this) {
            binding.checkedItems.layoutManager?.removeAllViews()
            binding.divider.visibility = if (it.isNotEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
            (binding.checkedItems.adapter as CheckedEditItemsAdapter).update(it)
        }

        viewModel.newActive.observe(this) {
            itemActivated(it)
        }
    }

    private fun registerListeners() {
        binding.todoName.doOnTextChanged {
                text, _, _, _ -> viewModel.changeTitle(text.toString())
        }

        binding.backButton.setOnClickListener {
            toMain()
        }

        onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                toMain()
            }
        })

        binding.pinButton.setOnClickListener {
            viewModel.pinUpdate()
        }

        binding.newItemButton.addButton.setOnClickListener {
            viewModel.addItem()
        }
    }

    private fun loadCurrentItem() {
        val id: Int = intent.getIntExtra(resources.getString(R.string.todo_name_key), 0)
        viewModel.todoId(id)
    }

    private fun toMain() {
        viewModel.saveData()
        setResult(Activity.RESULT_OK)
        finish()
    }

}