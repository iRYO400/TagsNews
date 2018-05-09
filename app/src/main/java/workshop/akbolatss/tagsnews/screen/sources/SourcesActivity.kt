package workshop.akbolatss.tagsnews.screen.sources

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sources.*
import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.base.BaseActivity
import workshop.akbolatss.tagsnews.di.component.DaggerSourcesComponent
import workshop.akbolatss.tagsnews.di.module.SourcesModule
import workshop.akbolatss.tagsnews.model.dao.RssSource
import workshop.akbolatss.tagsnews.screen.sources.helper.SimpleItemTouchHelperCallback
import javax.inject.Inject

/**
 * Activity for managing RSS sources, e.g. add, edit, remove
 */
class SourcesActivity : BaseActivity(), SourcesView, SourcesAdapter.SourceListener {

    @Inject
    lateinit var mContext: Context

    @Inject
    lateinit var mPresenter: SourcesPresenter

    private var mSourcesAdapter: SourcesAdapter? = null
    private var mItemTouchHelper: ItemTouchHelper? = null

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {
        super.onViewReady(savedInstanceState, intent)
        initToolbar()
        initRV()
        initListeners()
        mPresenter.loadSources()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun initListeners() {
        fabAdd.setOnClickListener {
            onAddNewSource()
        }
    }

    private fun initRV() {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)

        mSourcesAdapter = SourcesAdapter(this)
        recyclerView.adapter = mSourcesAdapter

        val callback = SimpleItemTouchHelperCallback(mSourcesAdapter)
        mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper!!.attachToRecyclerView(recyclerView)
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        mItemTouchHelper!!.startDrag(viewHolder)
    }

    /**
     * Add manually new RSS source
     */
    override fun onAddNewSource() {
        val layoutInflater = LayoutInflater.from(this@SourcesActivity)
        val subView = layoutInflater.inflate(R.layout.dialog_new_source, null)
        val etLink = subView.findViewById<EditText>(R.id.etLink)

        val builder = AlertDialog.Builder(this, R.style.Dialog)

        builder.setTitle(R.string.tvEnter)
        builder.setView(subView)
        builder.setPositiveButton(R.string.tvAdd) { dialogInterface, i ->
            val rssSource = RssSource()
            rssSource.isActive = true
            rssSource.title = etLink.text.toString().trim()
            rssSource.link = etLink.text.toString().trim()
            if (mPresenter.checkInSources(rssSource)) {
                Toast.makeText(this, resources.getString(R.string.rss_exists), Toast.LENGTH_LONG).show()
            } else {
                mPresenter.addNewSource(rssSource)

                mSourcesAdapter!!.onAddItem(rssSource)
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton(R.string.tvCancel) { dialogInterface, i -> dialogInterface.cancel() }
        builder.show()
    }

    /**
     * Load list of all RSS sources to Adapter
     */
    override fun onLoadSources(rssSourceList: List<RssSource>) {
        mSourcesAdapter!!.onAddItems(rssSourceList)
    }

    /**
     * Swap RSS items
     */
    override fun onItemsSwapped(from: RssSource, to: RssSource) {
        mPresenter.swapPositions(from, to)
    }

    /**
     * RSS source options. Edit name, edit link and remove
     */
    override fun onSourceOptions(rssSource: RssSource, view: View, pos: Int) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.menu_popup_source)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.mRename -> {
                    val layoutInflater = LayoutInflater.from(this@SourcesActivity)
                    val subView = layoutInflater.inflate(R.layout.dialog_new_source, null)
                    val etLink = subView.findViewById<EditText>(R.id.etLink)

                    etLink.setText(rssSource.title)
                    etLink.setHint(R.string.tvNewName)
                    val builder = AlertDialog.Builder(this@SourcesActivity, R.style.Dialog)

                    builder.setTitle(R.string.tvEnter)
                    builder.setView(subView)
                    builder.setPositiveButton(R.string.tvEdit) { dialogInterface, i ->
                        rssSource.title = etLink.text.toString()
                        mPresenter.updateSource(rssSource)
                        mSourcesAdapter!!.onUpdateItem(rssSource, pos)
                        dialogInterface.dismiss()
                    }
                    builder.setNegativeButton(R.string.tvCancel) { dialogInterface, i -> dialogInterface.cancel() }
                    builder.show()
                    return@OnMenuItemClickListener true
                }
                R.id.mReaddress -> {
                    val layoutInflater2 = LayoutInflater.from(this@SourcesActivity)
                    val subView2 = layoutInflater2.inflate(R.layout.dialog_new_source, null)
                    val etLink2 = subView2.findViewById<EditText>(R.id.etLink)

                    etLink2.setText(rssSource.link)
                    etLink2.setHint(R.string.tvNewAddress)
                    val builder2 = AlertDialog.Builder(this@SourcesActivity, R.style.Dialog)

                    builder2.setTitle(R.string.tvEnter)
                    builder2.setView(subView2)
                    builder2.setPositiveButton(R.string.tvEdit) { dialogInterface, i ->
                        rssSource.link = etLink2.text.toString()
                        mPresenter.updateSource(rssSource)
                        mSourcesAdapter!!.onUpdateItem(rssSource, pos)
                        dialogInterface.dismiss()
                    }
                    builder2.setNegativeButton(R.string.tvCancel) { dialogInterface, i -> dialogInterface.cancel() }
                    builder2.show()
                    return@OnMenuItemClickListener true
                }
                R.id.mRemove -> {
                    mPresenter.removeSource(rssSource)
                    mSourcesAdapter!!.onRemoveItem(pos)
                    return@OnMenuItemClickListener true
                }
            }
            false
        })
        popupMenu.show()
    }

    /**
     * Activate/deactivate RSS source in #BoardActivity
     * @see BoardActivity
     */
    override fun onSourceSwitch(rssSource: RssSource, isActive: Boolean, pos: Int) {
        rssSource.isActive = isActive
        mPresenter.updateSource(rssSource)
    }

    /**
     * Show loading state
     */
    override fun onShowLoading() {
        recyclerView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    /**
     * Hide loading state
     */
    override fun onHideLoading() {
        recyclerView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    /**
     * Show if there no content and vice-versa
     */
    override fun onNoContent(isEmpty: Boolean) {
        if (isEmpty) {
            recyclerView.visibility = View.GONE
            tvNoContent.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            tvNoContent.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onInitDagger() {
        DaggerSourcesComponent.builder()
                .appComponent(appComponent)
                .sourcesModule(SourcesModule(this))
                .build()
                .inject(this)
    }

    override fun getContentView(): Int {
        return R.layout.activity_sources
    }

    override fun onUnknownError(errorMessage: String) {
        Toast.makeText(mContext, R.string.unknown_error, Toast.LENGTH_LONG).show()
    }

    override fun onTimeout() {
        Toast.makeText(mContext, R.string.timeout_error, Toast.LENGTH_LONG).show()
    }

    override fun onNetworkError() {
        Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_LONG).show()
    }


}
