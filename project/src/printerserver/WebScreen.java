package printerserver;

import java.awt.SystemTray;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import netscape.javascript.JSObject;

/**
 *
 * @author anderson
 */
public class WebScreen extends Application{
    
    private static final String PAGE_NOT_FOUND = "<h1>Page not Found!</h1>";
    private static final String PATH_FILE = "file:///";
    
    private static Stage primaryStage;
    private static String htmlContent;
    private static String name;
    private static Object jsObject;
    private static WebEngine webEngine;
    private static ArrayList<String> jsStatments = new ArrayList<>();

    public static void show(URL htmlPath, Object jsObject){
        if(SystemTray.isSupported()){
            Platform.setImplicitExit(false);
        }
        if(htmlPath == null){
            WebScreen.htmlContent = "";
        }else{
            WebScreen.htmlContent = htmlPath.toExternalForm();
        }
        WebScreen.name = "Page";
        WebScreen.jsObject = jsObject == null ? new Object() : jsObject;
        new Thread(() -> {
            Application.launch(WebScreen.class, new String());
        }).start();
    }
    
    public static void init(URL htmlPath){
        show(htmlPath, new Object());
    }

    public static void init(String url, Object jsObject) throws MalformedURLException{
        show(new URL(url), jsObject);
    }

    public static void init(String url) throws MalformedURLException{
        WebScreen.init(new URL(url));
    }

    public static void init(File filePath, Object jsObject) throws MalformedURLException{
        WebScreen.init(WebScreen.PATH_FILE + filePath.getAbsolutePath(), jsObject);
    }

    public static void init(File filePath) throws MalformedURLException{
        WebScreen.init(WebScreen.PATH_FILE + filePath.getAbsolutePath());
    }
    
    public static void show(){
        Platform.runLater(() -> WebScreen.primaryStage.show());
    }
    
    public static void close(){
        Platform.runLater(() -> WebScreen.primaryStage.close());
    }
    
    public static boolean isVisible(){
        return primaryStage.isShowing();
    }
    
    public static void putJs(String js){
        if(webEngine != null){
            if(webEngine.getLoadWorker().getState() == Worker.State.SUCCEEDED) {
                webEngine.executeScript(js);
            }
        }else{
            jsStatments.add(js);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        WebScreen.primaryStage = stage;
                
        WebView webView = new WebView();
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);     
        webEngine.setOnAlert(event -> showAlert(event));
        webEngine.setConfirmHandler(param -> showConfirmDialog(param));
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> 
            changeListener(observable, oldValue, newValue)
        );
        if(WebScreen.htmlContent.isEmpty()){
            webEngine.loadContent(WebScreen.PAGE_NOT_FOUND);
        }else{
            webEngine.load(WebScreen.htmlContent);
        }
        
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 500, 400);
        root.getChildren().add(webView);

        WebScreen.primaryStage.setTitle(WebScreen.name);
        WebScreen.primaryStage.setScene(scene);
        WebScreen.primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        if(!SystemTray.isSupported()){
            PrinterServer.quit();
        }
    }
    
    private void showAlert(WebEvent<String> event){
        JOptionPane.showMessageDialog(null, 
                event.getData(), 
                WebScreen.name + " says", 
                JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private boolean showConfirmDialog(String param){
        return JOptionPane.showConfirmDialog(null, 
                param,  
                WebScreen.name + " says", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.QUESTION_MESSAGE
        ) == 0;
    }
    
    private void changeListener(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue){
        if(newValue == Worker.State.SUCCEEDED){
            WebScreen.name = webEngine.getTitle() != null ? webEngine.getTitle() : "Page";
            WebScreen.primaryStage.setTitle(WebScreen.name);
            JSObject jso = (JSObject) webEngine.executeScript("window");
            jso.setMember(WebScreen.jsObject.getClass().getSimpleName(), WebScreen.jsObject);
            jsStatments.forEach(statment -> webEngine.executeScript(statment));
            jsStatments.clear();
        }else if(newValue == Worker.State.FAILED){
            WebScreen.name = "Page";
            WebScreen.primaryStage.setTitle(WebScreen.name);
            webEngine.loadContent(WebScreen.PAGE_NOT_FOUND);
        }      
    }
    
}
