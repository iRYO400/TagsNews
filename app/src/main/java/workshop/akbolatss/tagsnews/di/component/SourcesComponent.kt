package workshop.akbolatss.tagsnews.di.component

import dagger.Component
import workshop.akbolatss.tagsnews.di.module.SourcesModule
import workshop.akbolatss.tagsnews.di.scope.ActivityScope
import workshop.akbolatss.tagsnews.screen.recommendations.RecommendationsFragment
import workshop.akbolatss.tagsnews.screen.sources.SourcesActivity

/**
 * Dagger2 Component with specific scope for SourcesActivity, RecommendationsFragment
 * @see SourcesModule
 * @see SourcesActivity
 * @see RecommendationsFragment
 */
@ActivityScope
@Component(dependencies = [(AppComponent::class)], modules = [(SourcesModule::class)])
interface SourcesComponent {

    fun inject(activity: SourcesActivity)

    fun inject(fragment: RecommendationsFragment)
}
