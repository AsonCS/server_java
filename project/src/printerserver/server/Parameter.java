package printerserver.server;

/**
 * Object for centralize parameters of server.
 * This class turn easy the use of <b><i>Content type</i></b>, <b><i>Method</i></b>,
 * <b><i>Encoding</i></b> and <b><i>Status</i></b>.
 *
 * @author Anderson Costa
 * @version 1.0
 *
 * @see <a href="https://github.com/AsonCS" target="_blank">My repository on GitHub</a>
 */
public class Parameter {
    
    /**
     * Constant for Method <b>GET</b>.
     */
    public static final String GET = "GET";
    
    /**
     * Constant for Method <b>POST</b>.
     */
    public static final String POST = "POST";
    
    /**
     * Object Enum with <b><u>Methods</u></b> types.
     */
    public enum Method{

        /**
         * Method: <b>GET</b>.
         */
        GET,

        /**
         * Method: <b>POST</b>.
         */
        POST
    }
    
    /**
     * Object Enum with <b><u>Content Types</u></b>.
     */
    public enum ContentType{

        /**
         * Content Type: <b>text/html</b>.<br>
         * Indentify also: <u>html</u>.
         */
        TEXT_HTML,

        /**
         * Content Type: <b>text/plain</b>.
         */
        TEXT_PLAIN,

        /**
         * Content Type: <b>text/zpl</b>.
         */
        TEXT_ZPL,

        /**
         * Content Type: <b>application/json</b>.<br>
         * Indentify also: <u>json</u>.
         */
        APPLICATION_JSON,

        /**
         * Content Type: <b>text/css</b>.<br>
         * Indentify also: <u>css</u>.
         */
        TEXT_CSS,

        /**
         * Content Type: <b>application/javascript</b>.<br>
         * Indentify also: <u>text/javascript, javascript, js</u>.
         */
        TEXT_JAVASCRIPT,

        /**
         * Content Type: <b>image/ico</b>.<br>
         * Indentify also: <u>ico</u>.
         */
        IMAGE_ICO,

        /**
         * Content Type: <b>image/png</b>.<br>
         * Indentify also: <u>png</u>.
         */
        IMAGE_PNG,

        /**
         * Content Type: <b>image/svg+xml</b>.<br>
         * Indentify also: <u>svg</u>.
         */
        IMAGE_SVG
    }
        
    /**
     * Object Enum with <b><u>Encoding</u></b> types.
     */
    public enum Encoding{

        /**
         * Encoding: <b>UTF-8</b>.
         */
        UTF_8
    }
        
    /**
     * Object Enum with <b><u>Status</u></b> types.
     */
    public enum Status {

        /**
         * Status: <b>200 OK</b>.
         */
        OK,

        /**
         * Status: <b>404 NOT FOUND</b>.
         */
        NOT_FOUND
    }
    
    /**
     * Identify <b>Method</b> type for give parameter String.
     * 
     * @param string String with method.
     * @return Object enum with <b>Method</b> type.
     */
    public static Method getMethod(String string){
        Method method;
        switch(string.toUpperCase()){
            case POST:
                method = Method.POST;
                break;
            case GET:
            default:
                method = Method.GET;
        }
        return method;
    }
        
    /**
     * Identify text content-type for give parameter <b>Content Type</b>.
     * 
     * @param contentType Enum <b>Content Type</b> of content-type.
     * @return Text representation of content-type.
     */
    public static String getContentType(ContentType contentType){
        String type;
        switch(contentType){
            case TEXT_HTML:
                type = "text/html"; 
                break;               
            case TEXT_ZPL:
                type = "text/zpl";  
                break;              
            case APPLICATION_JSON:
                type = "application/json";
                break;       
            case TEXT_CSS:
                type = "text/css";
                break;       
            case TEXT_JAVASCRIPT:
                type = "application/javascript";
                break;      
            case IMAGE_ICO:
                type = "image/ico";
                break;      
            case IMAGE_PNG:
                type = "image/png";
                break;      
            case IMAGE_SVG:
                type = "image/svg+xml";
                break;
            case TEXT_PLAIN:
            default:
                type = "text/plain";
        }
        return type;
    }
    
    /**
     * Identify <b>Content Type</b> for give parameter String.
     * 
     * @param contentType String with content-type.
     * @return Object enum with <b>Content Type</b>.
     */
    public static ContentType indentifyContentType(String contentType){
        ContentType type;
        switch(contentType){
            case "text/html":
            case "html":
                type = ContentType.TEXT_HTML;
                break;
            case "text/zpl":                 
            case "zpl":
                type = ContentType.TEXT_ZPL; 
                break;               
            case "application/json":     
            case "json":
                type = ContentType.APPLICATION_JSON; 
                break;     
            case "text/css":
            case "css":
                type = ContentType.TEXT_CSS; 
                break;     
            case "application/javascript":
            case "text/javascript":
            case "javascript":
            case "js":
                type = ContentType.TEXT_JAVASCRIPT;
                break;
            case "image/ico":
            case "ico":
                type = ContentType.IMAGE_ICO;
                break;
            case "image/png":
            case "png":
                type = ContentType.IMAGE_PNG;
                break;
            case "image/svg+xml":
            case "svg":
                type = ContentType.IMAGE_SVG;
                break;
            case "text/plain":
            default:
                type = ContentType.TEXT_PLAIN;
        }
        return type;
    }
            
    /**
     * Identify text encoding for give parameter <b>Encoding</b>.
     * 
     * @param encoding Enum <b>Encoding</b> of encoding.
     * @return Text representation of encoding.
     */
    public static String getEncoding(Encoding encoding){
        String encod;
        switch(encoding){
            case UTF_8:
            default:
                encod = "UTF-8";
        }
        return encod;
    }
            
    /**
     * Identify text status for give parameter <b>Status</b>.
     * 
     * @param status Enum <b>Status</b> of status.
     * @return Text representation of status.
     */
    public static String getStatus(Status status){
        String sts;
        switch(status){
            case OK:
                sts = "200 OK";
                break;
            case NOT_FOUND:
            default:
                sts = "404 NOT FOUND";            
        }
        return sts;
    }
                
    /**
     * Identify if this type is binary, for diferent write in Stream.
     * 
     * @param type Enum <b>Content Type</b> of content-type.
     * @return If this type is binary returns <b>True</b> or <b>False</b> otherwise.
     */
    public static boolean isImage(ContentType type){
        boolean resp;
        switch (type) {
            case IMAGE_ICO:
            case IMAGE_PNG:
                resp = true;
                break;
            default:
                resp = false;
        }
        return resp;
    }
    
}
