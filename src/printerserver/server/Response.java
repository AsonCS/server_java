package printerserver.server;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import printerserver.server.Parameter.*;

public class Response {
    
    private String body;
    private ContentType contentType;
    private Encoding encoding;
    private Status status;

    public Response() {
        body = "";
        contentType = ContentType.TEXT_PLAIN;
        encoding = Encoding.UTF_8;
        status = Status.OK;
    }

    public String getBody() {
        return body;
    }

    public Response setBody(String body) {
        this.body = body;
        return this;
    }

    public Response putBody(String body) {
        this.body += body;
        return this;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public Response setContentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    public Encoding getEncoding() {
        return encoding;
    }

    public Response setEncoding(Encoding encoding) {
        this.encoding = encoding;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public Response setStatus(Status status) {
        this.status = status;
        return this;
    }
    
    public Response readTemplate(String file){
        
        return this;
    }
    
    
    public byte[] getBytes() throws UnsupportedEncodingException{
        String headerResponse = "HTTP/1.1 ";
        headerResponse += Parameter.getStatus(status);
        headerResponse += "\r\nServer: Anderson : 1.0";
        headerResponse += "\r\nDate: " + new Date().toString();
        headerResponse += "\r\nContent-length: " + body.getBytes(Parameter.getEncoding(encoding)).length;
        headerResponse += "\r\nContent-type: " + Parameter.getContentType(contentType) + ";charset=" + Parameter.getEncoding(encoding);
        headerResponse += "\r\n\r\n";
        return headerResponse.concat(body).getBytes(Parameter.getEncoding(encoding));
    }
    
}
