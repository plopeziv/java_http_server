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

    public String routeResponse(ServerRequest requestObject) {
        String method = (String) requestObject.requestLine.get("Method");
        switch (method){
            case "GET":
                if (this.routes.contains(requestObject.requestLine.get("Path"))){
                    String route = (String) requestObject.requestLine.get("Path");
                    switch(route){
                        case "/redirect":
                            String buildResponse = requestObject.requestLine.get("HTTPVersion") + " 301 Moved Permanently\r\n";
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
                            return   buildResponse;

                    }
                } else{
                    return requestObject.requestLine.get("HTTPVersion") + " 404 Not Found\r\n";
                }

            case "HEAD":
                if (this.routes.contains(requestObject.requestLine.get("Path"))){
                    return requestObject.requestLine.get("HTTPVersion") + " 200 OK\r\n";
                }else{
                    return requestObject.requestLine.get("HTTPVersion") + " 404 Not Found\r\n";
                }

            case "POST":
                return  "This is a post";

            case "OPTIONS":
                if (this.routes.contains(requestObject.requestLine.get("Path"))){
                    String route = (String) requestObject.requestLine.get("Path");

                    switch(route){
                        case "/method_options":
                            String buildResponse = requestObject.requestLine.get("HTTPVersion") + " 200 OK\r\n";
                            buildResponse += "Allow: GET, HEAD, OPTIONS\r\n";
                            return buildResponse;

                        case "/method_options2":
                            buildResponse = requestObject.requestLine.get("HTTPVersion") + " 200 OK\r\n";
                            buildResponse += "Allow: GET, HEAD, OPTIONS, PUT, POST\r\n";
                            return buildResponse;
                        default:
                            throw new IllegalStateException("Unexpected value: " + route);
                    }

                }else{
                    return requestObject.requestLine.get("HTTPVersion") + " 404 Not Found\r\n";
                }

            default:
                return method + " not found!";

        }
    }







}