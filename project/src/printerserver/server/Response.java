package printerserver.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;
import java.util.Scanner;
import javax.imageio.ImageIO;
import printerserver.server.Parameter.*;

/**
 * Object to treat client connection response.
 * 
 * This object manage client connection response at method which treat url route.
 *
 * @author Anderson Costa
 * @version 2019/01
 *
 * @see <a href="https://github.com/AsonCS/server_java" target="_blank">Repository on GitHub</a>
 */
public class Response {
    
    private String body;
    private ContentType contentType;
    private Encoding encoding;
    private Status status;
    private byte[] bytes;

    /**
     * Empty constructor for simple response. 
     */
    public Response() {
        body = "";
        bytes = new byte[0];
        contentType = ContentType.TEXT_PLAIN;
        encoding = Encoding.UTF_8;
        status = Status.OK;
    }

    /**
     * Gets the current body.
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
     * @return {@link Response}.
     */
    public Response setBody(String body) {
        this.body = body;
        return this;
    }

    /**
     * Add the current body.
     *
     * @param body The body for replaced the current body.
     * @return {@link Response}.
     */
    public Response putBody(String body) {
        this.body += body;
        return this;
    }

    /**
     * Gets the current {@link ContentType}.
     *
     * @return {@link ContentType}.
     */
    public ContentType getContentType() {
        return contentType;
    }

    /**
     * Alters the current {@link ContentType}.
     *
     * @param contentType The {@link ContentType} for replaced the current {@link ContentType}.
     * @return {@link Response}.
     */
    public Response setContentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * Gets the current {@link Encoding}.
     *
     * @return {@link Encoding}.
     */
    public Encoding getEncoding() {
        return encoding;
    }

    /**
     * Alters the current {@link Encoding}.
     *
     * @param encoding The {@link Encoding} for replaced the current {@link Encoding}.
     * @return {@link Response}.
     */
    public Response setEncoding(Encoding encoding) {
        this.encoding = encoding;
        return this;
    }

    /**
     * Gets the current {@link Status}.
     *
     * @return {@link Status}.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Alters the current {@link Status}.
     *
     * @param status The {@link Status} for replaced the current {@link Status}.
     * @return {@link Response}.
     */
    public Response setStatus(Status status) {
        this.status = status;
        return this;
    }
    
    /**
     * This method seek and read file, that can be text or binary file.
     *
     * @param file File complete path.
     * @return {@link Response}.
     */
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
    
    /**
     * This method use {@link Response#readFile(java.lang.String)} and replaces
     * the "<code>{{ variables }}</code>" structures for given variables.  
     *
     * @param file File complete path.
     * @param variables Variables to be placed in file content.
     * @return {@link Response}.
     */
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
    
    /**
     * Gets the current content in <i>byte</i> form.
     *
     * @return Content at <i>byte</i> form.
     * @throws Exception {@link Exception}.
     */
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
