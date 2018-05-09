package workshop.akbolatss.tagsnews.screen.news

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_news.*
import me.toptas.rssconverter.RssFeed
import me.toptas.rssconverter.RssItem
import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.application.App
import workshop.akbolatss.tagsnews.di.component.DaggerNewsComponent
import workshop.akbolatss.tagsnews.di.module.NewsListModule
import workshop.akbolatss.tagsnews.screen.details.DetailsActivity
import workshop.akbolatss.tagsnews.util.Constants.INTENT_RSS_FEED_ITEM
import javax.inject.Inject

/**
 * Fragment with list of loaded RSS feeds. Creates in #BoardActivity
 * @see BoardActivity
 */
class NewsFragment : Fragment(), NewsView, SwipeRefreshLayout.OnRefreshListener, NewsAdapter.NewsListener {

    @Inject
    lateinit var mPresenter: NewsPresenter

    @Inject
    lateinit var mContext: Context

    private var mNewsAdapter: NewsAdapter? = null

    /**
     * Rss Source ID
     */
    private var mRssSourceId: Long? = null

    companion object {

        private const val PARAM_RSS_SOURCE_ID = "RssSourceID"
        private const val PARAM_RSS_URL = "RssSourceLink"

        fun newInstance(rssSourceId: Long, url: String): NewsFragment {
            val fragment = NewsFragment()
            val args = Bundle()
            args.putLong(PARAM_RSS_SOURCE_ID, rssSourceId)
            args.putString(PARAM_RSS_URL, url)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    private fun initDagger() {
        DaggerNewsComponent.builder()
                .appComponent((context!!.applicationContext as App).appComponent)
                .newsListModule(NewsListModule(this))
                .build()
                .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDagger()

        mRssSourceId = arguments!!.getLong(PARAM_RSS_SOURCE_ID)
        mPresenter.url = arguments!!.getString(PARAM_RSS_URL)

        onInitSwipeRefresh()
        onInitRecycler()

        mPresenter.onLoadNews()
    }

    private fun onInitSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.colorAccent)
        swipeRefresh.setOnRefreshListener(this)
    }

    private fun onInitRecycler() {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)

        mNewsAdapter = NewsAdapter(this)
        recyclerView.adapter = mNewsAdapter
    }

    /**
     * #SwipeRefreshLayout listener
     * @see android.support.v4.widget.SwipeRefreshLayout
     */
    override fun onRefresh() {
        mPresenter.onLoadNews()
    }

    /**
     * Show loading state
     */
    override fun onShowLoading() {
        swipeRefresh?.isRefreshing = true
    }

    /**
     * Hide loading state
     */
    override fun onHideLoading() {
        swipeRefresh?.isRefreshing = false
    }

    /**
     * Called when there are no content
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

    /**
     * Load items to RecyclerView.Adapter
     */
    override fun onLoadNews(rssFeed: RssFeed) {
        mNewsAdapter!!.onAddItems(rssFeed.items)
    }

    /**
     * Opens #DetailsActivity with extra info
     * @param rssItem that converted to RSS feed item
     */
    override fun onItemClick(rssItem: RssItem) {
        val intent = Intent(activity, DetailsActivity::class.java)
        intent.putExtra(INTENT_RSS_FEED_ITEM, mPresenter.mapRssFeed(rssItem, mRssSourceId))
        startActivity(intent)
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
