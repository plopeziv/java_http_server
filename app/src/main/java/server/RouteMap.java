package server;

import java.util.*;

public class RouteMap {
    HashMap<String, Route> startupList;

    public RouteMap() {
        this.startupList = constructRouteList();
    }

    private HashMap<String, Route> constructRouteList() {
        HashMap<String, Route> startupList = new HashMap<>();

        ArrayList<String> simpleGetMethods = new ArrayList<>(Arrays.asList("GET", "HEAD", "OPTIONS"));
        RouteBehavior simpleGet  = (String HTTPVersion, String headers, String body) ->
                HTTPVersion + " 200 OK\r\n" + headers + "\r\n\r\n";

        startupList.put("/simple_get", new Route( "/simple_get", simpleGetMethods, simpleGet));


        ArrayList<String> simpleGetWithBodyMethods = new ArrayList<>(Arrays.asList("GET", "HEAD", "OPTIONS"));
        RouteBehavior simpleGetWithBody = (String HTTPVersion, String headers, String body) ->
                HTTPVersion + " 200 OK\r\n" + headers + "\r\n" + "Hello World";

        startupList.put("/simple_get_with_body", new Route("/simple_get_with_body",
                simpleGetWithBodyMethods, simpleGetWithBody));

        ArrayList<String> methodOptionsMethods = new ArrayList<>(Arrays.asList("GET", "HEAD", "OPTIONS"));
        RouteBehavior methodOptions = (String HTTPVersion, String headers, String body) ->
                HTTPVersion + " 200 OK\r\n" + "Allow: GET, HEAD, OPTIONS";

        startupList.put("/method_options", new Route("/method_options", methodOptionsMethods,
                methodOptions));

        ArrayList<String> methodOptionsTwoMethods = new ArrayList<>(Arrays.asList("GET", "HEAD", "OPTIONS", "PUT", "POST"));
        RouteBehavior methodOptionsTwo = (String HTTPVersion, String headers, String body) ->
                HTTPVersion + " 200 OK\r\n" + "Allow: GET, HEAD, OPTIONS, PUT, POST";

        startupList.put("/method_options2", new Route("/method_options2", methodOptionsTwoMethods,
                methodOptionsTwo));

        return startupList;
    }

    public void AddRoute(String route, Route object){
        this.startupList.put(route, object);
    }

    public void RemoveRoute(String route){
        this.startupList.remove(route);
    }

}