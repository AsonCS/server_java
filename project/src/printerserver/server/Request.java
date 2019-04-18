package printerserver.server;

import java.util.TreeMap;
import printerserver.server.Parameter.*;

/**
 *
 * @author anderson
 */
public class Request {
    
    private String body;
    private String header;
    private Method method;
    private ContentType contentType;
    private TreeMap<String, String> params;

    public Request(String header, String body, Method method, TreeMap<String, String> params) {
        this.body = body;
        this.header = header;
        this.method = method;
        this.params = params;
        identifyContentType();
    }

    public String getHeader() {
        return header;
    }

    public Request setHeader(String header) {
        this.header = header;
        return this;
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

    public ContentType getContentType() {
        return contentType;
    }

    public Request setContentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }
    
    private void identifyContentType(){
        contentType = ContentType.TEXT_PLAIN;
        int i = header.toLowerCase().indexOf("content-type: ") + 14;
        int j = header.toLowerCase().substring(i).indexOf("\n");
        if(i != -1 && header.length() > i + j) contentType = Parameter.indentifyContentType(header.substring(i, i + j).split(";")[0]);
    }
    
    public Request processKeyValue(){
        params = QueryTransform.getKeyValue(getBody().replace("+", "%20"), params);
        return this;
    }
    
}
