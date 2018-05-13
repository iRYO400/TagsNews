package workshop.akbolatss.tagsnews.di.component

import dagger.Component
import workshop.akbolatss.tagsnews.di.module.SplashModule
import workshop.akbolatss.tagsnews.di.scope.ActivityScope
import workshop.akbolatss.tagsnews.screen.splash.SplashActivity

/**
 * Dagger2 Component with specific scope for SplashActivity
 * @see SplashModule
 * @see SplashActivity
 */
@ActivityScope
@Component(dependencies = [(AppComponent::class)], modules = [(SplashModule::class)])
interface SplashComponent {

    fun inject(activity: SplashActivity)
}
