package workshop.akbolatss.tagsnews.screen.news;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.toptas.rssconverter.RssFeed;
import me.toptas.rssconverter.RssItem;
import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.application.App;
import workshop.akbolatss.tagsnews.di.component.DaggerNewsComponent;
import workshop.akbolatss.tagsnews.di.module.NewsListModule;
import workshop.akbolatss.tagsnews.screen.board.BoardActivity;
import workshop.akbolatss.tagsnews.util.Constants;

/**
 * Created by AkbolatSS on 09.08.2017.
 */

public class NewsFragment extends Fragment implements NewsView, SwipeRefreshLayout.OnRefreshListener, NewsListAdapter.OnRssClickInterface {

    private static final String TAG = "TAG";
    @Inject
    protected NewsPresenter mPresenter;

    @Inject
    protected Context mContext;

    @BindView(R.id.swipeRefresh)
    protected SwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.recyclerView)
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, rootView);

        DaggerNewsComponent.builder()
                .appComponent(App.getAppComponent())
                .newsListModule(new NewsListModule(this))
                .build().inject(this);

        Bundle bundle = getArguments();
        if (bundle != null) {
            for (int i = 0; i < 10; i++) {
                if (bundle.containsKey(String.valueOf(i))) {
                    mName = bundle.getString(String.valueOf(i));
                }
                mUrl = bundle.getString(mName);
            }
        }
        return rootView;
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
    public void onShowError() {
        Toast.makeText(mContext, "Oops! Let's try again...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadNews(RssFeed rssFeed) {
        mNewsListAdapter.onAddItems(rssFeed.getItems());
    }

    @Override
    public void OnItemClick(@NonNull RssItem rssItem) {
        ((BoardActivity) getActivity()).onOpenItemDetails(rssItem, mName);
    }
}
