package printerserver;

public interface Handler {
    Response handler(Request request, Response response);
}
