package workshop.akbolatss.tagsnews.screen.board;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import me.toptas.rssconverter.RssItem;
import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.application.App;
import workshop.akbolatss.tagsnews.base.BaseActivity;
import workshop.akbolatss.tagsnews.di.component.DaggerBoardComponent;
import workshop.akbolatss.tagsnews.di.module.BoardModule;
import workshop.akbolatss.tagsnews.repositories.source.RssSource;
import workshop.akbolatss.tagsnews.screen.favorites.FavoritesActivity;
import workshop.akbolatss.tagsnews.screen.news.NewsSource;
import workshop.akbolatss.tagsnews.screen.sources.SourcesActivity;
import workshop.akbolatss.tagsnews.util.FullDrawerLayout;
import workshop.akbolatss.tagsnews.util.customTabs.CustomTabActivityHelper;

import static workshop.akbolatss.tagsnews.util.Constants.FB_PACHAGE_NAME;
import static workshop.akbolatss.tagsnews.util.Constants.ITEMS_VIEW_MODE;
import static workshop.akbolatss.tagsnews.util.Constants.SELECTED_THEME;
import static workshop.akbolatss.tagsnews.util.Constants.TW_PACHAGE_NAME;
import static workshop.akbolatss.tagsnews.util.Constants.VK_PACHAGE_NAME;

public class BoardActivity extends BaseActivity implements BoardView {

    private static final String TAG = "TAG";
    @Inject
    BoardPresenter mPresenter;

    @BindView(R.id.fullDrawer)
    protected FullDrawerLayout mFullDrawerLayout;

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

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);

        DaggerBoardComponent.builder()
                .appComponent(App.getAppComponent())
                .boardModule(new BoardModule(this))
                .build()
                .inject(this);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFullDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mFullDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mFullDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mFullDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        mPresenter.onLoadSources();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (isUpdateBoardNeeded) {
            isUpdateBoardNeeded = false;
            mPresenter.onLoadSources();
            Log.d("TAG", "onPostResume, The BoardActivity is Updated");
        }
    }

    @Override
    public void onInitSources(List<RssSource> rssSources) {
        List<NewsSource> fragments = new ArrayList<>();
        for (RssSource rssSource : rssSources) {
            fragments.add(new NewsSource(rssSource.getTitle(), rssSource.getLink()));
        }

        mSectionsAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mSectionsAdapter);
        mSectionsAdapter.notifyDataSetChanged();
        mTabLayout.setupWithViewPager(mViewPager);
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

    @OnClick(R.id.btnOpenSource)
    @Override
    public void onOpenSource() {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
        builder.setToolbarColor(getResources().getColor(R.color.colorAccent));


        CustomTabsIntent customTabsIntent = builder.build();
        CustomTabActivityHelper.openCustomTab(this, customTabsIntent, Uri.parse(mRssItem.getLink()),
                new CustomTabActivityHelper.CustomTabFallback() {
                    @Override
                    public void openUri(Activity activity, Uri uri) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        activity.startActivity(intent);
                    }
                });
    }

    @OnClick(R.id.btnVkShare)
    protected void shareVk() {
        String messageSend = mRssItem.getTitle() + "\n\n" + mRssItem.getLink() + " \n\n---\n" + "Tag News (Beta) bit.ly/TagNewsApp";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, messageSend);
        boolean socialAppFound = false;
        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith(VK_PACHAGE_NAME)) {
                intent.setPackage(info.activityInfo.packageName);
                socialAppFound = true;
                break;
            }
        }
        if (socialAppFound) {
            startActivity(intent);
        } else {  // As fallback, realize sharing through browser
            shareWithWebIntent(VK_PACHAGE_NAME);
        }
    }

    @OnClick(R.id.btnFbShare)
    protected void shareFb() {
        String messageSend = mRssItem.getTitle() + "\n\n" + mRssItem.getLink() + " \n\n---\n" + "Tag News (Beta) bit.ly/TagNewsApp";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, messageSend);
        boolean socialAppFound = false;
        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith(FB_PACHAGE_NAME)) {
                intent.setPackage(info.activityInfo.packageName);
                socialAppFound = true;
                break;
            }
        }
        if (socialAppFound) {
            startActivity(intent);
        } else {  // As fallback, realize sharing through browser
            shareWithWebIntent(FB_PACHAGE_NAME);
            Log.d("TAG", "Share with WebView intent");
        }
    }

    @OnClick(R.id.btnTwShare)
    protected void shareTw() {
        String messageSend = mRssItem.getTitle() + "\n\n" + mRssItem.getLink() + " \n\n---\n" + "Tag News (Beta) bit.ly/TagNewsApp";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, messageSend);
        boolean socialAppFound = false;
        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith(TW_PACHAGE_NAME)) {
                intent.setPackage(info.activityInfo.packageName);
                socialAppFound = true;
                break;
            }
        }
        if (socialAppFound) {
            startActivity(intent);
        } else {  // As fallback, realize sharing through browser
            shareWithWebIntent(TW_PACHAGE_NAME);
        }
    }

    protected void shareWithWebIntent(String socialNetwrkId) {
        String shareUrl = "market://details?id=" + socialNetwrkId;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(shareUrl));
        startActivity(intent);
    }

    @Override
    public void onRefreshDrawerDetails() {
        mImageView.setImageBitmap(null);
        mImageView.destroyDrawingCache();
    }

    @Override
    public void onOpenItemDetails(@NonNull RssItem rssItem, String sourceName) {
        mFullDrawerLayout.openDrawer(findViewById(R.id.drawerDetails));
        if (rssItem != null) {
            mRssItem = rssItem;
            mTitle.setText(rssItem.getTitle());
            mTimestamp.setText(rssItem.getPublishDate());
            mDescription.setText(rssItem.getDescription());

            mSourceName.setText(sourceName);

            Picasso.with(this)
                    .load(rssItem.getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(mImageView);

            isFavorite = mPresenter.onCheckFavorites(rssItem.getPublishDate());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        MenuItem item = menu.findItem(R.id.mAdd2Favorites);
        if (isFavorite) {
            item.setIcon(R.drawable.ic_favorite_24dp);
        } else {
            item.setIcon(R.drawable.ic_favorite_border_24dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mFullDrawerLayout.closeDrawer(findViewById(R.id.drawerDetails));
                onRefreshDrawerDetails();
                return true;
            case R.id.mAdd2Favorites:
                if (isFavorite) {
                    mPresenter.OnRemoveFromFavorites(mRssItem.getPublishDate());
                } else {
                    mPresenter.OnAddToFavorites(mRssItem);
                }
                return true;
            case R.id.mShare:
                onShareNews();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
    public void onBackPressed() {
        if (mFullDrawerLayout.isDrawerOpen(findViewById(R.id.drawerDetails))) {
            mFullDrawerLayout.closeDrawer(findViewById(R.id.drawerDetails));
            onRefreshDrawerDetails();
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


        builder.setTitle(R.string.tvEnter);
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (mFabMenu.isOpened()) {

                Rect outRect = new Rect();
                mFabMenu.getGlobalVisibleRect(outRect);

                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    mFabMenu.close(true);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }
}
