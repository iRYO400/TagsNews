package workshop.akbolatss.tagsnews.screen.board;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

import com.github.clans.fab.FloatingActionMenu;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.application.App;
import workshop.akbolatss.tagsnews.base.BaseActivity;
import workshop.akbolatss.tagsnews.di.component.DaggerBoardComponent;
import workshop.akbolatss.tagsnews.di.module.BoardModule;
import workshop.akbolatss.tagsnews.repositories.source.RssSource;
import workshop.akbolatss.tagsnews.screen.favorites.FavoritesActivity;
import workshop.akbolatss.tagsnews.screen.news.NewsSource;
import workshop.akbolatss.tagsnews.screen.sources.SourcesActivity;
import workshop.akbolatss.tagsnews.util.Constants;

public class BoardActivity extends BaseActivity implements BoardView {

    @Inject
    BoardPresenter mPresenter;

    @BindView(R.id.viewPager)
    protected ViewPager mViewPager;

    @BindView(R.id.tabs)
    protected TabLayout mTabLayout;

    @BindView(R.id.btnFam)
    protected FloatingActionMenu mFabMenu;

    private SectionsPagerAdapter mSectionsAdapter;

    private boolean isUpdateBoardNeeded;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);

        DaggerBoardComponent.builder()
                .appComponent(App.getAppComponent())
                .boardModule(new BoardModule(this))
                .build()
                .inject(this);

        Hawk.init(this).build();

        mPresenter.initPresenter();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (isUpdateBoardNeeded) {
            isUpdateBoardNeeded = false;
            mPresenter.initPresenter();
        }
    }

    @Override
    public void onInitSources(List<RssSource> rssSources) {
        List<NewsSource> fragments = new ArrayList<>();
        for (RssSource rssSource : rssSources) {
            if (rssSource.getIsActive()) {
                fragments.add(new NewsSource(rssSource.getTitle(), rssSource.getLink()));
            }
        }

        mSectionsAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mSectionsAdapter);
        mSectionsAdapter.notifyDataSetChanged();
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @OnClick(R.id.btnFab1)
    public void onOpenFavorites() {
        Intent i = new Intent(this, FavoritesActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btnFab2)
    public void onOpenSourceManager() {
        Intent i = new Intent(this, SourcesActivity.class);
        isUpdateBoardNeeded = true;
        startActivity(i);
    }

    @OnClick(R.id.btnFab3)
    public void onSwitchRVLayout() {
        if (Hawk.contains(Constants.SMALL_ITEMS_MODE)) {
            if (Hawk.get(Constants.SMALL_ITEMS_MODE)) {
                Hawk.put(Constants.SMALL_ITEMS_MODE, false);
            } else {
                Hawk.put(Constants.SMALL_ITEMS_MODE, true);
            }
        } else {
            Hawk.put(Constants.SMALL_ITEMS_MODE, true);
        }
        mPresenter.initPresenter();
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
