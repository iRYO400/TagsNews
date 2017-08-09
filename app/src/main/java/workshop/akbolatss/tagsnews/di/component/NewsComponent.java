package workshop.akbolatss.tagsnews.di.component;

import dagger.Component;
import workshop.akbolatss.tagsnews.di.module.NewsListModule;
import workshop.akbolatss.tagsnews.di.scope.ActivityScope;
import workshop.akbolatss.tagsnews.screen.news.NewsActivity;

/**
 * Created by AkbolatSS on 08.08.2017.
 */

@ActivityScope
@Component(modules = NewsListModule.class, dependencies = AppComponent.class)
public interface NewsComponent {

    void inject(NewsActivity activity);


}
