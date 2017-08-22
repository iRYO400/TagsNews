package workshop.akbolatss.tagsnews.screen.sources;

import java.util.List;

import workshop.akbolatss.tagsnews.base.BaseView;
import workshop.akbolatss.tagsnews.repositories.source.RssSource;

/**
 * Created by AkbolatSS on 17.08.2017.
 */

public interface SourcesView extends BaseView {

    void onAddNewSource();

    void onLoadSources(List<RssSource> rssSourceList);

    void onUpdate();

    void onShowLoading();

    void onHideLoading();
}
