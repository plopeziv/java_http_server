package server;

import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class ServerResponseTest {
    static HashMap<String, Route> testRoutes = new HashMap<>();

    @Before
    public void testSetup() {
        ArrayList<String> availableMethods = new ArrayList<>(
                Arrays.asList("GET", "HEAD", "OPTIONS"));

        RouteBehavior simpleGet = (String firstLine, String headers, String body) ->
                firstLine + " 200 OK\r\n" + headers + "";

        testRoutes.put("/simple_get",
                new Route( "/simple_get", availableMethods, simpleGet));
    }

    @Test
    public void Server_response_sets_attributes() throws IOException {
        ServerRequest testRequest;

        String simulatedRequest = "GET /simple_get HTTP/1.1\r\n" +
                "Connection: close\r\n" + "Host: 127.0.0.1:5000\r\n" +
                "User-Agent: http.rb/4.3.0\r\n" + "Content-Length: 8\r\n";

        InputStream streamRequest = new ByteArrayInputStream(simulatedRequest.getBytes(StandardCharsets.UTF_8));

        testRequest = new ServerRequest(streamRequest);

        ServerResponse testResponse = new ServerResponse(testRequest, testRoutes);

        assertEquals(testResponse.routeMap, testRoutes);
        assertEquals(testResponse.routes, new ArrayList<>(Collections.singletonList("/simple_get")));
    }

    @Test
    public void Server_returns_simple_get() throws IOException {
        ServerRequest testRequest;

        String simulatedRequest = "GET /simple_get HTTP/1.1\r\n" +
                "Connection: close\r\n" + "Host: 127.0.0.1:5000\r\n" +
                "User-Agent: http.rb/4.3.0\r\n" + "Content-Length: 8\r\n";

        InputStream streamRequest = new ByteArrayInputStream(simulatedRequest.getBytes(StandardCharsets.UTF_8));

        testRequest = new ServerRequest(streamRequest);

        ServerResponse testResponse = new ServerResponse(testRequest, testRoutes);

        System.out.println(testResponse);

        String simpleGetResponse = "HTTP/1.1 200 OK\r\n" +
                "Connection: close\r\n" + "Host: 127.0.0.1:5000\r\n" +
                "User-Agent: http.rb/4.3.0\r\n" + "Content-Length: 8\r\n";

        assertEquals(simpleGetResponse, testResponse.response);
    }

    @Test
    public void Server_returns_404() throws IOException {
        ServerRequest testRequest;

        String simulatedRequest = "OPTIONS /weird_address HTTP/1.1\r\n" +
                "Connection: close\r\n" + "Host: 127.0.0.1:5000\r\n" +
                "User-Agent: http.rb/4.3.0\r\n" + "Content-Length: 8\r\n";

        InputStream streamRequest = new ByteArrayInputStream(simulatedRequest.getBytes(StandardCharsets.UTF_8));

        testRequest = new ServerRequest(streamRequest);

        ServerResponse testResponse = new ServerResponse(testRequest, testRoutes);

        System.out.println(testResponse);

        String simpleGetResponse = "HTTP/1.1 404 Not Found\r\n\r\n";

        assertEquals(simpleGetResponse, testResponse.response);
    }

    @Test
    public void Check_headers_returns_header_not_allowed() throws IOException {

        ServerRequest testRequest;

        String simulatedRequest = "POST /simple_get HTTP/1.1\r\n" +
                "Connection: close\r\n" + "Host: 127.0.0.1:5000\r\n" +
                "User-Agent: http.rb/4.3.0\r\n" + "Content-Length: 8\r\n";

        InputStream streamRequest = new ByteArrayInputStream(simulatedRequest.getBytes(StandardCharsets.UTF_8));

        testRequest = new ServerRequest(streamRequest);

        ServerResponse testResponse = new ServerResponse(testRequest, testRoutes);

        String headerCheck = "HTTP/1.1 405 Method Not Allowed\r\n" +
        "Allow: GET, HEAD, OPTIONS\r\n\r\n";

        assertEquals(headerCheck, testResponse.checkIfMethodIsAllowed(testRequest));
    }

    @Test
    public void Check_headers_returns_empty_string() throws IOException {

        ServerRequest testRequest;

        String simulatedRequest = "GET /simple_get HTTP/1.1\r\n" +
                "Connection: close\r\n" + "Host: 127.0.0.1:5000\r\n" +
                "User-Agent: http.rb/4.3.0\r\n" + "Content-Length: 8\r\n";

        InputStream streamRequest = new ByteArrayInputStream(simulatedRequest.getBytes(StandardCharsets.UTF_8));

        testRequest = new ServerRequest(streamRequest);

        ServerResponse testResponse = new ServerResponse(testRequest, testRoutes);

        String headerCheck = "";

        assertEquals(headerCheck, testResponse.checkIfMethodIsAllowed(testRequest));
    }

}