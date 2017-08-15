package workshop.akbolatss.tagsnews.screen.details;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import me.toptas.rssconverter.RssItem;
import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.application.App;
import workshop.akbolatss.tagsnews.base.BaseActivity;
import workshop.akbolatss.tagsnews.di.component.DaggerDetailsComponent;
import workshop.akbolatss.tagsnews.di.module.DetailsModule;
import workshop.akbolatss.tagsnews.repositories.source.DaoSession;
import workshop.akbolatss.tagsnews.util.Constants;

/**
 * Created by AkbolatSS on 10.08.2017.
 */

public class DetailsActivity extends BaseActivity implements DetailsView {

    @Inject
    protected DetailsPresenter mPresenter;

    @Inject
    protected DaoSession mDaoSession;

    @Inject
    protected Context mContext;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    private RssItem mRssItem;
    @BindView(R.id.tvTitle)
    protected TextView mTitle;
    @BindView(R.id.tvTimestamp)
    protected TextView mTimestamp;
    @BindView(R.id.tvDescription)
    protected TextView mDescription;
    @BindView(R.id.imgView)
    protected ImageView mImage;

    private boolean isFavorite;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DaggerDetailsComponent.builder()
                .appComponent(App.getAppComponent())
                .detailsModule(new DetailsModule(this))
                .build().inject(this);

        initView();
    }

    private void initView() {
        mRssItem = (RssItem) getIntent().getSerializableExtra(Constants.INTENT_RSS_ITEM);
        if (mRssItem != null) {
            mTitle.setText(mRssItem.getTitle());
            mTimestamp.setText(mRssItem.getPublishDate());
            mDescription.setText(mRssItem.getDescription());

            Picasso.with(mContext)
                    .load(mRssItem.getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(mImage);


            isFavorite = mPresenter.onCheckFavorites(mRssItem.getPublishDate());
        }
    }

    @Override
    public void onRefreshToolbar(boolean isFavorite) {
        this.isFavorite = isFavorite;
        if (isFavorite) {
            mToolbar.getMenu().findItem(R.id.action_favorite).setIcon(R.drawable.ic_favorite_24dp);
        } else {
            mToolbar.getMenu().findItem(R.id.action_favorite).setIcon(R.drawable.ic_favorite_border_24dp);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        MenuItem item = menu.findItem(R.id.action_favorite);
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
                onBackPressed();
                return true;
            case R.id.action_favorite:
                if (isFavorite) {
                    mPresenter.OnRemoveFromFavorites(mRssItem.getPublishDate());
                } else {
                    mPresenter.OnAddToFavorites(mRssItem);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_details;
    }

    @OnClick(R.id.btnOpenSource)
    @Override
    public void onOpenSource() {
        //Start Custom ChromeExtension
        Toast.makeText(mContext, mRssItem.getTitle(), Toast.LENGTH_SHORT).show();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorAccent));
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_cloud_queue_black_24dp));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(mRssItem.getLink()));
    }
}
