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
        String routeMethod = String.valueOf(requestObject.requestLine.get("Path"));

        response = this.assembleResponse(routeVersion, routePath, routeMethod);

        return response;

    }

    private String assembleResponse(String routeVersion, String routePath, String routeMethod){
        Route obj = this.routeList.get(routePath);

        if (obj != null){
            if (!checkIfMethodIsAllowed(this.currentRequest).equals("")){
                return checkIfMethodIsAllowed(this.currentRequest);
            } else{
               String response = obj.getObjectResponse(routeVersion, this.getHeaders(), this.currentRequest.body);

               response = trimBody(response, routePath, routeMethod);
               return response;
            }
        } else {
            return routeVersion +  " 404 Not Found\r\n\r\n";
        }
    }

    private String trimBody(String originalResponse, String routePath, String routeMethod){

        if (routeMethod.equals("HEAD")|| routeMethod.equals("OPTIONS")) {
            int breakIndex = response.indexOf("\r\n\r\n");
            response = response.substring(0, breakIndex) + "\r\n\r\n";

            if (routeMethod.equals("OPTIONS")) {
                ArrayList<String> allowedHeaders = routeList.get(routePath).methods;
                response = response.substring(0, breakIndex) + "\r\n" +
                        UnpackHeaders(allowedHeaders) + response.substring(breakIndex);
            }

            return response;

        } else {
            return originalResponse;
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

        unpackedHeaders = new StringBuilder(unpackedHeaders.substring(0, unpackedHeaders.length() - 2));

        return String.valueOf(unpackedHeaders);
    }
}