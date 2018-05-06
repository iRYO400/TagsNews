package workshop.akbolatss.tagsnews.di.module

import dagger.Module
import dagger.Provides
import workshop.akbolatss.tagsnews.di.scope.ActivityScope
import workshop.akbolatss.tagsnews.screen.news.NewsView


@Module
class NewsListModule(private val mView: NewsView) {

    @ActivityScope
    @Provides
    internal fun provideView(): NewsView {
        return mView
    }
}
