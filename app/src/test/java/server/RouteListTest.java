package server;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class RouteListTest {

    @Test
    public void RouteListStartsWithStandardList() {
        RouteList list = new RouteList();

        assertTrue(list.startupList.containsKey("/simple_get"));
    }

    @Test
    public void RouteListItemCanBeRemoved(){
        RouteList list = new RouteList();
        list.RemoveRoute("/simple_get");

        assertFalse(list.startupList.containsKey("/simple_get"));
    }

    @Test
    public void RouteListItemCanBeAdded(){
        RouteList list = new RouteList();

        String route = "/new_route";
        ArrayList<String> headers = new ArrayList<>(Arrays.asList("GET", "HEAD", "OPTIONS"));

        RouteBehavior behavior = (String HTTPVersion, String info, String body) ->
                HTTPVersion + " 200 OK\r\n" + info + "\r\n" + "Hello World";

        Route object = new Route(route, headers, behavior);

        list.AddRoute(route, object);

        assertTrue(list.startupList.containsKey(route));
    }

}