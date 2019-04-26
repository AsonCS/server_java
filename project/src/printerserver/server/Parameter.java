package printerserver.server;

public class Parameter {
    
    public static final String GET = "GET";
    public static final String POST = "POST";
    
    public enum Method{
        GET,
        POST
    }
    
    public enum ContentType{
        TEXT_HTML,
        TEXT_PLAIN,
        TEXT_ZPL,
        APPLICATION_JSON,
        TEXT_CSS,
        TEXT_JAVASCRIPT,
        IMAGE_ICO,
        IMAGE_PNG,
        IMAGE_SVG
    }
    
    public enum Encoding{
        UTF_8
    }
    
    public enum Status {
        OK,
        NOT_FOUND
    }
    
    public static Method getMethod(String string){
        Method method;
        switch(string){
            case POST:
                method = Method.POST;
                break;
            case GET:
            default:
                method = Method.GET;
        }
        return method;
    }
    
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
            case "ico":
                type = ContentType.IMAGE_ICO;
                break;
            case "png":
                type = ContentType.IMAGE_PNG;
                break;
            case "svg":
                type = ContentType.IMAGE_SVG;
                break;
            case "text/plain":
            default:
                type = ContentType.TEXT_PLAIN;
        }
        return type;
    }
    
    public static String getEncoding(Encoding encoding){
        String encod;
        switch(encoding){
            case UTF_8:
            default:
                encod = "UTF-8";
        }
        return encod;
    }
    
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
