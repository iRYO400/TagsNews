package workshop.akbolatss.tagsnews.screen.details

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import kotlinx.android.synthetic.main.activity_details.*
import me.toptas.rssconverter.RssItem
import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.base.BaseActivity
import workshop.akbolatss.tagsnews.screen.browser.BrowserFragment
import android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
import android.os.Build
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

class DetailsActivity : BaseActivity() {

    private var mSectionsAdapter: SectionsPagerAdapter? = null

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {
        super.onViewReady(savedInstanceState, intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)

        onInitSources()
    }

    /**
     * Init TabLayout Fragments
     */
    private fun onInitSources() {
        mSectionsAdapter = SectionsPagerAdapter(supportFragmentManager, intent.getSerializableExtra("RssItem") as RssItem)
        viewPager.adapter = mSectionsAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    (mSectionsAdapter?.getRegisteredFragment(1) as BrowserFragment).onPause()
                    toggleHideBar(true)
                } else {
                    (mSectionsAdapter?.getRegisteredFragment(1) as BrowserFragment).onResume()
                    toggleHideBar(false)
                }
            }
        })
    }

    fun onOpenSource() {
        viewPager.setCurrentItem(1, true)
    }

    fun onOpenDetails() {
        viewPager.setCurrentItem(0, true)
    }

    private fun toggleHideBar(immersiveOff: Boolean) {

        // BEGIN_INCLUDE (get_current_ui_flags)
        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.
        val uiOptions = window.decorView.systemUiVisibility
        var newUiOptions = uiOptions

        if (immersiveOff) {
            Log.i("TAG", "Turning immersive mode mode off. ")
            newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_FULLSCREEN
            newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        } else {
            Log.i("TAG", "Turning immersive mode mode on.")
            newUiOptions = newUiOptions or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            newUiOptions = newUiOptions or View.SYSTEM_UI_FLAG_FULLSCREEN
            newUiOptions = newUiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
        window.decorView.systemUiVisibility = newUiOptions
    }

    override fun getContentView(): Int {
        return R.layout.activity_details
    }
}