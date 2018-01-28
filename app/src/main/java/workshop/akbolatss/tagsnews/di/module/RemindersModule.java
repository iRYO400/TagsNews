package workshop.akbolatss.tagsnews.di.module;

import dagger.Module;
import dagger.Provides;
import workshop.akbolatss.tagsnews.api.NewsApiService;
import workshop.akbolatss.tagsnews.di.scope.ActivityScope;
import workshop.akbolatss.tagsnews.model.DBReminderItemRepository;
import workshop.akbolatss.tagsnews.model.DBRssSourceRepository;
import workshop.akbolatss.tagsnews.model.dao.DaoSession;
import workshop.akbolatss.tagsnews.screen.reminders.RemindersView;

@Module
public class RemindersModule {
    private RemindersView mView;

    public RemindersModule() {
    }

    public RemindersModule(RemindersView mView) {
        this.mView = mView;
    }

    @ActivityScope
    @Provides
    public RemindersView getmView() {
        return mView;
    }

    @ActivityScope
    @Provides
    DBReminderItemRepository provideDBReminderItemRepository(DaoSession daoSession) {
        return new DBReminderItemRepository(daoSession);
    }

    @ActivityScope
    @Provides
    DBRssSourceRepository provideDbRssSourceRepository(DaoSession daoSession, NewsApiService newsApiService) {
        return new DBRssSourceRepository(daoSession, newsApiService);
    }
}
