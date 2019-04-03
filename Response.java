package printerserver;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class Response {
    
    private String body;

    public Response(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    
    public byte[] getBytes() throws UnsupportedEncodingException{
        String headerResponse = "HTTP/1.1 200 OK\r\n"
                + "Server: Anderson : 1.0\r\n"
                + "Date: " + new Date().toString() + "\r\n"
                + "Content-length: " + body.getBytes("UTF-8").length + "\r\n"
                + "Content-type: text/html\r\n"
                + "\r\n";
        return headerResponse.concat(body).getBytes("UTF-8");
    }
    
}
