package workshop.akbolatss.tagsnews.screen.sources;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.github.clans.fab.FloatingActionButton;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;
import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.application.App;
import workshop.akbolatss.tagsnews.base.BaseActivity;
import workshop.akbolatss.tagsnews.di.component.DaggerSourcesComponent;
import workshop.akbolatss.tagsnews.di.module.SourcesModule;
import workshop.akbolatss.tagsnews.repositories.source.RssSource;
import workshop.akbolatss.tagsnews.screen.sources.helper.SimpleItemTouchHelperCallback;

/**
 * Created by AkbolatSS on 17.08.2017.
 */

public class SourcesActivity extends BaseActivity implements SourcesView,
        SourcesAdapter.OnRssClickInterface, SearchView.OnQueryTextListener,
        View.OnClickListener {

    @Inject
    protected Context mContext;

    @Inject
    protected SourcesPresenter mPresenter;

    @BindView(R.id.btnFabAdd)
    protected FloatingActionButton btnFabAdd;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.progressBar)
    protected ProgressBar mProgressBar;

    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;
    private SourcesAdapter mSourcesAdapter;

    @BindView(R.id.recyclerViewSearch)
    protected RecyclerView mRecyclerViewResult;
    private SourcesSearchAdapter mSourcesSearchAdapter;

    private boolean isSearchEnabled;
    private BehaviorSubject<String> searchSubject;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DaggerSourcesComponent.builder()
                .appComponent(App.getAppComponent())
                .sourcesModule(new SourcesModule(this))
                .build()
                .inject(this);

        onInitRecycler();
        onInitRecyclerResult();

        mPresenter.onLoadSources();
    }

    private void onInitRecycler() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setNestedScrollingEnabled(false);

        mSourcesAdapter = new SourcesAdapter(this);
        mRecyclerView.setAdapter(mSourcesAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mSourcesAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void onInitRecyclerResult() {
        mRecyclerViewResult.setHasFixedSize(true);
        mRecyclerViewResult.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerViewResult.setNestedScrollingEnabled(false);

        mSourcesSearchAdapter = new SourcesSearchAdapter(this);
        mRecyclerViewResult.setAdapter(mSourcesSearchAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.searchMenu);
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setOnSearchClickListener(this);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mRecyclerViewResult.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);

                isSearchEnabled = false;

                searchSubject.onComplete();
                return false;
            }
        });


        searchView.setQueryHint(getResources().getString(R.string.hint_search));
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorWhite));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorWhite));
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchMenu:
                mRecyclerView.setVisibility(View.GONE);
                mRecyclerViewResult.setVisibility(View.VISIBLE);

                isSearchEnabled = true;

                searchSubject = BehaviorSubject.create();
                searchSubject.debounce(500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                mPresenter.onSearchSources(s);
                            }
                        });
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchSubject.onComplete();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()) {
            searchSubject.onNext(newText);
        }
        return true;
    }

    @OnClick(R.id.btnFabAdd)
    @Override
    public void onAddNewSource() {
        LayoutInflater layoutInflater = LayoutInflater.from(SourcesActivity.this);
        final View subView = layoutInflater.inflate(R.layout.dialog_new_source, null);
        final EditText etLink = subView.findViewById(R.id.etLink);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Dialog);

        builder.setTitle(R.string.tvEnter);
        builder.setView(subView);
        builder.setPositiveButton(R.string.tvAdd, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RssSource rssSource = new RssSource();
                rssSource.setIsActive(true);
                rssSource.setTitle(etLink.getText().toString());
                rssSource.setLink(etLink.getText().toString());
                mPresenter.onAddNewSource(rssSource);

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
    public void onLoadSources(List<RssSource> rssSourceList) {
        if (!isSearchEnabled) {
            mSourcesAdapter.onAddItems(rssSourceList);
        } else {
            mSourcesSearchAdapter.onAddItems(rssSourceList);
        }
    }

    @Override
    public void onItemsSwapped(RssSource from, RssSource to) {
        mPresenter.onSwapPositions(from, to);
    }

    @Override
    public void onItemCheckBoxClick(final RssSource rssSource, View view) {
        if (!isSearchEnabled) {
            if (view instanceof CheckBox) {
                CheckBox cb = (CheckBox) view;
                if (cb.isChecked()) {
                    rssSource.setIsActive(true);
                } else {
                    rssSource.setIsActive(false);
                }
                mPresenter.onUpdateSource(rssSource);
            } else {
                PopupMenu popupMenu = new PopupMenu(this, view);
                popupMenu.inflate(R.menu.menu_popup_source);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.mRename:
                                LayoutInflater layoutInflater = LayoutInflater.from(SourcesActivity.this);
                                final View subView = layoutInflater.inflate(R.layout.dialog_new_source, null);
                                final EditText etLink = subView.findViewById(R.id.etLink);

                                etLink.setText(rssSource.getTitle());
                                etLink.setHint(R.string.tvNewName);
                                AlertDialog.Builder builder = new AlertDialog.Builder(SourcesActivity.this, R.style.Dialog);

                                builder.setTitle(R.string.tvEnter);
                                builder.setView(subView);
                                builder.setPositiveButton(R.string.tvEdit, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        rssSource.setTitle(etLink.getText().toString());
                                        mPresenter.onUpdateSource(rssSource);

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
                                return true;
                            case R.id.mReaddress:
                                LayoutInflater layoutInflater2 = LayoutInflater.from(SourcesActivity.this);
                                final View subView2 = layoutInflater2.inflate(R.layout.dialog_new_source, null);
                                final EditText etLink2 = subView2.findViewById(R.id.etLink);

                                etLink2.setText(rssSource.getLink());
                                etLink2.setHint(R.string.tvNewAddress);
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(SourcesActivity.this, R.style.Dialog);

                                builder2.setTitle(R.string.tvEnter);
                                builder2.setView(subView2);
                                builder2.setPositiveButton(R.string.tvEdit, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        rssSource.setLink(etLink2.getText().toString());
                                        mPresenter.onUpdateSource(rssSource);

                                        dialogInterface.dismiss();
                                    }
                                });
                                builder2.setNegativeButton(R.string.tvCancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                builder2.show();
                                return true;
                            case R.id.mRemove:
                                mPresenter.onRemoveSource(rssSource);
                                return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        } else {
            CheckBox cb = (CheckBox) view;
            if (cb.isChecked()) {
                rssSource.setIsActive(true);
                mPresenter.onAddNewSource(rssSource);
            } else {
                mPresenter.onRemoveSource(rssSource);
            }
        }
    }

    @Override
    public void onUpdate() {
        mPresenter.onLoadSources();
    }

    @Override
    public void onShowLoading() {
        mRecyclerView.setVisibility(View.GONE);
        mRecyclerViewResult.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideLoading() {
        mProgressBar.setVisibility(View.GONE);
        if (!isSearchEnabled) {
            mRecyclerViewResult.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mRecyclerViewResult.setVisibility(View.VISIBLE);
        }
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
        return R.layout.activity_sources;
    }
}
