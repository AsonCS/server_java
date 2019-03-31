package javaapplication1;

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
        String httpResponse = "<h1 style=\"color: red\">Olá mundo!!</h1>";
        String headerResponse = "HTTP/1.1 200 OK\r\n"
                + "Server: Anderson : 1.0\r\n"
                + "Date: " + new Date().toString() + "\r\n"
                + "Content-length: " + httpResponse.getBytes("UTF-8").length + "\r\n"
                + "Content-type: text/html\r\n"
                + "\r\n";
        return headerResponse.concat(httpResponse).getBytes("UTF-8");
    }
    
    
    
}
