package workshop.akbolatss.tagsnews.screen.news;

import me.toptas.rssconverter.RssFeed;
import workshop.akbolatss.tagsnews.base.BaseView;
import workshop.akbolatss.tagsnews.base.ErrorView;
import workshop.akbolatss.tagsnews.base.LoadingView;


public interface NewsView extends BaseView, LoadingView, ErrorView {

    public void onLoadNews(RssFeed rssFeed);

}
