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
        TEXT_ZPL
    }
    
    public enum Encoding{
        UTF_8
    }
    
    public enum Status {
        OK,
        NOT_FOUND
    }
    
    public static Method getMethod(String string){
        switch(string){
            case POST:
                return Method.POST;
            case GET:
            default:
                return Method.GET;
        }
    }
    
    public static String getContentType(ContentType type){
        switch(type){
            case TEXT_HTML:
                return "text/html";                
            case TEXT_ZPL:
                return "text/zpl";
            case TEXT_PLAIN:
            default:
                return "text/plain";
        }
    }
    
    public static String getEncoding(Encoding encoding){
        switch(encoding){
            case UTF_8:
            default:
                return "UTF-8";
        }
    }
    
    public static String getStatus(Status status){
        switch(status){
            case OK:
                return "200 OK";
            case NOT_FOUND:
            default:
                return "404 NOT FOUND";
            
        }
    }
    
}
