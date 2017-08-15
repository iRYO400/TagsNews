package workshop.akbolatss.tagsnews.screen.favorites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.toptas.rssconverter.RssItem;
import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.application.App;
import workshop.akbolatss.tagsnews.base.BaseActivity;
import workshop.akbolatss.tagsnews.di.component.DaggerFavoritesComponent;
import workshop.akbolatss.tagsnews.di.module.FavoritesModule;
import workshop.akbolatss.tagsnews.screen.details.DetailsActivity;
import workshop.akbolatss.tagsnews.screen.news.NewsListAdapter;

import static workshop.akbolatss.tagsnews.util.Constants.INTENT_RSS_ITEM;

/**
 * Created by AkbolatSS on 14.08.2017.
 */

public class FavoritesActivity extends BaseActivity implements FavoritesView, NewsListAdapter.OnRssClickInterface {

    @Inject
    protected FavoritesPresenter mPresenter;

    @Inject
    protected Context mContext;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;
    private NewsListAdapter mNewsListAdapter;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DaggerFavoritesComponent.builder()
                .appComponent(App.getAppComponent())
                .favoritesModule(new FavoritesModule(this))
                .build().inject(this);


        onInitRecycler();
        mPresenter.onLoadFavorites();
    }


    @Override
    public void OnLoadFavorites(List<RssItem> rssItems) {
        mNewsListAdapter.onAddItems(rssItems);
    }

    private void onInitRecycler() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setNestedScrollingEnabled(false);

        mNewsListAdapter = new NewsListAdapter(this);
        mRecyclerView.setAdapter(mNewsListAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_favorites;
    }

    @Override
    public void OnItemClick(RssItem rssItem) {
        Intent i = new Intent(mContext, DetailsActivity.class);
        i.putExtra(INTENT_RSS_ITEM, rssItem);
        startActivity(i);
    }

}
