package workshop.akbolatss.tagsnews.di.component

import dagger.Component
import workshop.akbolatss.tagsnews.di.module.DetailsModule
import workshop.akbolatss.tagsnews.di.scope.ActivityScope
import workshop.akbolatss.tagsnews.screen.details.DetailsActivity
import workshop.akbolatss.tagsnews.screen.details.DetailsFragment

/**
 * Dagger2 Component with specific scope for DetailsFragment
 * @see DetailsModule
 * @see DetailsFragment
 */
@ActivityScope
@Component(dependencies = [(AppComponent::class)], modules = [(DetailsModule::class)])
interface DetailsComponent {

    fun inject(fragment: DetailsFragment)
}
