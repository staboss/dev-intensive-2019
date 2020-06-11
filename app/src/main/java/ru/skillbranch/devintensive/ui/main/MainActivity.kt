package ru.skillbranch.devintensive.ui.main

import android.content.Intent
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.R
//import ru.skillbranch.devintensive.extensions.createSnackBar
import ru.skillbranch.devintensive.extensions.dp
import ru.skillbranch.devintensive.extensions.getModeColor
import ru.skillbranch.devintensive.ui.adapters.ChatAdapter
import ru.skillbranch.devintensive.ui.adapters.ChatItemTouchHelperCallback
import ru.skillbranch.devintensive.ui.group.GroupActivity
import ru.skillbranch.devintensive.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var chatAdapter: ChatAdapter

    private val viewModelFactory = AndroidViewModelFactory.getInstance(application)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolbar()
        initViews()
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.queryHint = "Введите имя пользователя"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.handleSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.handleSearchQuery(newText)
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun initViews() {
        chatAdapter = ChatAdapter {
            Snackbar.make(rv_chat_list, "Click on ${it.title}", Snackbar.LENGTH_SHORT).apply {
                setTextColor(getModeColor(R.attr.colorSnackBarText))
                view.setBackgroundResource(R.drawable.bg_snackbar)
            }.show()
//            rv_chat_list.createSnackBar(
//                message = "Click on ${it.title}",
//                textColor = getModeColor(R.attr.colorSnackBarText),
//                backgroundResource = R.drawable.bg_snackbar
//            ).show()
        }

        val myDivider = resources.getDrawable(R.drawable.divider_chat_list, theme)
        val myDividerWithMargin = InsetDrawable(myDivider, 72.dp, 0, 0, 0)
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        divider.setDrawable(myDividerWithMargin)

        rv_chat_list.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(divider)
        }

        val touchCallback = ChatItemTouchHelperCallback(chatAdapter) {
            val id = it.id
            viewModel.addToArchive(id)

            Snackbar.make(rv_chat_list, "Вы точно хотите добавить ${it.title} в архив?", Snackbar.LENGTH_SHORT).apply {
                setTextColor(getModeColor(R.attr.colorSnackBarText))
                setActionTextColor(getModeColor(R.attr.colorSnackBarActionText))
                view.setBackgroundResource(R.drawable.bg_snackbar)
            }.show()

//            rv_chat_list.createSnackBar(
//                message = "Вы точно хотите добавить ${it.title} в архив?",
//                textColor = getModeColor(R.attr.colorSnackBarText),
//                textActionColor = getModeColor(R.attr.colorSnackBarActionText),
//                backgroundResource = R.drawable.bg_snackbar,
//                action = { viewModel.restoreFromArchive(id) }
//            ).show()
        }

        val touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(rv_chat_list)

        fab.setOnClickListener {
            val intent = Intent(this, GroupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initViewModel() {
        viewModel = viewModelFactory.create(MainViewModel::class.java)
        viewModel.getChatData().observe(this, Observer { chatAdapter.updateData(it) })
    }
}