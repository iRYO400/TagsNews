package workshop.akbolatss.tagsnews.base;

/**
 * Author: Akbolat Sadvakassov
 * Date: 27.12.2017
 */

public interface ErrorView {

    public void onUnknownError(String errorMessage);

    public void onTimeout();

    public void onNetworkError();
}
