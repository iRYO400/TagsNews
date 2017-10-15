package workshop.akbolatss.tagsnews.screen.news;

import me.toptas.rssconverter.RssFeed;
import workshop.akbolatss.tagsnews.base.BaseView;

/**
 * Created by AkbolatSS on 08.08.2017.
 */

public interface NewsView extends BaseView {

    public void onShowLoading();

    public void onHideLoading();

    public void onShowError();

    public void onLoadNews(RssFeed rssFeed);

}
