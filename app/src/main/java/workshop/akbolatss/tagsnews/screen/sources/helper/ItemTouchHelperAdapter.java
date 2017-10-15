package workshop.akbolatss.tagsnews.screen.sources.helper;

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemsMoved(int fromPosition, int toPosition);
}
