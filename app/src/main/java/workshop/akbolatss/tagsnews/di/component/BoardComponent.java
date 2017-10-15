package workshop.akbolatss.tagsnews.di.component;

import dagger.Component;
import workshop.akbolatss.tagsnews.di.module.BoardModule;
import workshop.akbolatss.tagsnews.di.module.DetailsModule;
import workshop.akbolatss.tagsnews.di.scope.ActivityScope;
import workshop.akbolatss.tagsnews.screen.board.BoardActivity;


@ActivityScope
@Component(dependencies = AppComponent.class, modules = {BoardModule.class, DetailsModule.class})
public interface BoardComponent {

    void inject(BoardActivity activity);
}
