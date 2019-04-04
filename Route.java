package printerserver;

import java.util.TreeMap;

public class Route {
    
    private String route;
    private String[] keys;
    private Handler handler;
    private Parameter[] parameters;
    private TreeMap<String, String> params;

    public Route(String route, String[] keys, Handler handler, Parameter[] parameters) {
        this.keys = keys;
        this.route = route;
        this.handler = handler;
        this.parameters = parameters;
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

    public Parameter[] getParameters() {
        return parameters;
    }

    public Route setParameters(Parameter[] parameters) {
        this.parameters = parameters;
        return this;
    }

    public TreeMap<String, String> getParams() {
        return params;
    }

    public Route setParams(TreeMap<String, String> params) {
        this.params = params;
        return this;
    }
    
    public boolean inParameters(Parameter parameter){
        for(Parameter p : parameters){
            if(p.equals(parameter)) return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "route: "+route+"\nparams: "+params;
    }
    
}
