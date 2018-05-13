package workshop.akbolatss.tagsnews.screen.board

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatDelegate
import android.view.Menu
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_board.*
import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.base.BaseActivity
import workshop.akbolatss.tagsnews.di.component.DaggerBoardComponent
import workshop.akbolatss.tagsnews.di.module.BoardModule
import workshop.akbolatss.tagsnews.model.dao.RssSource
import workshop.akbolatss.tagsnews.screen.favorites.FavoritesActivity
import workshop.akbolatss.tagsnews.screen.recommendations.RecommendationsFragment
import workshop.akbolatss.tagsnews.screen.reminders.RemindersActivity
import workshop.akbolatss.tagsnews.screen.sources.SourcesActivity
import workshop.akbolatss.tagsnews.util.Constants.SELECTED_THEME
import javax.inject.Inject

/**
 * Main Activity, that handles tabs with active RSS sources
 */
class BoardActivity : BaseActivity(), BoardView, RecommendationsFragment.OnFragmentInteractionListener {

    /**
     * MVP Presenter, where stored logic related to API calls, DB
     */
    @Inject
    lateinit var mPresenter: BoardPresenter

    /**
     * FragmentStatePagerAdapter for ViewPager
     */
    private var mSectionsAdapter: SectionsPagerAdapter? = null

    /**
     * Checker to update RSS Source when they changed
     */
    private var isUpdateBoardNeeded: Boolean = false

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {
        super.onViewReady(savedInstanceState, intent)
        setSwipeBackEnable(false)
        initListeners()
        mPresenter.onLoadSources(false)
    }

    /**
     * Init listeners for Floating Action Buttons
     */
    private fun initListeners() {
        fabNotifies.setOnClickListener {
            onOpenReminders()
        }
        fabViews.setOnClickListener {
            turnNightMode()
        }
        fabBooks.setOnClickListener {
            onOpenFavorites()
        }
        fabChannels.setOnClickListener {
            onOpenSourceManager()
        }
    }

    /**
     * Init TabLayout Fragments
     * @param rssSources List of RSS sources from DB
     */
    override fun onInitSources(rssSources: List<RssSource>) {
        mSectionsAdapter = SectionsPagerAdapter(supportFragmentManager, rssSources as MutableList<RssSource>)
        viewPager.offscreenPageLimit = 5
        viewPager.adapter = mSectionsAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    btnFam.hideMenu(true)
                } else {
                    btnFam.showMenu(true)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        mSectionsAdapter!!.notifyDataSetChanged()
        tabs.setupWithViewPager(viewPager)
        try {
            viewPager.currentItem = 1
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

    }

    /**
     * Update RSS sources after they changed in #SourceActivity
     * @see SourcesActivity
     */
    override fun onUpdateSources(rssSources: List<RssSource>) {
        mSectionsAdapter!!.onUpdate(rssSources)
    }

    /**
     * Directly update new added RSS source
     */
    override fun onUpdateRSS() {
        mPresenter.onLoadSources(true)
    }

    /**
     * Open #FavoritesActivity
     * @see FavoritesActivity
     */
    private fun onOpenFavorites() {
        btnFam.close(true)
        startActivity(Intent(this, FavoritesActivity::class.java))
    }

    /**
     * Open #RemindersActivity
     * @see RemindersActivity
     */
    private fun onOpenReminders() {
        btnFam.close(true)
        startActivity(Intent(this, RemindersActivity::class.java))
    }

    /**
     * Open #SourcesActivity
     * @see SourcesActivity
     */
    private fun onOpenSourceManager() {
        btnFam.close(true)
        val i = Intent(this, SourcesActivity::class.java)
        isUpdateBoardNeeded = true
        startActivity(i)
    }

    /**
     * Switch between Day and Night themes
     */
    private fun turnNightMode() {
        if (Hawk.contains(SELECTED_THEME)) { // False = Day, True = Night
            if (Hawk.get(SELECTED_THEME)) {
                Hawk.put(SELECTED_THEME, false)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                Hawk.put(SELECTED_THEME, true)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        } else {
            Hawk.put(SELECTED_THEME, true)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        recreate()
        btnFam.close(true)
    }

    override fun onPostResume() {
        super.onPostResume()
        if (isUpdateBoardNeeded) {
            isUpdateBoardNeeded = false
            mPresenter.onLoadSources(false)
        }
    }

    override fun onInitDagger() {
        DaggerBoardComponent.builder()
                .appComponent(appComponent)
                .boardModule(BoardModule(this))
                .build()
                .inject(this)
    }

    override fun getContentView(): Int {
        return R.layout.activity_board
    }
}
