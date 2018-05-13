package workshop.akbolatss.tagsnews.screen.details

import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.stfalcon.frescoimageviewer.ImageViewer
import kotlinx.android.synthetic.main.fragment_details.*
import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.application.App
import workshop.akbolatss.tagsnews.di.component.DaggerDetailsComponent
import workshop.akbolatss.tagsnews.di.module.DetailsModule
import workshop.akbolatss.tagsnews.model.dao.RssFeedItem
import workshop.akbolatss.tagsnews.model.dao.RssSource
import workshop.akbolatss.tagsnews.util.Constants.FB_PACKAGE_NAME
import workshop.akbolatss.tagsnews.util.Constants.SHARE_INFO
import workshop.akbolatss.tagsnews.util.Constants.TW_PACKAGE_NAME
import workshop.akbolatss.tagsnews.util.Constants.VK_PACKAGE_NAME
import workshop.akbolatss.tagsnews.util.UtilityMethods
import javax.inject.Inject

/**
 * Details for RSS Feed Item
 */
class DetailsFragment : Fragment(), DetailsView {

    @Inject
    lateinit var mPresenter: DetailsPresenter

    companion object {

        private const val PARAM_RSS_ITEM = "RssItemFragment"

        fun newInstance(rssItem: RssFeedItem): DetailsFragment {
            val fragment = DetailsFragment()
            val args = Bundle()
            args.putParcelable(PARAM_RSS_ITEM, rssItem)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDagger()

        mPresenter.mRssFeedItem = arguments!!.getParcelable(PARAM_RSS_ITEM) as RssFeedItem

        initView()
        initListeners()
    }

    private fun initDagger() {
        DaggerDetailsComponent.builder()
                .appComponent((context!!.applicationContext as App).appComponent)
                .detailsModule(DetailsModule(this))
                .build()
                .inject(this)
    }

    /**
     * Init text values, images for View
     */
    private fun initView() {
        tvTitle.text = mPresenter.mRssFeedItem!!.title
        if (mPresenter.mRssFeedItem!!.pubDate != null && mPresenter.mRssFeedItem!!.pubDate.isNotEmpty())
            tvTimestamp.text = UtilityMethods.convertTime(mPresenter.mRssFeedItem!!.pubDate)
        tvDescription.text = mPresenter.mRssFeedItem!!.description

        mPresenter.getRssSource()

        Picasso.with(activity)
                .load(mPresenter.mRssFeedItem!!.image)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(imgView)

        if (mPresenter.checkInFavorites()) {
            btnFavorite.setImageResource(R.drawable.ic_favorite_24dp)
        } else {
            btnFavorite.setImageResource(R.drawable.ic_favorite_border_24dp)
        }
//        val adRequest = AdRequest.Builder().build() //TODO Turn before production
//        adView.loadAd(adRequest)
    }

    /**
     * RSS source details
     */
    override fun loadRssSource(rssSource: RssSource?) {
        tvSourceName.text = rssSource?.title
    }

    /**
     * Init listeners for buttons
     */
    private fun initListeners() {
        flImage.setOnClickListener {
            ImageViewer.Builder(activity, arrayListOf(mPresenter.mRssFeedItem!!.image))
                    .setStartPosition(0)
                    .show()
        }
        btnOpenSource.setOnClickListener {
            onOpenSource()
        }

        btnVkShare.setOnClickListener {
            onShareVk()
        }

        btnFbShare.setOnClickListener {
            onShareFb()
        }

        btnTwShare.setOnClickListener {
            onShareTw()
        }

        btnShare.setOnClickListener {
            onShare()
        }

        btnFavorite.setOnClickListener {
            mPresenter.onAddToFavorites()
        }
    }

    /**
     * Share current RSS feed using any acceptable app
     */
    override fun onShare() {
        val messageSend = mPresenter.mRssFeedItem!!.title + "\n\n" + mPresenter.mRssFeedItem!!.getLink() + " \n\n---\n" + SHARE_INFO
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, messageSend)
        startActivity(intent)
    }

    /**
     * Share directly, if exists on VK.com app or open Play Market
     */
    override fun onShareVk() {
        val messageSend = mPresenter.mRssFeedItem!!.title + "\n\n" + mPresenter.mRssFeedItem!!.link + " \n\n---\n" + SHARE_INFO
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, messageSend)
        var socialAppFound = false
        val matches: List<ResolveInfo> = activity!!.packageManager.queryIntentActivities(intent, 0)
        for (info in matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith(VK_PACKAGE_NAME)) {
                intent.`package` = info.activityInfo.packageName
                socialAppFound = true
                break
            }
        }

        if (socialAppFound) {
            startActivity(intent)
        } else {
            onShareWithWebIntent(VK_PACKAGE_NAME)
        }
    }

    /**
     * Share directly, if exists on Facebook app or open Play Market
     */
    override fun onShareFb() {
        val messageSend = mPresenter.mRssFeedItem!!.title + "\n\n" + mPresenter.mRssFeedItem!!.link + " \n\n---\n" + SHARE_INFO
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, messageSend)
        var socialAppFound = false
        val matches: List<ResolveInfo> = activity!!.packageManager.queryIntentActivities(intent, 0)
        for (info in matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith(FB_PACKAGE_NAME)) {
                intent.`package` = info.activityInfo.packageName
                socialAppFound = true
                break
            }
        }
        if (socialAppFound) {
            startActivity(intent)
        } else {
            onShareWithWebIntent(FB_PACKAGE_NAME)
        }
    }

    /**
     * Share directly, if exists on Twitter app or open Play Market
     */
    override fun onShareTw() {
        val messageSend = mPresenter.mRssFeedItem!!.title + "\n\n" + mPresenter.mRssFeedItem!!.link + " \n\n---\n" + SHARE_INFO
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, messageSend)
        var socialAppFound = false
        val matches: List<ResolveInfo> = activity!!.packageManager.queryIntentActivities(intent, 0)
        for (info in matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith(TW_PACKAGE_NAME)) {
                intent.`package` = info.activityInfo.packageName
                socialAppFound = true
                break
            }
        }
        if (socialAppFound) {
            startActivity(intent)
        } else {
            onShareWithWebIntent(TW_PACKAGE_NAME)
        }
    }

    /**
     * Helper to open Play Market
     */
    override fun onShareWithWebIntent(socialNetworkId: String) {
        val shareUrl = "market://details?id=$socialNetworkId"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(shareUrl))
        startActivity(intent)
    }

    /**
     * Open #BrowserFragment
     */
    override fun onOpenSource() {
        (activity as DetailsActivity).onOpenSource()
    }

    /**
     * Update Toolbar state
     */
    override fun onRefreshToolbar(isFavorite: Boolean) {
        if (isFavorite) {
            btnFavorite.setImageResource(R.drawable.ic_favorite_24dp)
        } else {
            btnFavorite.setImageResource(R.drawable.ic_favorite_border_24dp)
        }
    }
}