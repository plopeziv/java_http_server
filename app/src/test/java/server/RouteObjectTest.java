package server;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class RouteObjectTest {

    @Test
    public void lambda_sets_attributes() {
        String route = "/test_route";

        ArrayList<String> methods = new ArrayList<>();
        methods.add("Why");
        methods.add("Java");
        methods.add("Why?");

        RouteBehavior routeResponse = (String firstLine, String headers, String body) ->
                "Because it be that way sometimes";

        Route object = new Route(route, methods, routeResponse);

        assertEquals(object.route, "/test_route");
        assertEquals(object.methods, Arrays.asList("Why", "Java", "Why?"));
    }

    @Test
    public void lambda_runs_simple_function(){
        String route = "/test_route";

        ArrayList<String> methods = new ArrayList<>();
        methods.add("Why");
        methods.add("Java");
        methods.add("Why?");

        RouteBehavior routeResponse = (String firstLine, String headers, String body) ->
                "Because it be that way sometimes";

        Route object = new Route(route, methods, routeResponse);

        assertEquals(object.getObjectResponse("", "", ""),
                "Because it be that way sometimes");

    }

    @Test
    public void lambda_runs_function_with_object() {
        String route = "/test_route";

        ArrayList<String> methods = new ArrayList<>();
        methods.add("Why");
        methods.add("Java");
        methods.add("Why?");

        RouteBehavior routeResponse = (String firstLine, String headers, String body) ->
                firstLine + "\r\n" + headers + "\r\n\r\n" + body;

        Route object = new Route(route, methods, routeResponse);

        assertEquals(object.getObjectResponse("Wow", "This", "Worked!"),
                "Wow\r\n" +
                        "This\r\n" +
                        "\r\n" +
                        "Worked!");
    }
}