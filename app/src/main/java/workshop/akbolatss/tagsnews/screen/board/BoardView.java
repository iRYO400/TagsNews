package workshop.akbolatss.tagsnews.screen.board;

import java.util.List;

import workshop.akbolatss.tagsnews.base.BaseView;
import workshop.akbolatss.tagsnews.repositories.source.RssSource;

/**
 * Created by AkbolatSS on 22.08.2017.
 */

public interface BoardView extends BaseView {

    void onInitSources(List<RssSource> rssSources);
}
