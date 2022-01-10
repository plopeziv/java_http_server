package server;

import java.util.*;

public class RouteList {
    HashMap<String, RouteObject> startupList;

    public RouteList() {
        this.startupList = constructRouteList();
    }

    private HashMap<String, RouteObject> constructRouteList() {
        HashMap<String, RouteObject> startupList = new HashMap<>();

        ArrayList<String> simpleGetMethods = new ArrayList<>(Arrays.asList("GET", "HEAD", "OPTIONS"));
        RouteInterface simpleGet  = (String HTTPVersion, String headers, String body) ->
                HTTPVersion + " 200 OK\r\n" + headers + "\r\n";

        startupList.put("/simple_get", new RouteObject( "/simple_get", simpleGetMethods, simpleGet));


        ArrayList<String> simpleGetWithBodyMethods = new ArrayList<>(Arrays.asList("GET", "HEAD", "OPTIONS"));
        RouteInterface simpleGetWithBody  = (String HTTPVersion, String headers, String body) ->
                HTTPVersion + " 200 OK\r\n" + "\r\nHello world";

        startupList.put("/simple_get_with_body",
                new RouteObject( "/simple_get_with_body", simpleGetWithBodyMethods, simpleGetWithBody));

        ArrayList<String> redirectMethods = new ArrayList<>(Arrays.asList("GET", "HEAD", "OPTIONS"));
        RouteInterface redirect  = (String HTTPVersion, String headers, String body) ->
                HTTPVersion + " 301 Moved Permanently\r\n" + "Location: http://127.0.0.1:5000/simple_get\r\n\r\n";

        startupList.put("/redirect",
                new RouteObject( "/redirect", redirectMethods, redirect));

        ArrayList<String> echoBodyMethods = new ArrayList<>(Arrays.asList("POST", "OPTIONS"));
        RouteInterface echoBody  = (String HTTPVersion, String headers, String body) ->
                HTTPVersion + " 200 OK\r\n" + "\r\n" + body;

        startupList.put("/echo_body",
                new RouteObject( "/echo_body", echoBodyMethods, echoBody));


        return startupList;
    }

    public void AddRoute(String route, RouteObject object){
        this.startupList.put(route, object);
    }

    public void RemoveRoute(String route){
        this.startupList.remove(route);
    }

}