package workshop.akbolatss.tagsnews.di.component;

import dagger.Component;
import workshop.akbolatss.tagsnews.di.module.FavoritesModule;
import workshop.akbolatss.tagsnews.di.scope.ActivityScope;
import workshop.akbolatss.tagsnews.screen.favorites.FavoritesActivity;

/**
 * Created by AkbolatSS on 14.08.2017.
 */

@ActivityScope
@Component(dependencies = AppComponent.class, modules = FavoritesModule.class)
public interface FavoritesComponent {

    void inject(FavoritesActivity activity);
}
