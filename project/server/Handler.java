package printerserver.server;

public interface Handler {
    Response handler(Request request, Response response);
}
