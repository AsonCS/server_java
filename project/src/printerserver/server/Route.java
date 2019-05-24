package printerserver.server;

import java.util.TreeMap;
import printerserver.server.Parameter.*;

/**
 * Object with route attributes.
 * 
 * This object turn easy creation a handler for client request, 
 * with parameters for that.
 *
 * @author Anderson Costa
 * @version 1.0
 *
 * @see <a href="https://github.com/AsonCS/server_java" target="_blank">Repository on GitHub</a>
 */
public class Route implements Comparable<Route>{
    
    private String route;
    private String[] keys;
    private Handler handler;
    private Method method;
    private TreeMap<String, String> params;

    /**
     * Constructor with every parameters for treat client request.
     *
     * @param route Route string which identifies <i>url</i> requested.
     * @param keys Keys at route to build key-value pairs.
     * @param handler {@link Handler} for treat this request route.
     * @param method {@link Method} <i>HTTP</i> of request route.
     */
    public Route(String route, String[] keys, Handler handler, Method method) {
        this.keys = keys;
        this.route = route;
        this.handler = handler;
        this.method = method;
    }

    /**
     * Gets the current keys.
     *
     * @return Current keys.
     */
    public String[] getKeys() {
        return keys;
    }

    /**
     * Alters the current keys.
     *
     * @param keys The keys for replaced the current keys.
     * @return {@link Route}.
     */
    public Route setKeys(String[] keys) {
        this.keys = keys;
        return this;
    }

    /**
     * Gets the current route.
     *
     * @return Current route.
     */
    public String getRoute() {
        return route;
    }

    /**
     * Alters the current route.
     *
     * @param route The route for replaced the current route.
     * @return {@link Route}.
     */
    public Route setRoute(String route) {
        this.route = route;
        return this;
    }

    /**
     * Gets the current {@link Handler}.
     *
     * @return Current {@link Handler}.
     */
    public Handler getHandler() {
        return handler;
    }

    /**
     * Alters the current {@link Handler}.
     *
     * @param handler The {@link Handler} for replaced the current {@link Handler}.
     * @return {@link Route}.
     */
    public Route setHandler(Handler handler) {
        this.handler = handler;
        return this;
    }

    /**
     * Gets the current {@link Method}.
     *
     * @return Current {@link Method}.
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Alters the current {@link Method}.
     *
     * @param method The {@link Method} for replaced the current {@link Method}.
     * @return {@link Route}.
     */
    public Route setMethod(Method method) {
        this.method = method;
        return this;
    }
    
    /**
     * Gets the current {@link TreeMap}.
     *
     * @return Current {@link TreeMap}.
     */
    public TreeMap<String, String> getParams() {
        return params;
    }

    /**
     * Alters the current {@link TreeMap}.
     *
     * @param params The {@link TreeMap} for replaced the current {@link TreeMap}.
     * @return {@link Route}.
     */
    public Route setParams(TreeMap<String, String> params) {
        this.params = params;
        return this;
    }
    
    @Override
    public String toString() {
        return "route: "+route+"\nparams: "+params;
    }

    @Override
    public int compareTo(Route o) {
        if(o.getRoute().equals("/(.)+")) return -1;
        if(getRoute().equals("/(.)+")) return 1;
        String[] a = getRoute().split("/");
        String[] b = o.getRoute().split("/");
        if(a.length == b.length){
            int c = 0;
            int d = 0;
            for(String e : a){
                if(e.substring(0).equals("(")) c++;
            }
            for(String e : b){
                if(e.substring(0).equals("(")) d++;
            }
            return c > d ? 1 : -1;
        }else{
            return a.length > b.length ? 1 : -1;
        }
    }
    
}
