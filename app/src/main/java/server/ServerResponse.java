package server;

import java.util.*;

public class ServerResponse {
    ServerRequest currentRequest;
    HashMap<String, Route> routeMap;
    String response;
    ArrayList<String> routes;

    public ServerResponse(ServerRequest request, HashMap<String, Route> routeList) {
        this.currentRequest = request;
        this.routeMap = routeList;
        this.routes  = new ArrayList<>(routeList.keySet());

        response = returnResponse(request);
    }

    private String returnResponse(ServerRequest requestObject){
        String routeVersion = String.valueOf(requestObject.requestLine.get("HTTPVersion"));
        String routePath = String.valueOf(requestObject.requestLine.get("Path"));
        String routeMethod = String.valueOf(requestObject.requestLine.get("Method"));

        response = this.assembleResponse(routeVersion, routePath, routeMethod);

        return response;

    }

    private String assembleResponse(String routeVersion, String routePath, String routeMethod){
        Route obj = this.routeMap.get(routePath);
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
        if (routeMethod.equals("HEAD") || routeMethod.equals("OPTIONS")){
            int breakIndex = originalResponse.indexOf("\r\n\r\n");
            response = originalResponse;

            if(breakIndex != -1) {
                response = originalResponse.substring(0, breakIndex) + "\r\n\r\n";
            }

            if (routeMethod.equals("OPTIONS")){
                ArrayList<String> allowedMethods = routeMap.get(routePath).methods;
                int optionsIndex = originalResponse.indexOf("\r\n");

                response = response.substring(0, optionsIndex) + "\r\n" +
                        unpackMethods(allowedMethods) + response.substring(optionsIndex);
            }

            return response;

        }else{
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
        ArrayList<String> allowedMethods = routeMap.get(route).methods;

        assert allowedMethods != null;

        if (!allowedMethods.contains(method)){
            String buildResponse = requestObject.requestLine.get("HTTPVersion") + " 405 Method Not Allowed\r\n";
            buildResponse += unpackMethods(allowedMethods) + "\r\n\r\n";
            return buildResponse;
        } else {
            return "";
        }
    }

    private String unpackMethods(ArrayList<String> allowedMethods) {
        StringBuilder unpackedMethods = new StringBuilder("Allow: ");

        for (String method : allowedMethods) {
            unpackedMethods.append(method.toUpperCase(Locale.ROOT));
            unpackedMethods.append(", ");
        }

        unpackedMethods = new StringBuilder(unpackedMethods.substring(0, unpackedMethods.length() - 2));

        return String.valueOf(unpackedMethods);
    }
}