package workshop.akbolatss.tagsnews.screen.favorites;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import me.toptas.rssconverter.RssItem;
import vcm.github.webkit.proview.ProWebView;
import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.base.BaseActivity;
import workshop.akbolatss.tagsnews.di.component.DaggerFavoritesComponent;
import workshop.akbolatss.tagsnews.di.module.DetailsModule;
import workshop.akbolatss.tagsnews.di.module.FavoritesModule;
import workshop.akbolatss.tagsnews.screen.details.DetailsPresenter;
import workshop.akbolatss.tagsnews.screen.details.DetailsView;
import workshop.akbolatss.tagsnews.screen.news.NewsListAdapter;
import workshop.akbolatss.tagsnews.util.AdvancedDrawerLayout;
import workshop.akbolatss.tagsnews.util.Constants;
import workshop.akbolatss.tagsnews.util.FullDrawerLayout;

import static workshop.akbolatss.tagsnews.util.Constants.FB_PACKAGE_NAME;
import static workshop.akbolatss.tagsnews.util.Constants.TW_PACKAGE_NAME;
import static workshop.akbolatss.tagsnews.util.Constants.VK_PACKAGE_NAME;
import static workshop.akbolatss.tagsnews.util.UtilityMethods.isWifiConnected;

public class FavoritesActivity extends BaseActivity implements FavoritesView, DetailsView, NewsListAdapter.OnRssClickListener {

    @Inject
    protected FavoritesPresenter mPresenter;

    @Inject
    protected DetailsPresenter mDetailsPresenter;

    @BindView(R.id.fullDrawer)
    protected FullDrawerLayout mFullDrawerLayout;

    @Inject
    protected Context mContext;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;

    private FavoritesListAdapter mListAdapter;

    //Details activity's views
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
    protected View rootView;

    @BindView(R.id.drawerDetails)
    protected View detailsView;

    @BindView(R.id.webView)
    protected ProWebView mProWebView;
    private boolean isUrlStartLoading; // For preloading when Wi-Fi is connected

    private String mCurrPageUrl;
    private String mCurrPageTitle;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DaggerFavoritesComponent.builder()
                .appComponent(getAppComponent())
                .favoritesModule(new FavoritesModule(this))
                .detailsModule(new DetailsModule(this))
                .build()
                .inject(this);

        mProWebView.setActivity(this);

        mFullDrawerLayout.setDrawerLockMode(AdvancedDrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        mFullDrawerLayout.addDrawerListener(new AdvancedDrawerLayout.AdvancedDrawerLayoutListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (drawerView.getId() == R.id.drawerDetails) {
                    rootView.setX(slideOffset * -100);
                    if (slideOffset >= 0.99f) {
                        rootView.setVisibility(View.GONE);
                    } else {
                        rootView.setVisibility(View.VISIBLE);
                    }
                }
                if (drawerView.getId() == R.id.drawerWebView) {
                    detailsView.setX(slideOffset * -100);
                    if (slideOffset >= 0.99f) {
                        detailsView.setVisibility(View.GONE);
                    } else {
                        detailsView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mFullDrawerLayout.setDrawerLockMode(AdvancedDrawerLayout.LOCK_MODE_UNLOCKED);
                mToolbar.getMenu().findItem(R.id.mAdd2Favorites).setIcon(R.drawable.ic_favorite_24dp);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (drawerView.getId() == R.id.drawerDetails) {
                    mFullDrawerLayout.setDrawerLockMode(AdvancedDrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                } else if (drawerView.getId() == R.id.drawerWebView) {
                    isUrlStartLoading = false;
                }
                mProWebView.showBlank();
                mProWebView.clearCache();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        onInitRecycler();
        mPresenter.onLoadFavorites();

        mProWebView.setProClient(new ProWebView.ProClient() {
            @Override
            public void onInformationReceived(ProWebView webView, String url, String title, Bitmap favicon) {
                mCurrPageUrl = url;
                mCurrPageTitle = title;
            }
        });
    }

    @Override
    public void OnItemClick(RssItem rssItem) {
        onOpenItemDetails(rssItem, "");
    }

    @Override
    public void onRefreshDrawerDetails() {
        mImageView.setImageBitmap(null);
        mImageView.destroyDrawingCache();
    }

    @Override
    public void onOpenItemDetails(@NonNull final RssItem rssItem, String sourceName) {
        mFullDrawerLayout.openDrawer(findViewById(R.id.drawerDetails));

        mRssItem = rssItem;

        mTitle.setText(rssItem.getTitle());
        mTimestamp.setText(rssItem.getPublishDate());
        mDescription.setText(rssItem.getDescription());

        mSourceName.setText(sourceName);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Picasso.with(FavoritesActivity.this)
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
        mFullDrawerLayout.openDrawer(findViewById(R.id.drawerWebView));
        if (!isUrlStartLoading) {
            mProWebView.loadUrl(mRssItem.getLink());
        }
    }

    @OnClick(R.id.btnClose)
    @Override
    public void onCloseWeb() {
        mFullDrawerLayout.closeDrawer(findViewById(R.id.drawerWebView));
    }

    @OnClick(R.id.btnBack)
    @Override
    public void onBackWebPage() {
        if (mProWebView.canGoBack()) {
            mProWebView.goBack();
        }
    }

    @OnClick(R.id.btnForward)
    @Override
    public void onForwardWebPage() {
        if (mProWebView.canGoForward()) {
            mProWebView.goForward();
        }
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
    public void onRefreshToolbar(boolean isFavorite) {
        mImageView.setImageBitmap(null);
        mImageView.destroyDrawingCache();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, mToolbar.getMenu());
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
                if (mFullDrawerLayout.isDrawerOpen(findViewById(R.id.drawerDetails))) {
                    mFullDrawerLayout.closeDrawer(findViewById(R.id.drawerDetails));
                    onRefreshDrawerDetails();
                } else {
                    super.onBackPressed();
                }
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
    public void onShareWithWebIntent(String socialNetwrkId) {
        String shareUrl = "market://details?id=" + socialNetwrkId;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(shareUrl));
        startActivity(intent);
    }

    @OnClick(R.id.btnCloseFavorites)
    @Override
    public void onBackPressed() {
        if (mFullDrawerLayout.isDrawerOpen(findViewById(R.id.drawerWebView))) {
            mFullDrawerLayout.closeDrawer(findViewById(R.id.drawerWebView));
        } else if (mFullDrawerLayout.isDrawerOpen(findViewById(R.id.drawerDetails))) {
            mFullDrawerLayout.closeDrawer(findViewById(R.id.drawerDetails));
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void OnLoadFavorites(List<RssItem> rssItems) {
        mListAdapter.onAddItems(rssItems);
    }

    private void onInitRecycler() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        mListAdapter = new FavoritesListAdapter(this, Hawk.get(Constants.ITEMS_VIEW_MODE, 0));
        mRecyclerView.setAdapter(mListAdapter);
    }

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
        return R.layout.activity_favorites;
    }
}
