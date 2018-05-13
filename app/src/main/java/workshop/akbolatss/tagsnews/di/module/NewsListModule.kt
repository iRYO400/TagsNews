package workshop.akbolatss.tagsnews.di.module

import dagger.Module
import dagger.Provides
import workshop.akbolatss.tagsnews.di.scope.ActivityScope
import workshop.akbolatss.tagsnews.screen.news.NewsView

/**
 * Dagger2 module for NewsComponent
 * @see workshop.akbolatss.tagsnews.di.component.NewsComponent
 */
@Module
class NewsListModule(private val mView: NewsView) {

    /**
     * MVP View Provider
     * @see NewsView
     */
    @ActivityScope
    @Provides
    internal fun provideView(): NewsView {
        return mView
    }
}
