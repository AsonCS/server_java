package printerserver.server;

import java.util.TreeMap;
import printerserver.server.Parameter.*;

/**
 *
 * @author anderson
 */
public class Request {
    
    private String body;
    private Method method;
    private TreeMap<String, String> params;

    public Request(String body, Method method, TreeMap<String, String> params) {
        this.body = body;
        this.method = method;
        this.params = params;
    }

    public String getBody() {
        return body;
    }

    public Request setBody(String body) {
        this.body = body;
        return this;
    }

    public Method getMethod() {
        return method;
    }

    public Request setMethod(Method method) {
        this.method = method;
        return this;
    }

    public TreeMap<String, String> getParams() {
        return params;
    }

    public Request setParams(TreeMap<String, String> params) {
        this.params = params;
        return this;
    }
    
    
    
}
