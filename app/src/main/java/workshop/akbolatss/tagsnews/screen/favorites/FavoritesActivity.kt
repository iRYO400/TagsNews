package workshop.akbolatss.tagsnews.screen.favorites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_favorites.*
import me.toptas.rssconverter.RssItem
import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.base.BaseActivity
import workshop.akbolatss.tagsnews.di.component.DaggerFavoritesComponent
import workshop.akbolatss.tagsnews.di.module.FavoritesModule
import workshop.akbolatss.tagsnews.screen.details.DetailsActivity
import workshop.akbolatss.tagsnews.screen.news.NewsAdapter
import javax.inject.Inject

class FavoritesActivity : BaseActivity(), FavoritesView, NewsAdapter.NewsListener {

    @Inject
    lateinit var mPresenter: FavoritesPresenter

    @Inject
    lateinit var mContext: Context

    private var mNewsAdapter: NewsAdapter? = null

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {
        super.onViewReady(savedInstanceState, intent)
        onInitRecycler()
        mPresenter.onLoadFavorites()
    }

    private fun onInitRecycler() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.isNestedScrollingEnabled = false

        mNewsAdapter = NewsAdapter(this)
        recyclerView.adapter = mNewsAdapter
    }

    override fun onItemClick(rssItem: RssItem) {
        val intent = Intent(this@FavoritesActivity, DetailsActivity::class.java)
        intent.putExtra("RssItem", rssItem)
        startActivity(intent)
    }

    override fun onLoadFavorites(rssItems: List<RssItem>) {
        mNewsAdapter!!.onAddItems(rssItems)
    }

    override fun onShowLoading() {

    }

    override fun onHideLoading() {

    }

    override fun onNoContent(isEmpty: Boolean) {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onInitDagger() {
        DaggerFavoritesComponent.builder()
                .appComponent(appComponent)
                .favoritesModule(FavoritesModule(this))
                .build()
                .inject(this)
    }

    override fun getContentView(): Int {
        return R.layout.activity_favorites
    }
}
