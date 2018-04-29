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


/**
 * Author: Akbolat Sadvakassov
 * Date: 27.04.2018
 */

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

    public fun onOpenSource() {
        viewPager.setCurrentItem(1, true)
    }

    public fun onOpenDetails() {
        viewPager.setCurrentItem(0, true)
    }

    fun toggleHideBar(immersiveOff: Boolean) {

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

//        // Navigation bar hiding:  Backwards compatible to ICS.
//        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//        // Status bar hiding: Backwards compatible to Jellybean
//        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_FULLSCREEN
//
//        // Immersive mode: Backward compatible to KitKat.
//        // Note that this flag doesn't do anything by itself, it only augments the behavior
//        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
//        // all three flags are being toggled together.
//        // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
//        // Sticky immersive mode differs in that it makes the navigation and status bars
//        // semi-transparent, and the UI flag does not get cleared when the user interacts with
//        // the screen.
//        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//        window.decorView.systemUiVisibility = newUiOptions
    }

    override fun getContentView(): Int {
        return R.layout.activity_details
    }
}