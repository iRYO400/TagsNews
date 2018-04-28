package workshop.akbolatss.tagsnews.screen.board

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.AppCompatRadioButton
import android.view.LayoutInflater
import android.view.Menu
import android.widget.RadioGroup
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_board.*
import kotlinx.android.synthetic.main.activity_main.*
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
import workshop.akbolatss.tagsnews.util.Constants.ITEMS_VIEW_MODE
import workshop.akbolatss.tagsnews.util.Constants.SELECTED_THEME
import java.util.*
import javax.inject.Inject

class BoardActivity : BaseActivity(), BoardView, RecommendationsFragment.OnFragmentInteractionListener {

    @Inject
    lateinit var mPresenter: BoardPresenter

    private var mSectionsAdapter: SectionsPagerAdapter? = null

    private var isUpdateBoardNeeded: Boolean = false

    //    //Details activity's views
    //    @BindView(R.id.toolbar)
    //    protected Toolbar mToolbar;
    //
    //    @BindView(R.id.imgView)
    //    protected ImageView mImageView;
    //
    //    @BindView(R.id.tvTitle)
    //    protected TextView mTitle;
    //
    //    @BindView(R.id.tvSourceName)
    //    protected TextView mSourceName;
    //
    //    @BindView(R.id.tvTimestamp)
    //    protected TextView mTimestamp;
    //
    //    @BindView(R.id.tvDescription)
    //    protected TextView mDescription;
    //    @BindView(R.id.drawerDetails)
    //    protected View drawerDetails;
    //
    //    @BindView(R.id.drawerWebView)
    //    protected View drawerWeb;

    //    @BindView(R.id.webView)
    //    protected ProWebView mProWebView;

    //    private boolean isUrlStartLoading; // For preloading when Wi-Fi is connected

    //    private String mCurrPageUrl;
    //    private String mCurrPageTitle;

    //    @BindView(R.id.adView)
    //    protected AdView mAdView;

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {
        super.onViewReady(savedInstanceState, intent)


        DaggerBoardComponent.builder()
                .appComponent(appComponent)
                .boardModule(BoardModule(this))
                .build()
                .inject(this)

        mPresenter.onLoadSources(false)


        //        setSupportActionBar(mToolbar);
        //        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //        mProWebView.setActivity(this);

        //        mProWebView.getSettings().setDomStorageEnabled(true);
        //        mProWebView.setThirdPartyCookiesEnabled(true);

//        fullDrawer.addDrawerListener(object : DrawerLayout.DrawerListener {
//            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
////                if (drawerView.id == R.id.drawerDetails) {
////                    drawerBackground!!.x = slideOffset * -100
////                    if (slideOffset >= 0.998f) {
////                        drawerBackground!!.visibility = View.GONE
////                    } else {
////                        drawerBackground!!.visibility = View.VISIBLE
////                    }
////                }
//                if (drawerView.id == R.id.drawerWebView) {
//                    //                    drawerDetails.setX(slideOffset * -100);
//                    //                    if (slideOffset >= 0.998f) {
//                    //                        drawerDetails.setVisibility(View.GONE);
//                    //                    } else {
//                    //                        drawerDetails.setVisibility(View.VISIBLE);
//                    //                    }
//                }
//            }
//
//            override fun onDrawerOpened(drawerView: View) {
//                fullDrawer.setDrawerLockMode(FullDrawerLayout.LOCK_MODE_UNLOCKED)
//            }
//
//            override fun onDrawerClosed(drawerView: View) {
//                //                if (drawerView == drawerDetails) {
//                //                    mDrawerLayout.setDrawerLockMode(AdvancedDrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//                //                    onRefreshDrawerDetails();
//                //                } else if (drawerView == drawerWeb) {
//                //                    isUrlStartLoading = false;
//                //                }
//                //                mProWebView.clearHistory();
//                //                mProWebView.showBlank();
//                //                mToolbar.getMenu().findItem(R.id.mAdd2Favorites).setIcon(R.drawable.ic_favorite_border_24dp);
//            }
//
//            override fun onDrawerStateChanged(newState: Int) {}
//        })

//        mPresenter!!.onLoadSources(false)

        //        mProWebView.setProClient(new ProWebView.ProClient() {
        //            @Override
        //            public void onInformationReceived(ProWebView webView, String url, String title, Bitmap favicon) {
        //                mCurrPageUrl = url;
        //                mCurrPageTitle = title;
        //            }
        //
        //            @Override
        //            public void onProgressChanged(ProWebView webView, int progress) {
        //                Log.d(TAG, "Progress... " + progress);
        //            }
        //
        //            @Override
        //            public boolean shouldOverrideUrlLoading(ProWebView webView, String url) {
        //                Log.d(TAG, "URL " + url);
        //                return true;
        //            }
        //        });
        //
        //        AdRequest adRequest = new AdRequest.Builder().build();
        //        mAdView.loadAd(adRequest);
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
//        mPresenter!!.onLoadSources(true)
    }
    //
    //    @Override
    //    public void onRefreshToolbar(boolean isFavorite) {
    //        this.isFavorite = isFavorite;
    //        if (isFavorite) {
    //            mToolbar.getMenu().findItem(R.id.mAdd2Favorites).setIcon(R.drawable.ic_favorite_24dp);
    //        } else {
    //            mToolbar.getMenu().findItem(R.id.mAdd2Favorites).setIcon(R.drawable.ic_favorite_border_24dp);
    //        }
    //    }
    //
    //    @Override
    //    public void onRefreshDrawerDetails() {
    //        mImageView.setImageBitmap(null);
    //        mImageView.destroyDrawingCache();
    //    }

    //    @Override
    //    public void onOpenItemDetails(@NonNull final RssItem rssItem, final String sourceName) {
    //
    //        mDrawerLayout.openDrawer(drawerDetails);
    //
    //        mRssItem = rssItem;
    //
    //        mTitle.setText(mRssItem.getTitle());
    //        mTimestamp.setText(mRssItem.getPublishDate());
    //        mDescription.setText(mRssItem.getDescription());
    //
    //        mSourceName.setText(sourceName);
    //
    //        new Handler().postDelayed(new Runnable() {
    //            @Override
    //            public void run() {
    //                Picasso.with(BoardActivity.this)
    //                        .load(mRssItem.getImage())
    //                        .error(R.drawable.placeholder)
    //                        .placeholder(R.drawable.placeholder)
    //                        .into(mImageView);
    //
    //                isFavorite = mDetailsPresenter.onCheckFavorites(mRssItem.getPublishDate());
    //                if (isWifiConnected(mContext)) {
    //                    mProWebView.deleteData();
    //                    mProWebView.loadUrl(rssItem.getLink());
    //                    isUrlStartLoading = true;
    //                }
    //            }
    //        }, 400);
    //    }
    //    @OnClick(R.id.btnOpenSource)
    //    @Override
    //    public void onOpenSource() {
    //        mDrawerLayout.openDrawer(drawerWeb);
    //        if (!isUrlStartLoading) {
    //            new Handler().postDelayed(new Runnable() {
    //                @Override
    //                public void run() {
    //                    mProWebView.loadUrl(mRssItem.getLink());
    //                }
    //            }, 300);
    //        }
    //    }
    //
    //    @OnClick(R.id.btnClose)
    //    @Override
    //    public void onCloseWeb() {
    //        mDrawerLayout.closeDrawer(drawerWeb);
    //    }
    //
    //    @OnClick(R.id.btnBack)
    //    @Override
    //    public void onBackWebPage() {
    //        mProWebView.tryGoBack();
    //    }
    //
    //    @OnClick(R.id.btnForward)
    //    @Override
    //    public void onForwardWebPage() {
    //        mProWebView.tryGoForward();
    //    }
    //
    //    @OnClick(R.id.btnReload)
    //    @Override
    //    public void onRefreshWeb() {
    //        mProWebView.reload();
    //    }
    //
    //    @OnClick(R.id.btnBrowser)
    //    @Override
    //    public void onOpenInBrowser() {
    //        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mCurrPageUrl));
    //        startActivity(intent);
    //    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
        return true
    }
    //    @OnClick(R.id.btnShareCurrent)
    //    @Override
    //    public void onShareCurrPage() {
    //        String messageSend = mCurrPageTitle + "\n\n" + mCurrPageUrl + " \n\n---\n" + "Tag News (Beta) bit.ly/TagNewsApp";
    //        Intent shareIntent = new Intent();
    //        shareIntent.setAction(Intent.ACTION_SEND);
    //        shareIntent.putExtra(Intent.EXTRA_TEXT, messageSend);
    //        shareIntent.setType("text/plain");
    //        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.tvShare)));
    //    }

    //    @Override
    //    public void onShareNews() {
    //        String messageSend = mRssItem.getTitle() + "\n\n" + mRssItem.getLink() + " \n\n---\n" + "Tag News (Beta) bit.ly/TagNewsApp";
    //        Intent shareIntent = new Intent();
    //        shareIntent.setAction(Intent.ACTION_SEND);
    //        shareIntent.putExtra(Intent.EXTRA_TEXT, messageSend);
    //        shareIntent.setType("text/plain");
    //        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.tvShare)));
    //    }
    //
    //    @Override
    //    public void onShareWithWebIntent(String socialNetwrkId) {
    //        String shareUrl = "market://details?id=" + socialNetwrkId;
    //        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(shareUrl));
    //        startActivity(intent);
    //    }

    //    @OnClick(R.id.btnVkShare)
    //    @Override
    //    public void onShareVk() {
    //        String messageSend = mRssItem.getTitle() + "\n\n" + mRssItem.getLink() + " \n\n---\n" + "Tag News (Beta) bit.ly/TagNewsApp";
    //        Intent intent = new Intent(Intent.ACTION_SEND);
    //        intent.setType("text/plain");
    //        intent.putExtra(Intent.EXTRA_TEXT, messageSend);
    //        boolean socialAppFound = false;
    //        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
    //        for (ResolveInfo info : matches) {
    //            if (info.activityInfo.packageName.toLowerCase().startsWith(VK_PACKAGE_NAME)) {
    //                intent.setPackage(info.activityInfo.packageName);
    //                socialAppFound = true;
    //                break;
    //            }
    //        }
    //        if (socialAppFound) {
    //            startActivity(intent);
    //        } else {
    //            onShareWithWebIntent(VK_PACKAGE_NAME);
    //        }
    //    }
    //
    //    @OnClick(R.id.btnFbShare)
    //    @Override
    //    public void onShareFb() {
    //        String messageSend = mRssItem.getTitle() + "\n\n" + mRssItem.getLink() + " \n\n---\n" + "Tag News (Beta) bit.ly/TagNewsApp";
    //        Intent intent = new Intent(Intent.ACTION_SEND);
    //        intent.setType("text/plain");
    //        intent.putExtra(Intent.EXTRA_TEXT, messageSend);
    //        boolean socialAppFound = false;
    //        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
    //        for (ResolveInfo info : matches) {
    //            if (info.activityInfo.packageName.toLowerCase().startsWith(FB_PACKAGE_NAME)) {
    //                intent.setPackage(info.activityInfo.packageName);
    //                socialAppFound = true;
    //                break;
    //            }
    //        }
    //        if (socialAppFound) {
    //            startActivity(intent);
    //        } else {
    //            onShareWithWebIntent(FB_PACKAGE_NAME);
    //        }
    //    }
    //
    //    @OnClick(R.id.btnTwShare)
    //    @Override
    //    public void onShareTw() {
    //        String messageSend = mRssItem.getTitle() + "\n\n" + mRssItem.getLink() + " \n\n---\n" + "Tag News (Beta) bit.ly/TagNewsApp";
    //        Intent intent = new Intent(Intent.ACTION_SEND);
    //        intent.setType("text/plain");
    //        intent.putExtra(Intent.EXTRA_TEXT, messageSend);
    //        boolean socialAppFound = false;
    //        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
    //        for (ResolveInfo info : matches) {
    //            if (info.activityInfo.packageName.toLowerCase().startsWith(TW_PACKAGE_NAME)) {
    //                intent.setPackage(info.activityInfo.packageName);
    //                socialAppFound = true;
    //                break;
    //            }
    //        }
    //        if (socialAppFound) {
    //            startActivity(intent);
    //        } else {
    //            onShareWithWebIntent(TW_PACKAGE_NAME);
    //        }
    //    }

    fun onOpenFavorites() {
        btnFam.close(true)
        val i = Intent(this, FavoritesActivity::class.java)
        startActivity(i)
    }

    fun onOpenReminders() {
        btnFam.close(true)
        startActivity(Intent(this, RemindersActivity::class.java))
    }

    fun onOpenSourceManager() {
        btnFam.close(true)
        val i = Intent(this, SourcesActivity::class.java)
        isUpdateBoardNeeded = true
        startActivity(i)
    }

    fun onSwitchRVLayout() {
        val layoutInflater = LayoutInflater.from(this@BoardActivity)
        val subView = layoutInflater.inflate(R.layout.dialog_theme, null)

        val rGroupTheme = subView.findViewById<RadioGroup>(R.id.rGroupTheme)
        val rGroupViewItems = subView.findViewById<RadioGroup>(R.id.rGroupViewItems)

        val rbDayTheme = rGroupTheme.findViewById<AppCompatRadioButton>(R.id.rButtonDay)
        val rbNightTheme = rGroupTheme.findViewById<AppCompatRadioButton>(R.id.rButtonNight)
        val rbTextOnly = rGroupViewItems.findViewById<AppCompatRadioButton>(R.id.rButtonTextOnly)
        val rbSmall = rGroupViewItems.findViewById<AppCompatRadioButton>(R.id.rButtonSmallBlock)
        val rbLarge = rGroupViewItems.findViewById<AppCompatRadioButton>(R.id.rButtonLargeBlock)
        val builder = AlertDialog.Builder(this, R.style.Dialog)

        if (Hawk.contains(SELECTED_THEME)) { // False = Day, True = Night
            if (Hawk.get(SELECTED_THEME)) {
                rGroupTheme.check(rbNightTheme.id)
            } else {
                rGroupTheme.check(rbDayTheme.id)
            }
        } else {
            Hawk.put(SELECTED_THEME, false)
            rGroupTheme.check(rbDayTheme.id)
        }


        if (Hawk.contains(ITEMS_VIEW_MODE)) {
            if (Hawk.get<Any>(ITEMS_VIEW_MODE) == 0) {
                rGroupViewItems.check(rbTextOnly.id)
            } else if (Hawk.get<Any>(ITEMS_VIEW_MODE) == 1) {
                rGroupViewItems.check(rbSmall.id)
            } else if (Hawk.get<Any>(ITEMS_VIEW_MODE) == 2) {
                rGroupViewItems.check(rbLarge.id)
            }
        } else {
            Hawk.put(ITEMS_VIEW_MODE, 0)
            rGroupViewItems.check(rbTextOnly.id)
        }
        builder.setView(subView)
        builder.setPositiveButton(R.string.tvSave) { dialogInterface, _ ->
            if (rbTextOnly.isChecked) {
                Hawk.put(ITEMS_VIEW_MODE, 0)
            } else if (rbSmall.isChecked) {
                Hawk.put(ITEMS_VIEW_MODE, 1)
            } else if (rbLarge.isChecked) {
                Hawk.put(ITEMS_VIEW_MODE, 2)
            }

            if (rbDayTheme.isChecked) {
                Hawk.put(SELECTED_THEME, false)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else if (rbNightTheme.isChecked) {
                Hawk.put(SELECTED_THEME, true)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            recreate()
            dialogInterface.dismiss()
        }
        builder.setNegativeButton(R.string.tvCancel) { dialogInterface, _ -> dialogInterface.cancel() }
        builder.show()
        btnFam.close(true)
    }

    override fun onPostResume() {
        super.onPostResume()
        if (isUpdateBoardNeeded) {
            isUpdateBoardNeeded = false
//            mPresenter!!.onLoadSources(false)
        }
    }

    //    @Override
    //    protected void onResume() {
    //        super.onResume();
    //        mProWebView.onResume();
    //    }
    //
    //    @Override
    //    protected void onPause() {
    //        super.onPause();
    //        mProWebView.onPause();
    //    }
    //
    //    @Override
    //    protected void onDestroy() {
    //        mProWebView.onDestroy();
    //        super.onDestroy();
    //    }
    //
    //    @Override
    //    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //        super.onActivityResult(requestCode, resultCode, data);
    //        mProWebView.onActivityResult(requestCode, resultCode, data);
    //    }
    //
    //    @Override
    //    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    //        super.onRestoreInstanceState(savedInstanceState);
    //        mProWebView.onRestoreInstanceState(savedInstanceState);
    //    }
    //
    //    @Override
    //    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    //        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    //        mProWebView.onRequestPermissionResult(requestCode, permissions, grantResults);
    //    }
    //
    //    @Override
    //    protected void onSaveInstanceState(Bundle outState) {
    //        super.onSaveInstanceState(outState);
    //        mProWebView.onSavedInstanceState(outState);
    //    }

    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    companion object {

        private val TAG = "TAG"
    }
}
