package printerserver.server;

import java.util.TreeMap;
import printerserver.server.Parameter.*;

/**
 * Object to treat client connection info.
 * 
 * This object manage client connection info for use at method which treat url route.
 *
 * @author Anderson Costa
 * @version 2019/01
 *
 * @see <a href="https://github.com/AsonCS/server_java" target="_blank">Repository on GitHub</a>
 */
public class Request {
    
    private String body;
    private String header;
    private Method method;
    private ContentType contentType;
    private TreeMap<String, String> params;

    /**
     * Constructor with client connection info as parameters.
     *
     * @param header HTTP headers.
     * @param body HTTP body.
     * @param method HTTP {@link Method}.
     * @param params {@link TreeMap} with key-value parameters of HTTP request.
     */
    public Request(String header, String body, Method method, TreeMap<String, String> params) {
        this.body = body;
        this.header = header;
        this.method = method;
        this.params = params;
        identifyContentType();
    }

    /**
     * Gets the current header.
     *
     * @return Current header.
     */
    public String getHeader() {
        return header;
    }

    /**
     * Alters the current header.
     *
     * @param header The header for replaced the current header.
     * @return {@link Request}.
     */
    public Request setHeader(String header) {
        this.header = header;
        return this;
    }

    /**
     * Alters the current body.
     *
     * @return Current body.
     */
    public String getBody() {
        return body;
    }

    /**
     * Alters the current body.
     *
     * @param body The body for replaced the current body.
     * @return {@link Request}.
     */
    public Request setBody(String body) {
        this.body = body;
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
     * @return {@link Request}.
     */
    public Request setMethod(Method method) {
        this.method = method;
        return this;
    }

    /**
     * Gets the current {@link TreeMap} params.
     *
     * @return Current {@link TreeMap} params.
     */
    public TreeMap<String, String> getParams() {
        return params;
    }

    /**
     * Alters the current params.
     *
     * @param params The {@link TreeMap} params for replaced the current params.
     * @return {@link Request}.
     */
    public Request setParams(TreeMap<String, String> params) {
        this.params = params;
        return this;
    }

    /**
     * Gets the current {@link ContentType}.
     *
     * @return Current {@link ContentType}.
     */
    public ContentType getContentType() {
        return contentType;
    }

    /**
     * Alters the current {@link ContentType}.
     *
     * @param contentType The {@link ContentType} for replaced the current {@link ContentType}.
     * @return {@link Request}.
     */
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
    
    /**
     * Method make use of {@link QueryTransform#getKeyValue(java.lang.String, java.util.TreeMap)} 
     * for process body key-value parameters.
     *
     * @return {@link Request}.
     */
    public Request processKeyValue(){
        params = QueryTransform.getKeyValue(getBody().replace("+", "%20"), params);
        return this;
    }
    
}
