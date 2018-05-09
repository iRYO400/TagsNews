package workshop.akbolatss.tagsnews.screen.details

import android.annotation.TargetApi
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.webkit.WebView
import kotlinx.android.synthetic.main.fragment_browser.*
import vcm.github.webkit.proview.ProWebView
import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.model.dao.RssFeedItem
import workshop.akbolatss.tagsnews.util.UtilityMethods
import workshop.akbolatss.tagsnews.util.UtilityMethods.getDirection
import workshop.akbolatss.tagsnews.util.UtilityMethods.getDistance
import workshop.akbolatss.tagsnews.util.UtilityMethods.isWiFiConnected

/**
 * Fragment with WebView, where all urls are being loaded
 */
class BrowserFragment : Fragment() {

    /**
     * RssFeedItem object
     */
    private var mRssFeedItem: RssFeedItem? = null
    /**
     * Current loaded page title
     */
    private var mCurrPageTitle = ""
    /**
     * Current loaded page url
     */
    private var mCurrPageUrl = ""
    /**
     * Reference to View's distance in pixels a touch can wander before we think the user is scrolling
     */
    private var touchSlop: Int = 0
    /**
     * Old X coordinate touch value. Used for hide/unhide #Toolbar
     */
    private var startX: Float = 0f
    /**
     * Old Y coordinate touch value. Used for hide/unhide #Toolbar
     */
    private var startY: Float = 0f
    /**
     * Check if Url already loaded
     */
    private var isUrlStartLoading = false

    companion object {

        private const val PARAM_TYPE = "RssItemFragment"

        fun newInstance(rssItem: RssFeedItem): BrowserFragment {
            val fragment = BrowserFragment()
            val args = Bundle()
            args.putParcelable(PARAM_TYPE, rssItem)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_browser, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRssFeedItem = arguments!!.getParcelable(PARAM_TYPE) as RssFeedItem

        initView()
        initListeners()
    }

    /**
     * Init WebView's properties and listeners
     */
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
                if (pageProgress != null) {
                    pageProgress.progress = progress * 10
                    if (pageProgress.progress >= 1000) {
                        pageProgress.hideProgress(true)
                    }
                }
            }

            override fun shouldOverrideUrlLoading(webView: ProWebView?, url: String?): Boolean {
                Log.d("TAG", "URL $url")
                return false
            }
        })

        //Load if WiFi connected
        if (isWiFiConnected(context!!)) {
            proWebView.loadUrl(mRssFeedItem!!.link)
            isUrlStartLoading = true
        }

        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    /**
     * Init button listeners.
     * Page scroll progression
     */
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
            pageProgress.showProgress(true)
        }
        btnBrowser.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mRssFeedItem!!.link))
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

        //To show page scroll progress
        onScrollListener()


        //To hide/unhide Toolbar when scrolling page
        proWebView.addOnTouchListener { _, event ->
            var direction: UtilityMethods.Direction = UtilityMethods.Direction.NOT_DETECTED
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.x
                    startY = event.y
                }
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                    startY = 0.0f
                    startX = startY
                }
                MotionEvent.ACTION_MOVE -> if (getDistance(event, startX, startY) > touchSlop) {
                    val newX = event.x
                    val newY = event.y

                    direction = getDirection(startX, startY, newX, newY)

                    val dy = Math.abs(newY - startY)
                    val layoutParams = pageProgress.layoutParams
                    if (direction == UtilityMethods.Direction.UP) {
                        if (layoutParams.height >= 120) {
                            layoutParams.height = (layoutParams.height - dy * 0.1).toInt()
                        }
                    } else if (direction == UtilityMethods.Direction.DOWN) {
                        if (layoutParams.height <= 224f) {
                            layoutParams.height = (layoutParams.height + dy * 0.1).toInt()
                        }
                    }

                    pageProgress.layoutParams = layoutParams
                    startX = newX
                    startY = newY
                }
            }
            return@addOnTouchListener false
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun onScrollListener() {
//        proWebView.setOnScrollChangeListener { _, _, _, _, _ ->
//            if (loadProgress != null) {
//                val percentage = calculateProgression(proWebView)
//                loadProgress.progress = percentage
//            }
//        }
    }

    /**
     * Calculate page scroll progression
     */
    private fun calculateProgression(content: WebView): Int {
        val contentHeight = content.contentHeight.toFloat() * content.scaleY
        val total = contentHeight * resources.displayMetrics.density - content.height
        return (content.scrollY * 1000) / total.toInt()
    }

    /**
     * Load page
     * WebView's onResume call
     */
    override fun onResume() {
        super.onResume()
        proWebView.onResume()
        if (!isUrlStartLoading) {
            proWebView.loadUrl(mRssFeedItem!!.link)
            isUrlStartLoading = true
        }
    }

    /**
     * WebView's onPause call
     */
    override fun onPause() {
        proWebView.onPause()
        super.onPause()
    }

    /**
     * WebView's onDestroy call
     */
    override fun onDestroyView() {
        proWebView.onDestroy()
        super.onDestroyView()
    }

    /**
     * WebView's onActivityResult call
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        proWebView.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * WebView's onViewStateRestored call
     */
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        proWebView.onRestoreInstanceState(savedInstanceState)
    }

    /**
     * WebView's onRequestPermissionsResult call
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        proWebView.onRequestPermissionResult(requestCode, permissions, grantResults)
    }

    /**
     * WebView's onSaveInstanceState call
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        proWebView.onSavedInstanceState(outState)
    }
}