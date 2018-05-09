package workshop.akbolatss.tagsnews.screen.favorites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_favorites.*
import me.toptas.rssconverter.RssItem
import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.base.BaseActivity
import workshop.akbolatss.tagsnews.di.component.DaggerFavoritesComponent
import workshop.akbolatss.tagsnews.di.module.FavoritesModule
import workshop.akbolatss.tagsnews.model.dao.RssFeedItem
import workshop.akbolatss.tagsnews.screen.details.DetailsActivity
import workshop.akbolatss.tagsnews.screen.news.NewsAdapter
import workshop.akbolatss.tagsnews.util.Constants
import javax.inject.Inject

/**
 * Activity that load all favorite RSS feeds
 */
class FavoritesActivity : BaseActivity(), FavoritesView, FavoritesAdapter.FavoritesListener {

    @Inject
    lateinit var mPresenter: FavoritesPresenter

    @Inject
    lateinit var mContext: Context

    private var mFavoritesAdapter: FavoritesAdapter? = null

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

        mFavoritesAdapter = FavoritesAdapter(this)
        recyclerView.adapter = mFavoritesAdapter
    }

    /**
     * Opens #DetailsActivity with extra info
     * @param rssFeedItem RSS feed item
     */
    override fun onItemClick(rssFeedItem: RssFeedItem) {
        val intent = Intent(this@FavoritesActivity, DetailsActivity::class.java)
        intent.putExtra(Constants.INTENT_RSS_FEED_ITEM, rssFeedItem)
        startActivity(intent)
    }

    /**
     * Load items to RecyclerView.Adapter
     */
    override fun onLoadFavorites(rssItems: List<RssFeedItem>) {
        mFavoritesAdapter!!.onAddItems(rssItems)
    }

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
