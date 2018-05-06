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
import workshop.akbolatss.tagsnews.screen.news.NewsSource
import workshop.akbolatss.tagsnews.screen.recommendations.RecommendationsFragment
import workshop.akbolatss.tagsnews.screen.reminders.RemindersActivity
import workshop.akbolatss.tagsnews.screen.sources.SourcesActivity
import workshop.akbolatss.tagsnews.util.Constants.SELECTED_THEME
import java.util.*
import javax.inject.Inject

class BoardActivity : BaseActivity(), BoardView, RecommendationsFragment.OnFragmentInteractionListener {

    @Inject
    lateinit var mPresenter: BoardPresenter

    private var mSectionsAdapter: SectionsPagerAdapter? = null

    private var isUpdateBoardNeeded: Boolean = false

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {
        super.onViewReady(savedInstanceState, intent)
        setSwipeBackEnable(false)
        initListeners()
        mPresenter.onLoadSources(false)
    }

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
     * @param rssSources
     */
    override fun onInitSources(rssSources: List<RssSource>) {
        val fragments = ArrayList<NewsSource>()
        fragments.add(NewsSource("+", null, true))
        for (rssSource in rssSources) {
            fragments.add(NewsSource(rssSource.title, rssSource.link, false))
        }

        mSectionsAdapter = SectionsPagerAdapter(supportFragmentManager, fragments)
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

    override fun onUpdateSources(rssSources: List<RssSource>) {
        val fragments = ArrayList<NewsSource>()
        for (rssSource in rssSources) {
            fragments.add(NewsSource(rssSource.title, rssSource.link, false))
        }
        mSectionsAdapter!!.onUpdate(fragments)
    }

    override fun onUpdateRSS() {
        mPresenter.onLoadSources(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        return true
    }

    private fun onOpenFavorites() {
        btnFam.close(true)
        val i = Intent(this, FavoritesActivity::class.java)
        startActivity(i)
    }

    private fun onOpenReminders() {
        btnFam.close(true)
        startActivity(Intent(this, RemindersActivity::class.java))
    }

    private fun onOpenSourceManager() {
        btnFam.close(true)
        val i = Intent(this, SourcesActivity::class.java)
        isUpdateBoardNeeded = true
        startActivity(i)
    }

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
