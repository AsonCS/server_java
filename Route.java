package javaapplication1;

import java.util.TreeMap;

public class Route {
    
    private String route;
    private Handler handler;
    private Parameter[] parameters;
    private TreeMap<String, String> params;

    public Route(String route, Handler handler, Parameter[] parameters, TreeMap<String, String> params) {
        this.route = route;
        this.handler = handler;
        this.parameters = parameters;
        this.params = params;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public void setParameters(Parameter[] parameters) {
        this.parameters = parameters;
    }

    public TreeMap<String, String> getParams() {
        return params;
    }

    public void setParams(TreeMap<String, String> params) {
        this.params = params;
    }
    
    @Override
    public String toString() {
        return "route: "+route+"\nparams: "+params;
    }
    
}
