package workshop.akbolatss.tagsnews.screen.board;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioGroup;

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

import static workshop.akbolatss.tagsnews.util.Constants.ITEMS_VIEW_MODE;
import static workshop.akbolatss.tagsnews.util.Constants.SELECTED_THEME;

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

        mPresenter.onLoadSources();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (isUpdateBoardNeeded) {
            isUpdateBoardNeeded = false;
            mPresenter.onLoadSources();
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
        LayoutInflater layoutInflater = LayoutInflater.from(BoardActivity.this);
        final View subView = layoutInflater.inflate(R.layout.dialog_theme, null);

        final RadioGroup rGroupTheme = subView.findViewById(R.id.rGroupTheme);
        final RadioGroup rGroupViewItems = subView.findViewById(R.id.rGroupViewItems);

        final AppCompatRadioButton rbDaytheme = rGroupTheme.findViewById(R.id.rButtonDay);
        final AppCompatRadioButton rbNightTheme = rGroupTheme.findViewById(R.id.rButtonNight);
        final AppCompatRadioButton rbTextOnly = rGroupViewItems.findViewById(R.id.rButtonTextOnly);
        final AppCompatRadioButton rbSmall = rGroupViewItems.findViewById(R.id.rButtonSmallBlock);
        final AppCompatRadioButton rbLarge = rGroupViewItems.findViewById(R.id.rButtonLargeBlock);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Dialog);

        if (Hawk.contains(SELECTED_THEME)) { // False = Day, True = Night
            if (Hawk.get(SELECTED_THEME)) {
                rGroupTheme.check(rbNightTheme.getId());
            } else {
                rGroupTheme.check(rbDaytheme.getId());
            }
        } else {
            Hawk.put(SELECTED_THEME, true);
            rGroupTheme.check(rbNightTheme.getId());
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
            Hawk.put(ITEMS_VIEW_MODE, 2);
            rGroupViewItems.check(rbLarge.getId());
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

                if (rbDaytheme.isChecked()) {
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
