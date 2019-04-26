package printerserver.server;

import java.util.TreeMap;
import printerserver.server.Parameter.*;

public class Route implements Comparable<Route>{
    
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
