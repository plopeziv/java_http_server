package server;

import java.util.*;

public class ServerResponse {
    ServerRequest currentRequest;
    String response;
    ArrayList<String> routes;

    public ServerResponse(ServerRequest request, ArrayList<String> routeList) {
        currentRequest = request;
        routes  = routeList;

        response = routeResponse(request);
    }

    private String routeResponse(ServerRequest requestObject) {
        String method = (String) requestObject.requestLine.get("Method");
        String  route = (String) requestObject.requestLine.get("Path");
        String buildResponse;

        if (this.routes.contains((route))) {
            switch (method) {
                case "GET":
                    switch (route) {
                        case "/redirect":
                            buildResponse = requestObject.requestLine.get("HTTPVersion") + " 301 Moved Permanently\r\n";
                            buildResponse += "Location: http://127.0.0.1:5000/simple_get\r\n";
                            return buildResponse;

                        case "/head_request":
                            buildResponse = requestObject.requestLine.get("HTTPVersion") + " 405 Method Not Allowed\r\n";
                            buildResponse += "Allow: HEAD, OPTIONS\r\n";
                            return buildResponse;

                        case "/simple_get_with_body":
                            buildResponse = requestObject.requestLine.get("HTTPVersion") + " 200 OK\r\n";
                            buildResponse += "\r\n";
                            buildResponse += "Hello World";
                            return buildResponse;
                    }

                case "HEAD":
                    return requestObject.requestLine.get("HTTPVersion") + " 200 OK\r\n";


                case "POST":
                    buildResponse = requestObject.requestLine.get("HTTPVersion") + " 200 OK\r\n";
                    buildResponse += "\r\n";
                    buildResponse += "This is a post";
                    return buildResponse;


                case "OPTIONS":

                    switch (route) {
                        case "/method_options":
                            buildResponse = requestObject.requestLine.get("HTTPVersion") + " 200 OK\r\n";
                            buildResponse += "Allow: GET, HEAD, OPTIONS\r\n";
                            return buildResponse;

                        case "/method_options2":
                            buildResponse = requestObject.requestLine.get("HTTPVersion") + " 200 OK\r\n";
                            buildResponse += "Allow: GET, HEAD, OPTIONS, PUT, POST\r\n";
                            return buildResponse;

                            default:
                            throw new IllegalStateException("Unexpected value: " + route);
                    }

                default:
                    return method + " not found!";
            }
        } else{
            return requestObject.requestLine.get("HTTPVersion") + " 404 Not Found\r\n";
        }
    }
}