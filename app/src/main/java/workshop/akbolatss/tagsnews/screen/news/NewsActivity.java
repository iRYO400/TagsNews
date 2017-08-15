package workshop.akbolatss.tagsnews.screen.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.application.App;
import workshop.akbolatss.tagsnews.base.BaseActivity;
import workshop.akbolatss.tagsnews.di.component.DaggerNewsComponent;
import workshop.akbolatss.tagsnews.screen.favorites.FavoritesActivity;

public class NewsActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    protected ViewPager mViewPager;

    @BindView(R.id.tabs)
    protected TabLayout mTabLayout;

    @BindView(R.id.btnFam)
    protected FloatingActionsMenu mFabMenu;

    @BindView(R.id.btnFab1)
    protected FloatingActionButton mFab1;
    @BindView(R.id.btnFab2)
    protected FloatingActionButton mFab2;

    private NewsSectionsPagerAdapter mSectionsAdapter;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        initFragments();
    }

    private void initFragments() {

        List<NewsSource> fragments = new ArrayList<>();
        fragments.add(new NewsSource("DTF.ru", "https://dtf.ru/rss/all"));
        fragments.add(new NewsSource("3DNews.ru", "https://3dnews.ru/software-news/rss/"));
        fragments.add(new NewsSource("4PDA.ru", "https://4pda.ru/feed/"));
        fragments.add(new NewsSource("COSSA.ru", "http://www.cossa.ru/rss/"));

        mSectionsAdapter = new NewsSectionsPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mSectionsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @OnClick(R.id.btnFab1)
    public void OnOpenFavorites() {
        mFabMenu.collapse();
        Intent i = new Intent(this, FavoritesActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btnFab2)
    public void OnAddNewChannel() {
        mFabMenu.collapse();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (mFabMenu.isExpanded()) {

                Rect outRect = new Rect();
                mFabMenu.getGlobalVisibleRect(outRect);

                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    mFabMenu.collapse();
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
