package workshop.akbolatss.tagsnews.di.module

import dagger.Module
import dagger.Provides
import workshop.akbolatss.tagsnews.di.scope.ActivityScope
import workshop.akbolatss.tagsnews.model.DBReminderItemRepository
import workshop.akbolatss.tagsnews.model.DBRssSourceRepository
import workshop.akbolatss.tagsnews.model.dao.DaoSession
import workshop.akbolatss.tagsnews.screen.splash.SplashView

@Module
class SplashModule(private val mView: SplashView) {

    @ActivityScope
    @Provides
    internal fun provideView(): SplashView {
        return mView
    }

    @ActivityScope
    @Provides
    internal fun provideDbRssSourceRepository(daoSession: DaoSession): DBRssSourceRepository {
        return DBRssSourceRepository(daoSession)
    }

    @ActivityScope
    @Provides
    internal fun provideDbRemindersRepository(daoSession: DaoSession): DBReminderItemRepository {
        return DBReminderItemRepository(daoSession)
    }
}

//    private SplashView mView;
//
//    public SplashModule(SplashView mView) {
//        this.mView = mView;
//    }
//
//    @ActivityScope
//    @Provides
//    SplashView provideView() {
//        return mView;
//    }
//
//    @ActivityScope
//    @Provides
//    DBRssSourceRepository provideDbRssSourceRepository(DaoSession daoSession){
//        return new DBRssSourceRepository(daoSession);
//    }
//
//    @ActivityScope
//    @Provides
//    DBReminderItemRepository provideDbRemindersRepository(DaoSession daoSession){
//        return new DBReminderItemRepository(daoSession);
//    }