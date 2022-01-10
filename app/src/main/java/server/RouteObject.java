package server;

import java.util.*;

interface RouteInterface {
    String getResponse(String HTTPVersion, String headers, String body);
}

public class RouteObject {
    String route;
    ArrayList<String> methods;
    RouteInterface objectResponse;

    public RouteObject(String objectRoute, ArrayList<String> objectHeaders, RouteInterface response){
        this.route = objectRoute;
        this.methods = objectHeaders;
        this.objectResponse = response;
    }

    public String getObjectResponse(String HTTPVersion, String headers, String body){
        return this.objectResponse.getResponse(HTTPVersion, headers, body);
    }

}