package ge.itodadze.todoapp.view.activity

import ge.itodadze.todoapp.view.decorations.StaggeredGridSpacingItemDecoration
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.doOnTextChanged
import ge.itodadze.todoapp.R
import ge.itodadze.todoapp.databinding.ActivityMainBinding
import ge.itodadze.todoapp.viewmodel.MainViewModel
import ge.itodadze.todoapp.presenter.model.ToDoWithList
import ge.itodadze.todoapp.view.adapter.MainItemsAdapter
import ge.itodadze.todoapp.view.listener.ListItemClickListener


class MainActivity : AppCompatActivity(), ListItemClickListener {

    private val viewModel: MainViewModel by viewModels {
        MainViewModel.getMainViewModelFactory(
            applicationContext
        )
    }

    private lateinit var binding: ActivityMainBinding

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode != Activity.RESULT_OK) {
            Toast.makeText(applicationContext, resources.getString(R.string.result_error), Toast.LENGTH_SHORT)
        }
        viewModel.update("")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        registerAdapters()

        registerObservers()

        registerListeners()

        show()
    }

    override fun listItemClicked(toDoItem: ToDoWithList) {
        startEditActivity(toDoItem.todo.id)
    }

    private fun registerAdapters() {
        binding.pinnedItems.adapter = MainItemsAdapter(ArrayList(), this)
        binding.pinnedItems.addItemDecoration(
            StaggeredGridSpacingItemDecoration(
            resources.getDimensionPixelOffset(R.dimen.inner_margin) / 2,
            resources.getDimensionPixelSize(R.dimen.list_vertical_margin) / 2
        )
        )
        binding.otherItems.adapter = MainItemsAdapter(ArrayList(), this)
        binding.otherItems.addItemDecoration(
            StaggeredGridSpacingItemDecoration(
            resources.getDimensionPixelOffset(R.dimen.inner_margin) / 2,
            resources.getDimensionPixelSize(R.dimen.list_vertical_margin) / 2
        )
        )
    }

    private fun registerObservers() {
        viewModel.pinned.observe(this) {
            binding.pinnedItems.layoutManager?.removeAllViews()
            (binding.pinnedItems.adapter as MainItemsAdapter).update(it)
            binding.pinnedTitle.visibility = getVisibilityAccordingTo(it)
            binding.othersTitle.visibility = getVisibilityAccordingTo(it)
        }

        viewModel.others.observe(this) {
            binding.pinnedItems.layoutManager?.removeAllViews()
            (binding.otherItems.adapter as MainItemsAdapter).update(it)
        }
    }

    private fun registerListeners() {
        binding.filterText.doOnTextChanged {
                text, _, _, _ -> viewModel.update(text.toString())
        }

        binding.newListButton.setOnClickListener {
            startEditActivity(null)
        }
    }

    private fun show() {
        viewModel.update("")
    }

    private fun getVisibilityAccordingTo(item: List<ToDoWithList>): Int {
        if (item.isNotEmpty()) return View.VISIBLE
        return View.GONE
    }

    private fun startEditActivity(extraValue: Int?) {
        intent = Intent(this, EditActivity::class.java)
        intent.putExtra(resources.getString(R.string.todo_name_key), extraValue)
        launcher.launch(intent)
    }

}
