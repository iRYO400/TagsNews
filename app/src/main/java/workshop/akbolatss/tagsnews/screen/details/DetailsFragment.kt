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
import me.toptas.rssconverter.RssItem
import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.application.App
import workshop.akbolatss.tagsnews.di.component.DaggerDetailsComponent
import workshop.akbolatss.tagsnews.di.module.DetailsModule
import workshop.akbolatss.tagsnews.util.Constants.FB_PACKAGE_NAME
import workshop.akbolatss.tagsnews.util.Constants.TW_PACKAGE_NAME
import workshop.akbolatss.tagsnews.util.Constants.VK_PACKAGE_NAME
import workshop.akbolatss.tagsnews.util.UtilityMethods
import javax.inject.Inject

/**
 * Author: Akbolat Sadvakassov
 * Date: 28.04.2018
 */
class DetailsFragment : Fragment(), DetailsView {

    @Inject
    lateinit var mPresenter: DetailsPresenter

    companion object {

        private const val PARAM_TYPE = "RssItemFragment"

        fun newInstance(rssItem: RssItem): DetailsFragment {
            val fragment = DetailsFragment()
            val args = Bundle()
            args.putSerializable(PARAM_TYPE, rssItem)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DaggerDetailsComponent.builder()
                .appComponent((context!!.applicationContext as App).appComponent)
                .detailsModule(DetailsModule(this))
                .build()
                .inject(this)

        mPresenter.mRssItem = arguments!!.getSerializable(PARAM_TYPE) as RssItem

        initView()
        initListeners()
    }

    private fun initView() {
        tvTitle.text = mPresenter.mRssItem!!.title
        if (mPresenter.mRssItem!!.publishDate != null && mPresenter.mRssItem!!.publishDate.isNotEmpty())
            tvTimestamp.text = UtilityMethods.convertTime(mPresenter.mRssItem!!.publishDate)
        tvDescription.text = mPresenter.mRssItem!!.description

        Picasso.with(activity)
                .load(mPresenter.mRssItem!!.image)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(imgView)

        if (mPresenter.onCheckFavorites(mPresenter.mRssItem!!.publishDate)) {
            btnFavorite.setImageResource(R.drawable.ic_favorite_24dp)
        } else {
            btnFavorite.setImageResource(R.drawable.ic_favorite_border_24dp)
        }
//        val adRequest = AdRequest.Builder().build() //TODO Turn before production
//        adView.loadAd(adRequest)
    }

    private fun initListeners() {
        flImage.setOnClickListener {
            ImageViewer.Builder(activity, arrayListOf(mPresenter.mRssItem!!.image))
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

    override fun onShare() {
        val messageSend = mPresenter.mRssItem!!.title + "\n\n" + mPresenter.mRssItem!!.getLink() + " \n\n---\n" + "Tag News (Beta) bit.ly/TagNewsApp";
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, messageSend)
        startActivity(intent)
    }

    override fun onShareVk() {
        val messageSend = mPresenter.mRssItem!!.title + "\n\n" + mPresenter.mRssItem!!.link + " \n\n---\n" + "Tag News (Beta) bit.ly/TagNewsApp"
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

    override fun onShareFb() {
        val messageSend = mPresenter.mRssItem!!.getTitle() + "\n\n" + mPresenter.mRssItem!!.getLink() + " \n\n---\n" + "Tag News (Beta) bit.ly/TagNewsApp";
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

    override fun onShareTw() {
        val messageSend = mPresenter.mRssItem!!.title + "\n\n" + mPresenter.mRssItem!!.getLink() + " \n\n---\n" + "Tag News (Beta) bit.ly/TagNewsApp";
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

    override fun onShareWithWebIntent(socialNetworkId: String) {
        val shareUrl = "market://details?id=$socialNetworkId"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(shareUrl))
        startActivity(intent)
    }

    override fun onOpenSource() {
        (activity as DetailsActivity).onOpenSource()
    }

    override fun onRefreshToolbar(isFavorite: Boolean) {
        if (isFavorite) {
            btnFavorite.setImageResource(R.drawable.ic_favorite_24dp)
        } else {
            btnFavorite.setImageResource(R.drawable.ic_favorite_border_24dp)
        }
    }
}