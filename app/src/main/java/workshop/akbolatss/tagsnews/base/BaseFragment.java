package workshop.akbolatss.tagsnews.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import workshop.akbolatss.tagsnews.application.App;
import workshop.akbolatss.tagsnews.di.component.AppComponent;

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView(inflater, container));
        initDagger();
        initDefault();
        return getContentView(inflater, container);
    }

    protected AppComponent getAppComponent() {
        return ((App) getContext().getApplicationContext()).getAppComponent();
    }

    protected abstract View getContentView(LayoutInflater inflater, @Nullable ViewGroup container);

    protected abstract void initDagger();

    protected abstract void initDefault();
}
