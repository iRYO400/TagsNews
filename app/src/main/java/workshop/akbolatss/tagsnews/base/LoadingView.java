package workshop.akbolatss.tagsnews.base;

public interface LoadingView {

    void onShowLoading();

    void onHideLoading();

    void onNoContent(boolean isEmpty);
}
