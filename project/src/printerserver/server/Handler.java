package printerserver.server;

/**
 * Interface to implement callbacks for routes.
 * 
 * This interface implements type of method for treat request routes.
 *
 * @author Anderson Costa
 * @version 2019/01
 *
 * @see <a href="https://github.com/AsonCS/server_java" target="_blank">Repository on GitHub</a>
 */
public interface Handler {

    /**
     * Method to be implemented and which must be triggered when the request is discovered.
     *
     * @param request {@link Request}.
     * @param response {@link Response}.
     * @return {@link Response}.
     */
    Response handler(Request request, Response response);
}
