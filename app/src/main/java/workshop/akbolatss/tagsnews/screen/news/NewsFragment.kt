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

import javax.inject.Inject

import me.toptas.rssconverter.RssFeed
import me.toptas.rssconverter.RssItem
import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.application.App
import workshop.akbolatss.tagsnews.di.component.DaggerNewsComponent
import workshop.akbolatss.tagsnews.di.module.NewsListModule
import workshop.akbolatss.tagsnews.screen.details.DetailsActivity

class NewsFragment : Fragment(), NewsView, SwipeRefreshLayout.OnRefreshListener, NewsAdapter.NewsListener {

    @Inject
    lateinit var mPresenter: NewsPresenter

    @Inject
    lateinit var mContext: Context

    private var mNewsAdapter: NewsAdapter? = null

    /**
     * Name of RSS feed
     */
    private var mName: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    protected fun initDagger() {
        DaggerNewsComponent.builder()
                .appComponent((context!!.applicationContext as App).appComponent)
                .newsListModule(NewsListModule(this))
                .build()
                .inject(this)
    }

    private fun initDefault() {
        val bundle = arguments
        if (bundle != null) {
            for (i in 0..9) {
                if (bundle.containsKey(i.toString())) {
                    mName = bundle.getString(i.toString())
                }
                mPresenter.url = bundle.getString(mName)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDagger()
        initDefault()

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
        recyclerView.isNestedScrollingEnabled = false

        mNewsAdapter = NewsAdapter(this)
        recyclerView.adapter = mNewsAdapter
    }

    override fun onRefresh() {
        mPresenter.onLoadNews()
    }

    override fun onShowLoading() {
        swipeRefresh.isRefreshing = true
    }

    override fun onHideLoading() {
        swipeRefresh.isRefreshing = false
    }

    override fun onNoContent(isEmpty: Boolean) {
    }

    override fun onLoadNews(rssFeed: RssFeed) {
        mNewsAdapter!!.onAddItems(rssFeed.items)
    }

    override fun onItemClick(rssItem: RssItem) {
        val intent = Intent(activity, DetailsActivity::class.java)
        intent.putExtra("RssItem", rssItem)
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
    companion object {
        private val TAG = "NewsFragment"
    }
}
