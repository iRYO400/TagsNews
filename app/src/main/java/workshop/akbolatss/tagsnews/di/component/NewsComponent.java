package workshop.akbolatss.tagsnews.di.component;

import dagger.Component;
import workshop.akbolatss.tagsnews.di.module.NewsListModule;
import workshop.akbolatss.tagsnews.di.scope.ActivityScope;
import workshop.akbolatss.tagsnews.screen.board.BoardActivity;
import workshop.akbolatss.tagsnews.screen.news.NewsFragment;

/**
 * Created by AkbolatSS on 08.08.2017.
 */

@ActivityScope
@Component(dependencies = AppComponent.class, modules = NewsListModule.class)
public interface NewsComponent {

    void inject(NewsFragment fragment);
}
