package workshop.akbolatss.tagsnews.screen.sources;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.base.BaseActivity;
import workshop.akbolatss.tagsnews.di.component.DaggerSourcesComponent;
import workshop.akbolatss.tagsnews.di.module.SourcesModule;
import workshop.akbolatss.tagsnews.repositories.source.RssSource;
import workshop.akbolatss.tagsnews.screen.sources.helper.SimpleItemTouchHelperCallback;

public class SourcesActivity extends BaseActivity implements SourcesView, SourcesAdapter.OnRssClickListener {

    @Inject
    protected Context mContext;

    @Inject
    protected SourcesPresenter mPresenter;

    @BindView(R.id.btnFabAdd)
    protected FloatingActionButton btnFabAdd;

    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @BindView(R.id.tvNoContent)
    protected TextView mNoContent;

    @BindView(R.id.progressBar)
    protected ProgressBar mProgressBar;

    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;
    private SourcesAdapter mSourcesAdapter;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DaggerSourcesComponent.builder()
                .appComponent(getAppComponent())
                .sourcesModule(new SourcesModule(this))
                .build()
                .inject(this);

        onInitRecycler();

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

                mSourcesAdapter.onAddItem(rssSource);
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
        mSourcesAdapter.onAddItems(rssSourceList);
    }

    @Override
    public void onItemsSwapped(RssSource from, RssSource to) {
        mPresenter.onSwapPositions(from, to);
    }

    @Override
    public void onSourceOptions(final RssSource rssSource, View view, final int pos) {
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
                                mSourcesAdapter.onUpdateItem(rssSource, pos);
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
                                mSourcesAdapter.onUpdateItem(rssSource, pos);
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
                        mSourcesAdapter.onRemoveItem(pos);
                        return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public void onSourceSwitch(RssSource rssSource, boolean isActivated, int pos) {
        if (isActivated) {
            rssSource.setIsActive(true);
        } else {
            rssSource.setIsActive(false);
        }
        mPresenter.onUpdateSource(rssSource);
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

    @Override
    public void onShowLoading() {
        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideLoading() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onNoContent(boolean isEmpty) {
        if (isEmpty) {
            mRecyclerView.setVisibility(View.GONE);
            mNoContent.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoContent.setVisibility(View.GONE);
        }
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
