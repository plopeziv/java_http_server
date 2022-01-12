package server;

import java.util.*;

public class ServerResponse {
    ServerRequest currentRequest;
    HashMap<String, Route> routeList;
    String response;
    ArrayList<String> routes;

    public ServerResponse(ServerRequest request, HashMap<String, Route> routeList) {
        this.currentRequest = request;
        this.routeList = routeList;
        this.routes  = new ArrayList<>(routeList.keySet());

        response = returnResponse(request);
    }

    private String returnResponse(ServerRequest requestObject){
        String routeVersion = String.valueOf(requestObject.requestLine.get("HTTPVersion"));
        String routePath = String.valueOf(requestObject.requestLine.get("Path"));
        String response;

        response = this.returnOriginalResponse(routeVersion, routePath);

        if (requestObject.requestLine.get("Method").equals("OPTIONS")){
            ArrayList<String> allowedHeaders = routeList.get(routePath).methods;
            int breakIndex = response.indexOf("\r\n");
            response = response.substring(0, breakIndex) + "\r\n" +
                    UnpackHeaders(allowedHeaders) + response.substring(breakIndex);
        }

        if (requestObject.requestLine.get("Method").equals("HEAD")){
            int breakIndex = response.indexOf("\r\n\r\n");
            response = response.substring(0, breakIndex) + "\r\n\r\n";
        }

        return response;

    }

    private String returnOriginalResponse(String routeVersion, String routePath){
        Route obj = this.routeList.get(routePath);

        if (obj != null){
            if (!checkIfMethodIsAllowed(this.currentRequest).equals("")){
                return checkIfMethodIsAllowed(this.currentRequest);
            } else{
                return obj.getObjectResponse(routeVersion, this.getHeaders(), this.currentRequest.body);
            }
        } else {
            return routeVersion +  " 404 Not Found\r\n\r\n";
        }
    }

    private String getHeaders(){
        HashMap<String,String> headers = this.currentRequest.headers;
        StringBuilder headerList = new StringBuilder();

        for (String key: headers.keySet()){
            headerList.append(key).append(": ").append(headers.get(key)).append("\r\n");
        }

        return headerList.toString();
    }


    public String checkIfMethodIsAllowed(ServerRequest requestObject) {
        String method = requestObject.requestLine.get("Method");
        String route = requestObject.requestLine.get("Path");
        ArrayList<String> allowedHeaders = routeList.get(route).methods;

        assert allowedHeaders != null;

        if (!allowedHeaders.contains(method)){
            String buildResponse = requestObject.requestLine.get("HTTPVersion") + " 405 Method Not Allowed\r\n";
            buildResponse += UnpackHeaders(allowedHeaders) + "\r\n\r\n";
            return buildResponse;
        } else {
            return "";
        }
    }

    private String UnpackHeaders(ArrayList<String> allowedHeaders) {
        StringBuilder unpackedHeaders = new StringBuilder("Allow: ");

        for (String header : allowedHeaders) {
            unpackedHeaders.append(header.toUpperCase(Locale.ROOT));
            unpackedHeaders.append(", ");
        }

        if (unpackedHeaders.length() >1){
            unpackedHeaders = new StringBuilder(unpackedHeaders.substring(0, unpackedHeaders.length() - 2));
        }

        return String.valueOf(unpackedHeaders);
    }
}