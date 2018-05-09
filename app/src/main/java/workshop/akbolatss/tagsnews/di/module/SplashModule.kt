package workshop.akbolatss.tagsnews.di.module

import dagger.Module
import dagger.Provides
import workshop.akbolatss.tagsnews.di.scope.ActivityScope
import workshop.akbolatss.tagsnews.model.DBReminderItemRepository
import workshop.akbolatss.tagsnews.model.DBRssSourceRepository
import workshop.akbolatss.tagsnews.model.dao.DaoSession
import workshop.akbolatss.tagsnews.screen.splash.SplashView

/**
 * Dagger2 module for SplashComponent
 * @see workshop.akbolatss.tagsnews.di.component.SplashComponent
 */
@Module
class SplashModule(private val mView: SplashView) {

    /**
     * MVP View Provider
     * @see SplashView
     */
    @ActivityScope
    @Provides
    internal fun provideView(): SplashView {
        return mView
    }

    /**
     * DB Repository provider for Rss sources
     * @see DBRssSourceRepository
     */
    @ActivityScope
    @Provides
    internal fun provideDbRssSourceRepository(daoSession: DaoSession): DBRssSourceRepository {
        return DBRssSourceRepository(daoSession)
    }

    /**
     * DB Repository provider for Reminder
     * @see DBReminderItemRepository
     */
    @ActivityScope
    @Provides
    internal fun provideDbRemindersRepository(daoSession: DaoSession): DBReminderItemRepository {
        return DBReminderItemRepository(daoSession)
    }
}