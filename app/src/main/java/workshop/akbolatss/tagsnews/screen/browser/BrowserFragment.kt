package workshop.akbolatss.tagsnews.screen.browser

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.webkit.WebView
import kotlinx.android.synthetic.main.fragment_browser.*
import me.toptas.rssconverter.RssItem
import vcm.github.webkit.proview.ProWebView
import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.screen.details.DetailsActivity
import workshop.akbolatss.tagsnews.util.UtilityMethods.isWifiConnected

class BrowserFragment : Fragment(), BrowserView {

    private var mRssItem: RssItem? = null
    private var mCurrPageTitle = ""
    private var mCurrPageUrl = ""
    private var touchSlop: Int = 0
    private var startX: Float = 0f
    private var startY: Float = 0f
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

        private fun getDirection(x1: Float, y1: Float, x2: Float, y2: Float): Direction {
            val angle = getAngle(x1, y1, x2, y2)
            return Direction[angle]
        }

        private fun getAngle(x1: Float, y1: Float, x2: Float, y2: Float): Double {
            val rad = Math.atan2((y1 - y2).toDouble(), (x2 - x1).toDouble()) + Math.PI
            return (rad * 180 / Math.PI + 180) % 360
        }

        private fun getDistance(ev: MotionEvent, startX: Float, startY: Float): Float {
            var distanceSum = 0f

            val dx = ev.getX(0) - startX
            val dy = ev.getY(0) - startY
            distanceSum += Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()

            return distanceSum
        }

        enum class Direction {
            NOT_DETECTED,
            UP,
            DOWN,
            LEFT,
            RIGHT;

            companion object {

                operator fun get(angle: Double): Direction {
                    return if (inRange(angle, 45f, 135f)) {
                        Direction.UP
                    } else if (inRange(angle, 0f, 45f) || inRange(angle, 315f, 360f)) {
                        Direction.RIGHT
                    } else if (inRange(angle, 225f, 315f)) {
                        Direction.DOWN
                    } else {
                        Direction.LEFT
                    }
                }

                private fun inRange(angle: Double, init: Float, end: Float): Boolean {
                    return angle >= init && angle < end
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_browser, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRssItem = arguments!!.getSerializable(PARAM_TYPE) as RssItem

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

        if (isWifiConnected(context!!)) {
            proWebView.loadUrl(mRssItem!!.link)
            isUrlStartLoading = true
        }

        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
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
            pageProgress.showProgress(true)
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


        proWebView.setOnScrollChangeListener { _, _, _, _, _ ->
            if (loadProgress != null) {
                val percentage = calculateProgression(proWebView)
                loadProgress.progress = percentage
            }
        }

        proWebView.addOnTouchListener { v, event ->
            var direction: Direction = Direction.NOT_DETECTED
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
                    if (direction == Direction.UP) {
                        if (layoutParams.height >= 120) {
                            layoutParams.height = (layoutParams.height - dy * 0.1).toInt()
                        }
                    } else if (direction == Direction.DOWN) {
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

    private fun calculateProgression(content: WebView): Int {
        val contentHeight = content.contentHeight.toFloat() * content.scaleY
        val total = contentHeight * resources.displayMetrics.density - content.height
        return (content.scrollY * 1000) / total.toInt()
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