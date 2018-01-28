package workshop.akbolatss.tagsnews.di.module;

import dagger.Module;
import dagger.Provides;
import workshop.akbolatss.tagsnews.di.scope.ActivityScope;
import workshop.akbolatss.tagsnews.model.DBReminderItemRepository;
import workshop.akbolatss.tagsnews.model.DBRssSourceRepository;
import workshop.akbolatss.tagsnews.model.dao.DaoSession;
import workshop.akbolatss.tagsnews.screen.splash.SplashView;

@Module
public class SplashModule {

    private SplashView mView;

    public SplashModule(SplashView mView) {
        this.mView = mView;
    }

    @ActivityScope
    @Provides
    SplashView provideView() {
        return mView;
    }

    @ActivityScope
    @Provides
    DBRssSourceRepository provideDbRssSourceRepository(DaoSession daoSession){
        return new DBRssSourceRepository(daoSession);
    }

    @ActivityScope
    @Provides
    DBReminderItemRepository provideDbRemindersRepository(DaoSession daoSession){
        return new DBReminderItemRepository(daoSession);
    }
}
