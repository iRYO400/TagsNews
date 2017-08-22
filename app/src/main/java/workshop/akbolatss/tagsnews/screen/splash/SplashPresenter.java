package workshop.akbolatss.tagsnews.screen.splash;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

import javax.inject.Inject;

import workshop.akbolatss.tagsnews.base.BasePresenter;
import workshop.akbolatss.tagsnews.repositories.DBRssSourceRepository;
import workshop.akbolatss.tagsnews.repositories.source.RssSource;
import workshop.akbolatss.tagsnews.util.Constants;

/**
 * Created by AkbolatSS on 11.08.2017.
 */

public class SplashPresenter extends BasePresenter<SplashView> {

    @Inject
    protected Context mContext;

    @Inject
    protected DBRssSourceRepository mRepository;


    @Inject
    public SplashPresenter() {
    }

    public void onInitBaseSources() {

        if (!isAlreadyInited()) {
            RssSource rssSource = new RssSource();
            rssSource.setIsActive(true);
            rssSource.setTitle("DTF.ru");
            rssSource.setLink("https://dtf.ru/rss/all");
            rssSource.setDescription("DTF — игры, разработка, монетизация, продвижение");
            mRepository.addNewSource(rssSource);

            rssSource = new RssSource();
            rssSource.setIsActive(true);
            rssSource.setTitle("3DNews.ru");
            rssSource.setLink("https://3dnews.ru/software-news/rss/");
            rssSource.setDescription("");
            mRepository.addNewSource(rssSource);

            rssSource = new RssSource();
            rssSource.setIsActive(true);
            rssSource.setTitle("Новости — Игромания");
            rssSource.setLink("http://www.igromania.ru/rss/news.rss");
            rssSource.setDescription("Игромания — это оперативные новости из мира игр и кино от крупнейшего развлекательного медиа России.");
            mRepository.addNewSource(rssSource);

            rssSource = new RssSource();
            rssSource.setIsActive(true);
            rssSource.setTitle("Kanobu.ru: статьи");
            rssSource.setLink("http://kanobu.ru/rss/best/");
            rssSource.setDescription("Статьи от KANOBU.ru: обзоры, интервью, рецензиии");
            mRepository.addNewSource(rssSource);
        }
    }

    public boolean isAlreadyInited() {
//        Hawk.init(mContext).build();

        if (Hawk.contains(Constants.FIRST_START)){
            return true;
        } else {
            Hawk.put(Constants.FIRST_START, 1);
            return false;
        }
    }
}
