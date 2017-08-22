package workshop.akbolatss.tagsnews.di.component;

import dagger.Component;
import workshop.akbolatss.tagsnews.di.module.BoardModule;
import workshop.akbolatss.tagsnews.di.module.SourcesModule;
import workshop.akbolatss.tagsnews.di.scope.ActivityScope;
import workshop.akbolatss.tagsnews.screen.board.BoardActivity;
import workshop.akbolatss.tagsnews.screen.sources.SourcesActivity;

/**
 * Created by AkbolatSS on 17.08.2017.
 */

@ActivityScope
@Component(dependencies = AppComponent.class, modules = BoardModule.class)
public interface BoardComponent {

    void inject(BoardActivity activity);
}
