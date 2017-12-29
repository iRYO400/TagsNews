package workshop.akbolatss.tagsnews.base;

import javax.inject.Inject;

public class BasePresenter<V extends BaseView> {

    @Inject
    protected V mView;

    public V getView() {
        return mView;
    }
}
