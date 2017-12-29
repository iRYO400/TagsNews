package workshop.akbolatss.tagsnews.di.component;

import dagger.Component;
import workshop.akbolatss.tagsnews.di.module.SourcesModule;
import workshop.akbolatss.tagsnews.di.scope.ActivityScope;
import workshop.akbolatss.tagsnews.screen.recommendations.RecommendationsFragment;
import workshop.akbolatss.tagsnews.screen.sources.SourcesActivity;


@ActivityScope
@Component(dependencies = AppComponent.class, modules = SourcesModule.class)
public interface SourcesComponent {

    void inject(SourcesActivity activity);

    void inject(RecommendationsFragment fragment);
}
