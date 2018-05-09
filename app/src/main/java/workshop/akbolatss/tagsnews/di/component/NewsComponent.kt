package workshop.akbolatss.tagsnews.di.component

import dagger.Component
import workshop.akbolatss.tagsnews.di.module.NewsListModule
import workshop.akbolatss.tagsnews.di.scope.ActivityScope
import workshop.akbolatss.tagsnews.screen.news.NewsFragment

/**
 * Dagger2 Component with specific scope for NewsFragment
 * @see NewsListModule
 * @see NewsFragment
 */
@ActivityScope
@Component(dependencies = [(AppComponent::class)], modules = [(NewsListModule::class)])
interface NewsComponent {

    fun inject(fragment: NewsFragment)

}
