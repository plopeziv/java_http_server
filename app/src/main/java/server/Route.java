package server;

import java.util.*;

interface RouteBehavior {
    String getResponse(String HTTPVersion, String headers, String body);
}

public class Route {
    String route;
    ArrayList<String> methods;
    RouteBehavior behavior;

    public Route(String name, ArrayList<String> methods, RouteBehavior behavior){
        this.route = name;
        this.methods = methods;
        this.behavior = behavior;
    }

    public String getObjectResponse(String HTTPVersion, String headers, String body){
        return this.behavior.getResponse(HTTPVersion, headers, body);
    }

}