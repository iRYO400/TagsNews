package workshop.akbolatss.tagsnews.di.module;

import dagger.Module;
import dagger.Provides;
import workshop.akbolatss.tagsnews.api.NewsApiService;
import workshop.akbolatss.tagsnews.di.scope.ActivityScope;
import workshop.akbolatss.tagsnews.repositories.DBRssSourceRepository;
import workshop.akbolatss.tagsnews.repositories.source.DaoSession;
import workshop.akbolatss.tagsnews.screen.board.BoardView;
import workshop.akbolatss.tagsnews.screen.sources.SourcesView;

/**
 * Created by AkbolatSS on 17.08.2017.
 */

@Module
public class BoardModule {

    private BoardView mView;

    public BoardModule(BoardView mView) {
        this.mView = mView;
    }

    @ActivityScope
    @Provides
    BoardView provideView() {
        return mView;
    }

    @ActivityScope
    @Provides
    DBRssSourceRepository provideDbRssSourceRepository(DaoSession daoSession, NewsApiService newsApiService) {
        return new DBRssSourceRepository(daoSession, newsApiService);
    }
}
