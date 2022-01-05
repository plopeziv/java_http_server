package server;

import java.util.*;

interface routeInterface{
    String getResponse(String firstLine, String headers, String method);
}

public class RouteObject {
    String route;
    ArrayList<String> methods;
    routeInterface objectResponse;

    public RouteObject(String objectRoute, ArrayList<String> objectHeaders, routeInterface response){
        this.route = objectRoute;
        this.methods = objectHeaders;
        this.objectResponse = response;
    }

    public String getObjectResponse(String firstLine, String headers, String body){
        return this.objectResponse.getResponse(firstLine, headers, body);
    }

}