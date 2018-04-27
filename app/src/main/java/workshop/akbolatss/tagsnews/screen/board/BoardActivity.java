package workshop.akbolatss.tagsnews.screen.board;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import me.toptas.rssconverter.RssItem;
import vcm.github.webkit.proview.ProWebView;
import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.base.BaseActivity;
import workshop.akbolatss.tagsnews.di.component.DaggerBoardComponent;
import workshop.akbolatss.tagsnews.di.module.BoardModule;
import workshop.akbolatss.tagsnews.di.module.DetailsModule;
import workshop.akbolatss.tagsnews.model.dao.RssSource;
import workshop.akbolatss.tagsnews.screen.details.DetailsPresenter;
import workshop.akbolatss.tagsnews.screen.details.DetailsView;
import workshop.akbolatss.tagsnews.screen.favorites.FavoritesActivity;
import workshop.akbolatss.tagsnews.screen.news.NewsSource;
import workshop.akbolatss.tagsnews.screen.recommendations.RecommendationsFragment;
import workshop.akbolatss.tagsnews.screen.reminders.RemindersActivity;
import workshop.akbolatss.tagsnews.screen.sources.SourcesActivity;
import workshop.akbolatss.tagsnews.util.AdvancedDrawerLayout;
import workshop.akbolatss.tagsnews.util.FullDrawerLayout;

import static workshop.akbolatss.tagsnews.util.Constants.FB_PACKAGE_NAME;
import static workshop.akbolatss.tagsnews.util.Constants.ITEMS_VIEW_MODE;
import static workshop.akbolatss.tagsnews.util.Constants.SELECTED_THEME;
import static workshop.akbolatss.tagsnews.util.Constants.TW_PACKAGE_NAME;
import static workshop.akbolatss.tagsnews.util.Constants.VK_PACKAGE_NAME;
import static workshop.akbolatss.tagsnews.util.UtilityMethods.isWifiConnected;

public class BoardActivity extends BaseActivity implements BoardView, DetailsView, RecommendationsFragment.OnFragmentInteractionListener {

    private static final String TAG = "TAG";
    @Inject
    protected BoardPresenter mPresenter;

    @Inject
    protected Context mContext;

    @Inject
    protected DetailsPresenter mDetailsPresenter;

    @BindView(R.id.fullDrawer)
    protected FullDrawerLayout mDrawerLayout;

    @BindView(R.id.viewPager)
    protected ViewPager mViewPager;

    @BindView(R.id.tabs)
    protected TabLayout mTabLayout;

    @BindView(R.id.btnFam)
    protected FloatingActionMenu mFabMenu;

    private SectionsPagerAdapter mSectionsAdapter;

    private boolean isUpdateBoardNeeded;

    //Details activity's views
    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.imgView)
    protected ImageView mImageView;

    @BindView(R.id.tvTitle)
    protected TextView mTitle;

    @BindView(R.id.tvSourceName)
    protected TextView mSourceName;

    @BindView(R.id.tvTimestamp)
    protected TextView mTimestamp;

    @BindView(R.id.tvDescription)
    protected TextView mDescription;

    private boolean isFavorite;

    private RssItem mRssItem;

    @BindView(R.id.drawerBack)
    protected View drawerBackground;

    @BindView(R.id.drawerDetails)
    protected View drawerDetails;

    @BindView(R.id.drawerWebView)
    protected View drawerWeb;

    @BindView(R.id.pageProgress)
    protected ProgressBar mPageProgress;

    @BindView(R.id.webView)
    protected ProWebView mProWebView;
    private boolean isUrlStartLoading; // For preloading when Wi-Fi is connected

    private String mCurrPageUrl;
    private String mCurrPageTitle;

    @BindView(R.id.adView)
    protected AdView mAdView;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);

        DaggerBoardComponent.builder()
                .appComponent(getAppComponent())
                .boardModule(new BoardModule(this))
                .detailsModule(new DetailsModule(this))
                .build()
                .inject(this);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProWebView.setActivity(this);

        mProWebView.getSettings().setDomStorageEnabled(true);
        mProWebView.setThirdPartyCookiesEnabled(true);

        mDrawerLayout.setDrawerLockMode(AdvancedDrawerLayout.LOCK_MODE_LOCKED_CLOSED, drawerDetails);
        mDrawerLayout.addDrawerListener(new AdvancedDrawerLayout.AdvancedDrawerLayoutListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (drawerView.getId() == R.id.drawerDetails) {
                    drawerBackground.setX(slideOffset * -100);
                    if (slideOffset >= 0.998f) {
                        drawerBackground.setVisibility(View.GONE);
                    } else {
                        drawerBackground.setVisibility(View.VISIBLE);
                    }
                }
                if (drawerView.getId() == R.id.drawerWebView) {
                    drawerDetails.setX(slideOffset * -100);
                    if (slideOffset >= 0.998f) {
                        drawerDetails.setVisibility(View.GONE);
                    } else {
                        drawerDetails.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mDrawerLayout.setDrawerLockMode(AdvancedDrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (drawerView == drawerDetails) {
                    mDrawerLayout.setDrawerLockMode(AdvancedDrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    onRefreshDrawerDetails();
                } else if (drawerView == drawerWeb) {
                    isUrlStartLoading = false;
                }
                mProWebView.clearHistory();
                mProWebView.showBlank();
                mToolbar.getMenu().findItem(R.id.mAdd2Favorites).setIcon(R.drawable.ic_favorite_border_24dp);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        mPresenter.onLoadSources(false);

        mProWebView.setProClient(new ProWebView.ProClient() {
            @Override
            public void onInformationReceived(ProWebView webView, String url, String title, Bitmap favicon) {
                mCurrPageUrl = url;
                mCurrPageTitle = title;
            }

            @Override
            public void onProgressChanged(ProWebView webView, int progress) {
                Log.d(TAG, "Progress... " + progress);
            }

            @Override
            public boolean shouldOverrideUrlLoading(ProWebView webView, String url) {
                Log.d(TAG, "URL " + url);
                return true;
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onInitSources(List<RssSource> rssSources) {
        List<NewsSource> fragments = new ArrayList<>();
        fragments.add(new NewsSource("+", null, true));
        for (RssSource rssSource : rssSources) {
            fragments.add(new NewsSource(rssSource.getTitle(), rssSource.getLink(), false));
        }

        mSectionsAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setAdapter(mSectionsAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mFabMenu.hideMenu(true);
                } else {
                    mFabMenu.showMenu(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mSectionsAdapter.notifyDataSetChanged();
        mTabLayout.setupWithViewPager(mViewPager);
        try {
            mViewPager.setCurrentItem(1);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateSources(List<RssSource> rssSources) {
        List<NewsSource> fragments = new ArrayList<>();
        for (RssSource rssSource : rssSources) {
            fragments.add(new NewsSource(rssSource.getTitle(), rssSource.getLink(), false));
        }
        mSectionsAdapter.onUpdate(fragments);
    }

    @Override
    public void onUpdateRSS() {
        mPresenter.onLoadSources(true);
    }

    @Override
    public void onRefreshToolbar(boolean isFavorite) {
        this.isFavorite = isFavorite;
        if (isFavorite) {
            mToolbar.getMenu().findItem(R.id.mAdd2Favorites).setIcon(R.drawable.ic_favorite_24dp);
        } else {
            mToolbar.getMenu().findItem(R.id.mAdd2Favorites).setIcon(R.drawable.ic_favorite_border_24dp);
        }
    }

    @Override
    public void onRefreshDrawerDetails() {
        mImageView.setImageBitmap(null);
        mImageView.destroyDrawingCache();
    }

    @Override
    public void onOpenItemDetails(@NonNull final RssItem rssItem, final String sourceName) {
        mDrawerLayout.openDrawer(drawerDetails);

        mRssItem = rssItem;

        mTitle.setText(mRssItem.getTitle());
        mTimestamp.setText(mRssItem.getPublishDate());
        mDescription.setText(mRssItem.getDescription());

        mSourceName.setText(sourceName);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Picasso.with(BoardActivity.this)
                        .load(mRssItem.getImage())
                        .error(R.drawable.placeholder)
                        .placeholder(R.drawable.placeholder)
                        .into(mImageView);

                isFavorite = mDetailsPresenter.onCheckFavorites(mRssItem.getPublishDate());
                if (isWifiConnected(mContext)) {
                    mProWebView.deleteData();
                    mProWebView.loadUrl(rssItem.getLink());
                    isUrlStartLoading = true;
                }
            }
        }, 400);
    }

    @OnClick(R.id.btnOpenSource)
    @Override
    public void onOpenSource() {
        mDrawerLayout.openDrawer(drawerWeb);
        if (!isUrlStartLoading) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProWebView.loadUrl(mRssItem.getLink());
                }
            }, 300);
        }
    }

    @OnClick(R.id.btnClose)
    @Override
    public void onCloseWeb() {
        mDrawerLayout.closeDrawer(drawerWeb);
    }

    @OnClick(R.id.btnBack)
    @Override
    public void onBackWebPage() {
        mProWebView.tryGoBack();
    }

    @OnClick(R.id.btnForward)
    @Override
    public void onForwardWebPage() {
        mProWebView.tryGoForward();
    }

    @OnClick(R.id.btnReload)
    @Override
    public void onRefreshWeb() {
        mProWebView.reload();
    }

    @OnClick(R.id.btnBrowser)
    @Override
    public void onOpenInBrowser() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mCurrPageUrl));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.closeDrawer(drawerDetails);
                return true;
            case R.id.mAdd2Favorites:
                if (isFavorite) {
                    mDetailsPresenter.OnRemoveFromFavorites(mRssItem.getPublishDate());
                } else {
                    mDetailsPresenter.OnAddToFavorites(mRssItem);
                }
                return true;
            case R.id.mShare:
                onShareNews();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.btnShareCurrent)
    @Override
    public void onShareCurrPage() {
        String messageSend = mCurrPageTitle + "\n\n" + mCurrPageUrl + " \n\n---\n" + "Tag News (Beta) bit.ly/TagNewsApp";
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, messageSend);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.tvShare)));
    }

    @Override
    public void onShareNews() {
        String messageSend = mRssItem.getTitle() + "\n\n" + mRssItem.getLink() + " \n\n---\n" + "Tag News (Beta) bit.ly/TagNewsApp";
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, messageSend);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.tvShare)));
    }

    @Override
    public void onShareWithWebIntent(String socialNetwrkId) {
        String shareUrl = "market://details?id=" + socialNetwrkId;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(shareUrl));
        startActivity(intent);
    }

    @OnClick(R.id.btnVkShare)
    @Override
    public void onShareVk() {
        String messageSend = mRssItem.getTitle() + "\n\n" + mRssItem.getLink() + " \n\n---\n" + "Tag News (Beta) bit.ly/TagNewsApp";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, messageSend);
        boolean socialAppFound = false;
        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith(VK_PACKAGE_NAME)) {
                intent.setPackage(info.activityInfo.packageName);
                socialAppFound = true;
                break;
            }
        }
        if (socialAppFound) {
            startActivity(intent);
        } else {
            onShareWithWebIntent(VK_PACKAGE_NAME);
        }
    }

    @OnClick(R.id.btnFbShare)
    @Override
    public void onShareFb() {
        String messageSend = mRssItem.getTitle() + "\n\n" + mRssItem.getLink() + " \n\n---\n" + "Tag News (Beta) bit.ly/TagNewsApp";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, messageSend);
        boolean socialAppFound = false;
        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith(FB_PACKAGE_NAME)) {
                intent.setPackage(info.activityInfo.packageName);
                socialAppFound = true;
                break;
            }
        }
        if (socialAppFound) {
            startActivity(intent);
        } else {
            onShareWithWebIntent(FB_PACKAGE_NAME);
        }
    }

    @OnClick(R.id.btnTwShare)
    @Override
    public void onShareTw() {
        String messageSend = mRssItem.getTitle() + "\n\n" + mRssItem.getLink() + " \n\n---\n" + "Tag News (Beta) bit.ly/TagNewsApp";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, messageSend);
        boolean socialAppFound = false;
        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith(TW_PACKAGE_NAME)) {
                intent.setPackage(info.activityInfo.packageName);
                socialAppFound = true;
                break;
            }
        }
        if (socialAppFound) {
            startActivity(intent);
        } else {
            onShareWithWebIntent(TW_PACKAGE_NAME);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(drawerWeb)) {
            mDrawerLayout.closeDrawer(drawerWeb);
        } else if (mDrawerLayout.isDrawerOpen(drawerDetails)) {
            mDrawerLayout.closeDrawer(drawerDetails);
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.btnFab1)
    public void onOpenFavorites() {
        mFabMenu.close(true);
        Intent i = new Intent(this, FavoritesActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btnFab4)
    public void onOpenReminders() {
        mFabMenu.close(true);
        startActivity(new Intent(this, RemindersActivity.class));
    }

    @OnClick(R.id.btnFab2)
    public void onOpenSourceManager() {
        mFabMenu.close(true);
        Intent i = new Intent(this, SourcesActivity.class);
        isUpdateBoardNeeded = true;
        startActivity(i);
    }

    @OnClick(R.id.btnFab3)
    public void onSwitchRVLayout() {
        LayoutInflater layoutInflater = LayoutInflater.from(BoardActivity.this);
        final View subView = layoutInflater.inflate(R.layout.dialog_theme, null);

        final RadioGroup rGroupTheme = subView.findViewById(R.id.rGroupTheme);
        final RadioGroup rGroupViewItems = subView.findViewById(R.id.rGroupViewItems);

        final AppCompatRadioButton rbDayTheme = rGroupTheme.findViewById(R.id.rButtonDay);
        final AppCompatRadioButton rbNightTheme = rGroupTheme.findViewById(R.id.rButtonNight);
        final AppCompatRadioButton rbTextOnly = rGroupViewItems.findViewById(R.id.rButtonTextOnly);
        final AppCompatRadioButton rbSmall = rGroupViewItems.findViewById(R.id.rButtonSmallBlock);
        final AppCompatRadioButton rbLarge = rGroupViewItems.findViewById(R.id.rButtonLargeBlock);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Dialog);

        if (Hawk.contains(SELECTED_THEME)) { // False = Day, True = Night
            if (Hawk.get(SELECTED_THEME)) {
                rGroupTheme.check(rbNightTheme.getId());
            } else {
                rGroupTheme.check(rbDayTheme.getId());
            }
        } else {
            Hawk.put(SELECTED_THEME, false);
            rGroupTheme.check(rbDayTheme.getId());
        }


        if (Hawk.contains(ITEMS_VIEW_MODE)) {
            if (Hawk.get(ITEMS_VIEW_MODE).equals(0)) {
                rGroupViewItems.check(rbTextOnly.getId());
            } else if (Hawk.get(ITEMS_VIEW_MODE).equals(1)) {
                rGroupViewItems.check(rbSmall.getId());
            } else if (Hawk.get(ITEMS_VIEW_MODE).equals(2)) {
                rGroupViewItems.check(rbLarge.getId());
            }
        } else {
            Hawk.put(ITEMS_VIEW_MODE, 0);
            rGroupViewItems.check(rbTextOnly.getId());
        }
        builder.setView(subView);
        builder.setPositiveButton(R.string.tvSave, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (rbTextOnly.isChecked()) {
                    Hawk.put(ITEMS_VIEW_MODE, 0);
                } else if (rbSmall.isChecked()) {
                    Hawk.put(ITEMS_VIEW_MODE, 1);
                } else if (rbLarge.isChecked()) {
                    Hawk.put(ITEMS_VIEW_MODE, 2);
                }

                if (rbDayTheme.isChecked()) {
                    Hawk.put(SELECTED_THEME, false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else if (rbNightTheme.isChecked()) {
                    Hawk.put(SELECTED_THEME, true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                recreate();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(R.string.tvCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
        mFabMenu.close(true);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (isUpdateBoardNeeded) {
            isUpdateBoardNeeded = false;
            mPresenter.onLoadSources(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mProWebView.onPause();
    }

    @Override
    protected void onDestroy() {
        mProWebView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mProWebView.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mProWebView.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mProWebView.onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mProWebView.onSavedInstanceState(outState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }
}
