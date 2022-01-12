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
                HTTPVersion + " 200 OK\r\n\r\n" + "Hello world";

        startupList.put("/simple_get_with_body", new Route("/simple_get_with_body",
                simpleGetWithBodyMethods, simpleGetWithBody));


        ArrayList<String> redirectMethods = new ArrayList<>(Arrays.asList("GET", "HEAD", "OPTIONS"));
        RouteBehavior redirect = (String HTTPVersion, String headers, String body) ->
                HTTPVersion + " 301 Moved Permanently\r\n" +  "Location: http://127.0.0.1:5000/simple_get\r\n\r\n";

        startupList.put("/redirect", new Route("/redirect", redirectMethods,
                redirect));


        ArrayList<String> headRequestMethods = new ArrayList<>(Arrays.asList("HEAD", "OPTIONS"));
        RouteBehavior headRequest = (String HTTPVersion, String headers, String body) ->
                HTTPVersion + " 200 OK\r\n" + headers + "\r\ndummy body";

        startupList.put("/head_request", new Route("/head_request", headRequestMethods,
                headRequest));


        ArrayList<String> methodOptionsMethods = new ArrayList<>(Arrays.asList("GET", "HEAD", "OPTIONS"));
        RouteBehavior methodOptions  = (String HTTPVersion, String headers, String body) ->
                HTTPVersion + " 200 OK\r\n" + headers + "\r\n\r\n";

        startupList.put("/method_options", new Route( "/method_options", methodOptionsMethods, methodOptions));


        ArrayList<String> methodOptionsTwoMethods = new ArrayList<>(Arrays.asList("GET", "HEAD", "OPTIONS",
                "PUT", "POST"));
        RouteBehavior methodOptionsTwo  = (String HTTPVersion, String headers, String body) ->
                HTTPVersion + " 200 OK\r\n" + headers + "\r\n\r\n";

        startupList.put("/method_options2", new Route( "/method_options2", methodOptionsTwoMethods,
                methodOptionsTwo));


        ArrayList<String> echoBodyMethods = new ArrayList<>(Arrays.asList("POST", "OPTIONS"));
        RouteBehavior echoBody = (String HTTPVersion, String headers, String body) ->
                HTTPVersion + " 200 OK\r\n" + headers + "\r\n" + body;

        startupList.put("/echo_body", new Route("/echo_body", echoBodyMethods,
                echoBody));



        return startupList;
    }

    public void AddRoute(String route, Route object){
        this.startupList.put(route, object);
    }

    public void RemoveRoute(String route){
        this.startupList.remove(route);
    }

}