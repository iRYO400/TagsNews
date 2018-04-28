package workshop.akbolatss.tagsnews.screen.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;

import javax.inject.Inject;

import me.toptas.rssconverter.RssFeed;
import me.toptas.rssconverter.RssItem;
import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.application.App;
import workshop.akbolatss.tagsnews.di.component.AppComponent;
import workshop.akbolatss.tagsnews.di.component.DaggerNewsComponent;
import workshop.akbolatss.tagsnews.di.module.NewsListModule;
import workshop.akbolatss.tagsnews.screen.details.DetailsActivity;
import workshop.akbolatss.tagsnews.util.Constants;

public class NewsFragment extends Fragment implements NewsView, SwipeRefreshLayout.OnRefreshListener,
        NewsListAdapter.OnRssClickListener {

    private static final String TAG = "NewsFragment";
    @Inject
    protected NewsPresenter mPresenter;

    @Inject
    protected Context mContext;

    protected SwipeRefreshLayout mSwipeRefresh;

    protected RecyclerView mRecyclerView;
    private NewsListAdapter mNewsListAdapter;

    /**
     * RSS feed url
     */
    private String mUrl;
    /**
     * Name of RSS feed
     */
    private String mName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        mSwipeRefresh = rootView.findViewById(R.id.swipeRefresh);
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        initDagger();
        initDefault();
        return rootView;
    }

    protected void initDagger() {
        DaggerNewsComponent.builder()
                .appComponent(getAppComponent())
                .newsListModule(new NewsListModule(this))
                .build()
                .inject(this);
    }

    private AppComponent getAppComponent() {
        return ((App) getContext().getApplicationContext()).getAppComponent();
    }

    protected void initDefault() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            for (int i = 0; i < 10; i++) {
                if (bundle.containsKey(String.valueOf(i))) {
                    mName = bundle.getString(String.valueOf(i));
                }
                mUrl = bundle.getString(mName);
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onInitSwipeRefresh();
        onInitRecycler();

        mPresenter.setUrl(mUrl);
        mPresenter.onLoadNews();
    }

    private void onInitSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefresh.setOnRefreshListener(this);
    }

    private void onInitRecycler() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setNestedScrollingEnabled(false);

        mNewsListAdapter = new NewsListAdapter(this, Hawk.get(Constants.ITEMS_VIEW_MODE, 0));
        mRecyclerView.setAdapter(mNewsListAdapter);
    }

    @Override
    public void onRefresh() {
        mPresenter.onLoadNews();
    }

    @Override
    public void onShowLoading() {
        mSwipeRefresh.setRefreshing(true);
    }

    @Override
    public void onHideLoading() {
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void onNoContent(boolean isEmpty) {
    }

    @Override
    public void onLoadNews(RssFeed rssFeed) {
        mNewsListAdapter.onAddItems(rssFeed.getItems());
    }

    @Override
    public void OnItemClick(@NonNull RssItem rssItem) {
//        ((BoardActivity) getActivity()).onOpenItemDetails(rssItem, mName);
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra("RssItem", rssItem);
        startActivity(intent);
    }

    @Override
    public void onUnknownError(String errorMessage) {
        Toast.makeText(mContext, R.string.unknown_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTimeout() {
        Toast.makeText(mContext, R.string.timeout_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNetworkError() {
        Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_LONG).show();
    }
}
