package workshop.akbolatss.tagsnews.screen.news;

import java.util.List;

import workshop.akbolatss.tagsnews.base.BaseView;
import workshop.akbolatss.tagsnews.screen.news.model.News;

/**
 * Created by AkbolatSS on 08.08.2017.
 */

public interface NewsView extends BaseView {

    public void onShowLoading();
    public void onHideLoading();

    public void onShowError();

    public void onLoadNews(News news);

    public void onOpenDetails();

}
