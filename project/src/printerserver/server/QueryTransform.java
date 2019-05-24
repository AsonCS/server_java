package printerserver.server;

import java.util.TreeMap;

/**
 * Object to treat HTTP protocol codes.
 * 
 * This object encoding and decoding the data and url to connection.
 *
 * @author Anderson Costa
 * @version 2019/01
 *
 * @see <a href="https://github.com/AsonCS/server_java" target="_blank">Repository on GitHub</a>
 */
public class QueryTransform {
    
    private static final String CODE_BACKSLASH = "%5C";   // Para barra invertida
    private static final String CODE_SLASH = "%2F";       // Para barra
    private static final String CODE_BLANK_SPACE = "%20"; // Para espaço em branco%2B
    private static final String CODE_PLUS = "%2B";        // Para "+"
    private static final String CODE_PARENT_RIG = "%28";  // Para "("
    private static final String CODE_PARENT_LEF = "%29";  // Para ")"
    private static final String BACKSLASH = "\\\\";
    private static final String SLASH = "/";
    private static final String PLUS = "\\+";
    private static final String BLANK_SPACE = " ";
    private static final String PARENT_RIG = "\\(";
    private static final String PARENT_LEF = "\\)";
    private static final String REG_BACKSLASH = "[\\\\]";
    private static final String REG_SLASH = "[/]";
    
    /**
     * This method encodes text,
     * replacing <b>"</b> <i>blank space</i>, <code>/, \, (, ) and + </code><b>"</b>.
     *
     * @param codigo Text to be encoded.
     * @return Text encoded.
     */
    public static String encode(String codigo){
        codigo = codigo.replaceAll(REG_BACKSLASH, CODE_BACKSLASH);
        codigo = codigo.replaceAll(REG_SLASH, CODE_SLASH);
        codigo = codigo.replaceAll(BLANK_SPACE, CODE_BLANK_SPACE);
        codigo = codigo.replaceAll(PLUS, CODE_PLUS);
        codigo = codigo.replaceAll(PARENT_RIG, CODE_PARENT_RIG);
        codigo = codigo.replaceAll(PARENT_LEF, CODE_PARENT_LEF);
        return codigo;
    }
    
    /**
     * This method decodes text,
     * replacing <b>"</b> <i>%5C, %2F, %20, %2B, %28 and %29</i> <b>"</b>.
     *
     * @param codigo Text to be decoded.
     * @return Text decoded.
     */
    public static String decode(String codigo){
        codigo = codigo.replaceAll(CODE_BACKSLASH, BACKSLASH);
        codigo = codigo.replaceAll(CODE_SLASH, SLASH);
        codigo = codigo.replaceAll(CODE_BLANK_SPACE, BLANK_SPACE); 
        codigo = codigo.replaceAll(CODE_PLUS, PLUS);
        codigo = codigo.replaceAll(CODE_PARENT_RIG, PARENT_RIG);
        codigo = codigo.replaceAll(CODE_PARENT_LEF, PARENT_LEF);        
        return codigo;
    }
    
    /**
     * This method extract parameters in url text or HTTP body,
     * for "<i>key=value</i>" style.
     *
     * @param keyValue Text with "<i>key=value</i>" style to be process.
     * @param params {@link TreeMap} to add parameters.
     * @return {@link TreeMap} with key-value pairs.
     */
    public static TreeMap<String, String> getKeyValue(String keyValue, TreeMap<String, String> params){
        if(params == null) params = new TreeMap<>();
        for(String b : keyValue.split("&")){
            if(b.split("=").length > 1) params.put(b.split("=")[0], b.split("=")[1]);
            else params.put(b.split("=")[0], "");
        }
        return params;
    }
    
}
