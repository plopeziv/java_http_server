package server;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class RouteMapTest {

    @Test
    public void RouteListStartsWithStandardList() {
        RouteMap list = new RouteMap();

        assertTrue(list.startupMap.containsKey("/simple_get"));
    }

    @Test
    public void RouteListItemCanBeRemoved(){
        RouteMap list = new RouteMap();
        list.RemoveRoute("/simple_get");

        assertFalse(list.startupMap.containsKey("/simple_get"));
    }

    @Test
    public void RouteListItemCanBeAdded(){
        RouteMap list = new RouteMap();

        String route = "/new_route";
        ArrayList<String> headers = new ArrayList<>(Arrays.asList("GET", "HEAD", "OPTIONS"));

        RouteBehavior behavior = (String HTTPVersion, String info, String body) ->
                HTTPVersion + " 200 OK\r\n" + info + "\r\n" + "Hello World";

        Route object = new Route(route, headers, behavior);

        list.AddRoute(route, object);

        assertTrue(list.startupMap.containsKey(route));
    }

}