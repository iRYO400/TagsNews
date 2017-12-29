package workshop.akbolatss.tagsnews.screen.recommendations;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;
import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.application.App;
import workshop.akbolatss.tagsnews.di.component.AppComponent;
import workshop.akbolatss.tagsnews.di.component.DaggerSourcesComponent;
import workshop.akbolatss.tagsnews.di.module.SourcesModule;
import workshop.akbolatss.tagsnews.repositories.source.RssSource;
import workshop.akbolatss.tagsnews.screen.sources.SourcesPresenter;
import workshop.akbolatss.tagsnews.screen.sources.SourcesView;

public class RecommendationsFragment extends Fragment implements SourcesView, RecommendationsAdapter.onSourceClickListener {

    private static final String TAG = "RecommendationsFragment";

    @Inject
    protected Context mContext;

    @Inject
    protected SourcesPresenter mPresenter;

    @BindView(R.id.tvNoContent)
    protected TextView tvNoContent;

    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerV;
    private RecommendationsAdapter mAdapter;

    @BindView(R.id.btnSearch)
    protected ImageButton mBtnSearch;

    @BindView(R.id.progressBar)
    protected ProgressBar mProgress;

    @BindView(R.id.edit_query)
    protected EditText mEditText;

    private OnFragmentInteractionListener mListener;

    private BehaviorSubject<String> searchSubject;

    public RecommendationsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recommendations, container, false);
        ButterKnife.bind(this, rootView);
        initDagger();
        return rootView;
    }

    protected void initDagger() {
        DaggerSourcesComponent.builder()
                .appComponent(getAppComponent())
                .sourcesModule(new SourcesModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initRV();
        initEditText();
    }

    private void initRV() {
        mRecyclerV.setHasFixedSize(true);
        mRecyclerV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerV.setNestedScrollingEnabled(false);
        mAdapter = new RecommendationsAdapter(this);
        mRecyclerV.setAdapter(mAdapter);
    }

    private void initEditText() {
        searchSubject = BehaviorSubject.create();
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId != 0 || event.getAction() == KeyEvent.ACTION_DOWN) {
                    onSubmitQuery();
                    View view = getActivity().findViewById(android.R.id.content);
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });

        String[] suggestions = getResources().getStringArray(R.array.suggestions);
        mEditText.setText(suggestions[new Random().nextInt(9)]);
        onSubmitQuery();
    }

    @OnClick(R.id.btnSearch)
    protected void onSubmitQuery() {
        if (mEditText.getText().toString().trim().length() > 0) {
            mPresenter.onSearchSources(mEditText.getText().toString().trim());
        }
    }

    @OnFocusChange(R.id.edit_query)
    protected void onFocusChanged(View view, boolean isFocused) {
        if (view.getId() == R.id.edit_query) {
            if (isFocused) {
                searchSubject.debounce(1000, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                if (s.trim().length() > 0) {
                                    mPresenter.onSearchSources(s);
                                    mBtnSearch.setVisibility(View.GONE);
                                    mProgress.setVisibility(View.VISIBLE);
                                }
                            }
                        });
            }
        }
    }

    @OnTextChanged(R.id.edit_query)
    protected void onEtCurrencyChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (!charSequence.toString().isEmpty()) {
            searchSubject.onNext(charSequence.toString());
        } else {
            mAdapter.onClearItems();
        }
    }

    @Override
    public void onAddNewSource() {
    }

    @Override
    public void onLoadSources(List<RssSource> rssSourceList) {
        mAdapter.onAddItems(rssSourceList);
    }

    @Override
    public void onNoContent(boolean isEmpty) {
        if (isEmpty) {
            mRecyclerV.setVisibility(View.GONE);
            tvNoContent.setVisibility(View.VISIBLE);
        } else {
            mRecyclerV.setVisibility(View.VISIBLE);
            tvNoContent.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSourceClick(RssSource rssSource, boolean toDelete) {
        if (toDelete) {
            mPresenter.onRemoveSource(rssSource);
        } else {
            mPresenter.onAddNewSource(rssSource);
        }
        mListener.onUpdateRSS();
    }

    @Override
    public void onShowLoading() {
        mBtnSearch.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideLoading() {
        mBtnSearch.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
    }

    private AppComponent getAppComponent() {
        return ((App) getContext().getApplicationContext()).getAppComponent();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    public interface OnFragmentInteractionListener {
        void onUpdateRSS();
    }
}
