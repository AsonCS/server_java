package printerserver.server;

import java.util.TreeMap;
import printerserver.server.Parameter.*;

public class Route {
    
    private String route;
    private String[] keys;
    private Handler handler;
    private Method method;
    private TreeMap<String, String> params;

    public Route(String route, String[] keys, Handler handler, Method method) {
        this.keys = keys;
        this.route = route;
        this.handler = handler;
        this.method = method;
    }

    public String[] getKeys() {
        return keys;
    }

    public Route setKeys(String[] keys) {
        this.keys = keys;
        return this;
    }

    public String getRoute() {
        return route;
    }

    public Route setRoute(String route) {
        this.route = route;
        return this;
    }

    public Handler getHandler() {
        return handler;
    }

    public Route setHandler(Handler handler) {
        this.handler = handler;
        return this;
    }

    public Method getMethod() {
        return method;
    }

    public Route setMethod(Method method) {
        this.method = method;
        return this;
    }
    
    public TreeMap<String, String> getParams() {
        return params;
    }

    public Route setParams(TreeMap<String, String> params) {
        this.params = params;
        return this;
    }
    
    @Override
    public String toString() {
        return "route: "+route+"\nparams: "+params;
    }
    
}
