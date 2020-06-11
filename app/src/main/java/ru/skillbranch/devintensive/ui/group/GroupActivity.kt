package ru.skillbranch.devintensive.ui.group

import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_group.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.dp
import ru.skillbranch.devintensive.extensions.getModeColor
import ru.skillbranch.devintensive.models.data.UserItem
import ru.skillbranch.devintensive.ui.adapters.UserAdapter
import ru.skillbranch.devintensive.ui.custom.AvatarImageView
import ru.skillbranch.devintensive.viewmodels.GroupViewModel

class GroupActivity : AppCompatActivity() {

    private lateinit var usersAdapter: UserAdapter
    private lateinit var viewModel: GroupViewModel

    private val viewModelFactory =
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        if (item.itemId == android.R.id.home) {
            finish()
            overridePendingTransition(R.anim.idle, R.anim.bottom_down)
            true
        } else {
            super.onOptionsItemSelected(item)
        }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initViews() {
        title = "Создание группы"

        usersAdapter = UserAdapter { userItem -> viewModel.handleSelectedItems(userItem.id) }

        val myDivider = resources.getDrawable(R.drawable.divider_chat_list, theme)
        val myDividerWithMargin = InsetDrawable(myDivider, 72.dp, 0, 0, 0)
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        divider.setDrawable(myDividerWithMargin)

        rv_user_list.apply {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(this@GroupActivity)
            addItemDecoration(divider)
        }

        fab.setOnClickListener {
            viewModel.handleCreateGroup()
            finish()
            overridePendingTransition(R.anim.idle, R.anim.bottom_down)
        }
    }

    private fun initViewModel() {
        viewModel = viewModelFactory.create(GroupViewModel::class.java)
        viewModel.getUsersData().observe(this, Observer { usersAdapter.updateData(it) })
        viewModel.getSelectedData().observe(this, Observer {
            updateChips(it)
            toggleFab(it.size > 1)
        })
    }

    private fun toggleFab(isShow: Boolean) {
        if (isShow) fab.show()
        else fab.hide()
    }

    private fun addChipToGroup(user: UserItem) {
        val chip = Chip(this).apply {
            Glide.with(this).load(user.avatar)
                .circleCrop()
                .into(object : CustomTarget<Drawable>() {
                    override fun onLoadCleared(placeholder: Drawable?) {
                        chipIcon = placeholder
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        chipIcon = drawChipAvatar(user.initials ?: "??")
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        transition: com.bumptech.glide.request.transition.Transition<in Drawable>?
                    ) {
                        chipIcon = resource
                    }
                })

            text = user.fullName
            isCloseIconVisible = true
            tag = user.id
            isClickable = true
            closeIconTint = ColorStateList.valueOf(getModeColor(R.attr.colorChipCloseTint))
            chipBackgroundColor = ColorStateList.valueOf(getModeColor(R.attr.colorChipBackground))
            setTextColor(Color.WHITE)
        }

        chip.setOnCloseIconClickListener { viewModel.handleRemoveChip(it.tag.toString()) }
        chip_group.addView(chip)
    }

    private fun updateChips(listUsers: List<UserItem>) {
        chip_group.visibility = if (listUsers.isEmpty()) View.GONE else View.VISIBLE

        val users = listUsers
            .associateBy { user -> user.id }
            .toMutableMap()

        val views = chip_group.children.associateBy { view -> view.tag }

        views.forEach { (k, v) ->
            if (!users.containsKey(k)) {
                chip_group.removeView(v)
            } else {
                users.remove(k)
            }
        }

        users.forEach { (_, v) -> addChipToGroup(v) }
    }

    private fun drawChipAvatar(initials: String): Drawable {
        val paint = Paint().apply {
            isAntiAlias = true
            this.textSize = 18f
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
        }

        val textBounds = Rect()
        paint.getTextBounds(initials, 0, initials.length, textBounds)

        val backgroundBounds = RectF()
        backgroundBounds.set(0f, 0f, 40f, 40f)

        val textBottom = backgroundBounds.centerY() - textBounds.exactCenterY()

        val bitmap = Bitmap.createBitmap(40, 40, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        paint.color = AvatarImageView.initialsToColor(initials)
        canvas.drawCircle(20f, 20f, 20f, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        paint.color = Color.WHITE

        canvas.drawText(initials, backgroundBounds.centerX(), textBottom, paint)
        return bitmap.toDrawable(resources)
    }
}