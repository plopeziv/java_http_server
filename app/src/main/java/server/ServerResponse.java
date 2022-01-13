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

        Route obj = this.routeList.get(routePath);

        if (obj != null){
            return obj.getObjectResponse(routeVersion, this.getHeaders(), this.currentRequest.body);
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
}