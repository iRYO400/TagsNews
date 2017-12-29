package workshop.akbolatss.tagsnews.screen.sources;

import java.util.List;

import workshop.akbolatss.tagsnews.base.BaseView;
import workshop.akbolatss.tagsnews.base.ErrorView;
import workshop.akbolatss.tagsnews.base.LoadingView;
import workshop.akbolatss.tagsnews.repositories.source.RssSource;

public interface SourcesView extends BaseView, LoadingView, ErrorView {

    void onLoadSources(List<RssSource> rssSourceList);

    void onAddNewSource();
}
