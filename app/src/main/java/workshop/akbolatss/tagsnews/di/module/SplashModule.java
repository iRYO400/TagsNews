package workshop.akbolatss.tagsnews.di.module;

import dagger.Module;
import dagger.Provides;
import workshop.akbolatss.tagsnews.di.scope.ActivityScope;
import workshop.akbolatss.tagsnews.screen.splash.SplashView;

/**
 * Created by AkbolatSS on 11.08.2017.
 */

@Module
public class SplashModule {

    private SplashView mView;

    public SplashModule(SplashView mView) {
        this.mView = mView;
    }

    @ActivityScope
    @Provides
    SplashView provideView() {
        return mView;
    }
}
