package workshop.akbolatss.tagsnews.di.component

import dagger.Component
import workshop.akbolatss.tagsnews.di.module.DetailsModule
import workshop.akbolatss.tagsnews.di.module.FavoritesModule
import workshop.akbolatss.tagsnews.di.scope.ActivityScope
import workshop.akbolatss.tagsnews.screen.favorites.FavoritesActivity

@ActivityScope
@Component(dependencies = [(AppComponent::class)], modules = [(FavoritesModule::class)])
interface FavoritesComponent {

    fun inject(activity: FavoritesActivity)
}
