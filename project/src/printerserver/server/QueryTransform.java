package printerserver.server;

import java.util.TreeMap;

public class QueryTransform {
    
    private static final String CODE_BACKSLASH = "%5C";   // Para barra invertida
    private static final String CODE_SLASH = "%2F";       // Para barra
    private static final String CODE_BLANK_SPACE = "%20"; // Para espaço em branco%2B
    private static final String CODE_PLUS = "%2B";        // Para "+"
    private static final String BACKSLASH = "\\\\";
    private static final String SLASH = "/";
    private static final String PLUS = "\\+";
    private static final String BLANK_SPACE = " ";
    private static final String REG_BACKSLASH = "[\\\\]";
    private static final String REG_SLASH = "[/]";
    
    public static String encode(String codigo){
        codigo = codigo.replaceAll(REG_BACKSLASH, CODE_BACKSLASH);
        codigo = codigo.replaceAll(REG_SLASH, CODE_SLASH);
        codigo = codigo.replaceAll(BLANK_SPACE, CODE_BLANK_SPACE);
        codigo = codigo.replaceAll(PLUS, CODE_PLUS);
        return codigo;
    }
    
    public static String decode(String codigo){
        codigo = codigo.replaceAll(CODE_BACKSLASH, BACKSLASH);
        codigo = codigo.replaceAll(CODE_SLASH, SLASH);
        codigo = codigo.replaceAll(CODE_BLANK_SPACE, BLANK_SPACE); 
        codigo = codigo.replaceAll(CODE_PLUS, PLUS);        
        return codigo;
    }
    
    public static TreeMap<String, String> getKeyValue(String keyValue, TreeMap<String, String> params){
        if(params == null) params = new TreeMap<>();
        for(String b : keyValue.split("&")){
            if(b.split("=").length > 1) params.put(b.split("=")[0], b.split("=")[1]);
            else params.put(b.split("=")[0], "");
        }
        return params;
    }
    
}
