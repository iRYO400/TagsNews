package workshop.akbolatss.tagsnews.screen.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.base.BaseActivity;

public class NewsActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    protected ViewPager mViewPager;

    @BindView(R.id.tabs)
    protected TabLayout mTabLayout;

    private NewsSectionsPagerAdapter mSectionsAdapter;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);

        initFragments();

    }

    private void initFragments(){

        List<NewsSource> fragments = new ArrayList<>();
        fragments.add(new NewsSource("DTF.ru", "https://dtf.ru/rss/all"));
        fragments.add(new NewsSource("3DNews.ru", "https://3dnews.ru/software-news/rss/"));
        fragments.add(new NewsSource("4PDA.ru", "https://4pda.ru/feed/"));
        fragments.add(new NewsSource("COSSA.ru", "http://www.cossa.ru/rss/"));

        mSectionsAdapter = new NewsSectionsPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mSectionsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

}
