package workshop.akbolatss.tagsnews.di.component

import dagger.Component
import workshop.akbolatss.tagsnews.di.module.BoardModule
import workshop.akbolatss.tagsnews.di.scope.ActivityScope
import workshop.akbolatss.tagsnews.screen.board.BoardActivity

/**
 * Dagger2 Component with specific scope for BoardActivity
 * @see BoardModule
 * @see BoardActivity
 */
@ActivityScope
@Component(dependencies = [(AppComponent::class)], modules = [(BoardModule::class)])
interface BoardComponent {

    fun inject(activity: BoardActivity)
}
