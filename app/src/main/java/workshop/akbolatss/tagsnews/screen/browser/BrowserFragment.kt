package workshop.akbolatss.tagsnews.screen.browser

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_browser.*
import me.toptas.rssconverter.RssItem
import vcm.github.webkit.proview.ProWebView
import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.screen.details.DetailsActivity
import workshop.akbolatss.tagsnews.util.UtilityMethods.isWifiConnected

/**
 * Author: Akbolat Sadvakassov
 * Date: 28.04.2018
 */
class BrowserFragment : Fragment(), BrowserView {

    private var mRssItem: RssItem? = null
    private var mCurrPageTitle = ""
    private var mCurrPageUrl = ""

    private var isUrlStartLoading = false

    companion object {

        private const val PARAM_TYPE = "RssItemFragment"

        fun newInstance(rssItem: RssItem): BrowserFragment {
            val fragment = BrowserFragment()
            val args = Bundle()
            args.putSerializable(PARAM_TYPE, rssItem)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_browser, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mRssItem = arguments!!.getSerializable(PARAM_TYPE) as RssItem

//        Log.d(BrowserFragment::class.java.simpleName, "onViewCreated")
        initView()
        initListeners()
    }

    private fun initView() {

        proWebView.setActivity(activity)
        proWebView.settings.domStorageEnabled = true
        proWebView.setThirdPartyCookiesEnabled(true)
        proWebView.setProClient(object : ProWebView.ProClient() {
            override fun onInformationReceived(webView: ProWebView?, url: String?, title: String?, favicon: Bitmap?) {
                mCurrPageUrl = url!!
                mCurrPageTitle = title!!
            }

            override fun onProgressChanged(webView: ProWebView?, progress: Int) {
//                pageProgress.progress = progress
            }

            override fun shouldOverrideUrlLoading(webView: ProWebView?, url: String?): Boolean {
                Log.d("TAG", "URL $url")
                return true
            }
        })

        if (isWifiConnected(context!!)) {
            proWebView.loadUrl(mRssItem!!.link)
            isUrlStartLoading = true
        }
    }

    private fun initListeners() {
        btnClose.setOnClickListener {
            (activity as DetailsActivity).onOpenDetails()
        }
        btnBack.setOnClickListener {
            proWebView.tryGoBack()
        }
        btnForward.setOnClickListener {
            proWebView.tryGoForward()
        }
        btnReload.setOnClickListener {
            proWebView.reload()
        }
        btnBrowser.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mRssItem!!.link))
            startActivity(intent)
        }
        btnShareCurrent.setOnClickListener {
            val messageSend = "$mCurrPageTitle\n\n$mCurrPageUrl \n\n---\nTag News (Beta) bit.ly/TagNewsApp"
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_TEXT, messageSend)
            shareIntent.type = "text/plain"
            startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.tvShare)))
        }
    }

    override fun onResume() {
        super.onResume()
        proWebView.onResume()
        if (!isUrlStartLoading) {
            proWebView.loadUrl(mRssItem!!.link)
            isUrlStartLoading = true
        }
    }

    override fun onPause() {
        proWebView.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        proWebView.onDestroy()
        super.onDestroyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        proWebView.onActivityResult(requestCode, resultCode, data)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        proWebView.onRestoreInstanceState(savedInstanceState)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        proWebView.onRequestPermissionResult(requestCode, permissions, grantResults)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        proWebView.onSavedInstanceState(outState)
    }
}