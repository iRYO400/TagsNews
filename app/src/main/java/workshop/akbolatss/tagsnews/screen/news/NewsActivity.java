package workshop.akbolatss.tagsnews.screen.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.application.App;
import workshop.akbolatss.tagsnews.base.BaseActivity;
import workshop.akbolatss.tagsnews.di.component.DaggerNewsComponent;
import workshop.akbolatss.tagsnews.di.module.NewsListModule;
import workshop.akbolatss.tagsnews.screen.news.model.News;

public class NewsActivity extends BaseActivity implements NewsView {

    @Inject
    protected NewsPresenter mPresenter;

    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;
    private NewsAdapter mNewsAdapter;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);

        DaggerNewsComponent.builder()
                .appComponent(App.getAppComponent())
                .newsListModule(new NewsListModule(this))
                .build().inject(this);

        onInitRecycler();
        mPresenter.onLoadNews();
    }

    private void onInitRecycler() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));

        mNewsAdapter = new NewsAdapter(getLayoutInflater());
        mRecyclerView.setAdapter(mNewsAdapter);
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void onShowLoading() {
        Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onHideLoading() {
        Toast.makeText(this, "Hide...", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onShowError() {
        Toast.makeText(this, "ERROR caught", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoadNews(News news) {
        mNewsAdapter.onAddArticles(news.getArticles());
    }

    @Override
    public void onOpenDetails() {

    }
}
