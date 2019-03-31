/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.util.TreeMap;

/**
 *
 * @author anderson
 */
public class Request {
    
    private String body;
    private String method;
    private TreeMap<String, String> params;

    public Request(String body, String method, TreeMap<String, String> params) {
        this.body = body;
        this.method = method;
        this.params = params;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public TreeMap<String, String> getParams() {
        return params;
    }

    public void setParams(TreeMap<String, String> params) {
        this.params = params;
    }
    
    
    
}
