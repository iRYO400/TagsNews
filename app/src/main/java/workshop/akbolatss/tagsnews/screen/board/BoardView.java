package workshop.akbolatss.tagsnews.screen.board;

import java.util.List;

import workshop.akbolatss.tagsnews.base.BaseView;
import workshop.akbolatss.tagsnews.model.dao.RssSource;


public interface BoardView extends BaseView {

    void onInitSources(List<RssSource> rssSources);

    void onUpdateSources(List<RssSource> rssSources);
}
