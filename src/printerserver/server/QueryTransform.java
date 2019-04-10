package printerserver.server;

public class QueryTransform {
    
    private static final String CODE_BACKSLASH = "%5C";   // Para barra invertida
    private static final String CODE_SLASH = "%2F";       // Para barra
    private static final String CODE_BLANK_SPACE = "%20"; // Para espaço em branco
    private static final String BACKSLASH = "\\\\";
    private static final String SLASH = "/";
    private static final String BLANK_SPACE = " ";
    private static final String REG_BACKSLASH = "[\\\\]";
    private static final String REG_SLASH = "[/]";
    
    public static String encode(String codigo){
        codigo = codigo.replaceAll(REG_BACKSLASH, CODE_BACKSLASH);
        codigo = codigo.replaceAll(REG_SLASH, CODE_SLASH);
        codigo = codigo.replaceAll(BLANK_SPACE, CODE_BLANK_SPACE);
        return codigo;
    }
    
    public static String decode(String codigo){
        codigo = codigo.replaceAll(CODE_BACKSLASH, BACKSLASH);
        codigo = codigo.replaceAll(CODE_SLASH, SLASH);
        codigo = codigo.replaceAll(CODE_BLANK_SPACE, BLANK_SPACE);        
        return codigo;
    }
    
}
