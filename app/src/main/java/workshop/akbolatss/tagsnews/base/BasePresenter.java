package workshop.akbolatss.tagsnews.base;

import javax.inject.Inject;

/**
 * Created by AkbolatSS on 08.08.2017.
 */

public class BasePresenter<V extends BaseView> {

    @Inject
    protected V mView;

    public V getView() {
        return mView;
    }
}
