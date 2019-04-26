<<<<<<< HEAD
package printerserver.server;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Scanner;
import javax.imageio.ImageIO;
import printerserver.server.Parameter.*;

public class Response {
    
    private String body;
    private ContentType contentType;
    private Encoding encoding;
    private Status status;
    private byte[] bytes;

    public Response() {
        body = "";
        bytes = new byte[0];
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
    
    public Response readFile(String file){
        String[] a = file.split("\\.");
        if(a.length > 0) setContentType(Parameter.indentifyContentType(a[a.length - 1]));
        if(Parameter.isImage(contentType)){
            readImg(file, a[a.length - 1]);
        }else{
            readTextFile(file);
        }
        if(getBody().length() == 0 && bytes.length == 0) return setStatus(Status.NOT_FOUND);
        return this;
    }
    
    public Response readTemplate(String file, String[][] variables){
        readFile(file);
        if(getBody().length() == 0) return this;
        String a = getBody();
        if(variables != null){
            for(String[] b : variables){
                if(b.length != 2) continue;
                a = a.replaceFirst("\\{\\{[\\s]*" + b[0] + "[\\s]*\\}\\}", b[1]);
            }
        }
        a = a.replaceAll("\\{\\{([^\\{\\}])*\\}\\}", "");
        setBody(a);
        return this;
    }
    
    private void readTextFile(String fileName){
        //Debug.info(fileName);
        try{
            Scanner arq = new Scanner(new File(fileName));
            while(arq.hasNextLine()) putBody(arq.nextLine() + "\n");
            arq.close();
        }catch (Exception e){
            setBody("");
            Debug.error(e.getMessage());
        }
    }
    
    private void readImg(String fileName, String type){
        try{
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(ImageIO.read(new File(fileName)), type, outputStream);
            bytes = outputStream.toByteArray();
            outputStream.close();
        }catch (Exception e){
            bytes = new byte[0];
            Debug.error(e.getMessage());
        }
    }
    
    protected byte[] getBytes() throws Exception{
        String encod = (Parameter.isImage(contentType)) ? "" : ";charset=" + Parameter.getEncoding(encoding);
        int length = (Parameter.isImage(contentType)) ? bytes.length : body.length();
        String headerResponse = "HTTP/1.1 ";
        headerResponse += Parameter.getStatus(status);
        headerResponse += "\r\nServer: Anderson : 1.0";
        headerResponse += "\r\nDate: " + new Date().toString();
        headerResponse += "\r\nContent-length: " + length;
        headerResponse += "\r\nContent-type: " + Parameter.getContentType(contentType) + encod;
        headerResponse += "\r\nAccess-Control-Allow-Origin: *";
        headerResponse += "\r\n\r\n";
        if(Parameter.isImage(contentType)){
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(headerResponse.getBytes(Parameter.getEncoding(encoding)));
            outputStream.write(bytes);
            return outputStream.toByteArray();
        }
        return headerResponse.concat(body).getBytes(Parameter.getEncoding(encoding));
    }
    
}
=======
package printerserver.server;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Scanner;
import javax.imageio.ImageIO;
import printerserver.server.Parameter.*;

public class Response {
    
    private String body;
    private ContentType contentType;
    private Encoding encoding;
    private Status status;
    private byte[] bytes;

    public Response() {
        body = "";
        bytes = new byte[0];
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
    
    public Response readFile(String file){
        String[] a = file.split("\\.");
        if(a.length > 0) setContentType(Parameter.indentifyContentType(a[a.length - 1]));
        if(Parameter.isImage(contentType)){
            readImg(file, a[a.length - 1]);
        }else{
            readTextFile(file);
        }
        if(getBody().length() == 0 && bytes.length == 0) return setStatus(Status.NOT_FOUND);
        return this;
    }
    
    public Response readTemplate(String file, String[][] variables){
        readFile(file);
        if(getBody().length() == 0) return this;
        String a = getBody();
        if(variables != null){
            for(String[] b : variables){
                if(b.length != 2) continue;
                a = a.replaceFirst("\\{\\{[\\s]*" + b[0] + "[\\s]*\\}\\}", b[1]);
            }
        }
        a = a.replaceAll("\\{\\{([^\\{\\}])*\\}\\}", "");
        setBody(a);
        return this;
    }
    
    private void readTextFile(String fileName){
        //Debug.info(fileName);
        try{
            Scanner arq = new Scanner(new File(fileName));
            while(arq.hasNextLine()) putBody(arq.nextLine() + "\n");
            arq.close();
        }catch (Exception e){
            setBody("");
            Debug.error(e.getMessage());
        }
    }
    
    private void readImg(String fileName, String type){
        try{
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(ImageIO.read(new File(fileName)), type, outputStream);
            bytes = outputStream.toByteArray();
            outputStream.close();
        }catch (Exception e){
            bytes = new byte[0];
            Debug.error(e.getMessage());
        }
    }
    
    protected byte[] getBytes() throws Exception{
        String encod = (Parameter.isImage(contentType)) ? "" : ";charset=" + Parameter.getEncoding(encoding);
        int length = (Parameter.isImage(contentType)) ? bytes.length : body.length();
        String headerResponse = "HTTP/1.1 ";
        headerResponse += Parameter.getStatus(status);
        headerResponse += "\r\nServer: Anderson : 1.0";
        headerResponse += "\r\nDate: " + new Date().toString();
        headerResponse += "\r\nContent-length: " + length;
        headerResponse += "\r\nContent-type: " + Parameter.getContentType(contentType) + encod;
        headerResponse += "\r\n\r\n";
        if(Parameter.isImage(contentType)){
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(headerResponse.getBytes(Parameter.getEncoding(encoding)));
            outputStream.write(bytes);
            return outputStream.toByteArray();
        }
        return headerResponse.concat(body).getBytes(Parameter.getEncoding(encoding));
    }
    
}
>>>>>>> c349b21af459fa604849a938a05082d9af388b38
