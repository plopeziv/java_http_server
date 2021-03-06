/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package server;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class ServerRequestTest {

    @Test
    public void assembleRequest_returns_request_as_string() throws IOException {
        String simulatedRequest = "OPTIONS /method_options2 HTTP/1.1\r\n" +
                "Connection: close\r\n" + "Host: 127.0.0.1:5000\r\n" +
                "User-Agent: http.rb/4.3.0\r\n" + "Content-Length: 0\r\n";
        InputStream streamRequest = new ByteArrayInputStream(simulatedRequest.getBytes(StandardCharsets.UTF_8));

        ServerRequest testRequest = new ServerRequest(streamRequest);

        assertEquals(testRequest.requestLines, simulatedRequest);
    }

    @Test
    public void createRequestLine_returns_the_firstRowHash() throws IOException {
        String simulatedRequest = "OPTIONS /method_options2 HTTP/1.1\r\n" +
                "Connection: close\r\n" +
                "Host: 127.0.0.1:5000\r\n" +
                "User-Agent: http.rb/4.3.0\r\n" +
                "Content-Length: 0\r\n" +
                "";
        InputStream streamRequest = new ByteArrayInputStream(simulatedRequest.getBytes(StandardCharsets.UTF_8));
        HashMap <String, String> testHash = new HashMap<>();
        testHash.put("Path", "/method_options2");
        testHash.put("Method", "OPTIONS");
        testHash.put("HTTPVersion", "HTTP/1.1");

        ServerRequest testRequest = new ServerRequest(streamRequest);

        assertEquals(testRequest.requestLine, testHash);
    }

    @Test
    public void createRequestLine_with_body() throws IOException {
        String simulatedRequest = "OPTIONS /method_options2 HTTP/1.1\r\n" +
                "Connection: close\r\n" +
                "Host: 127.0.0.1:5000\r\n" +
                "User-Agent: http.rb/4.3.0\r\n" +
                "Content-Length: 9\r\n" +
                "\r\n"+
                "SoMe BoDy";
        InputStream streamRequest = new ByteArrayInputStream(simulatedRequest.getBytes(StandardCharsets.UTF_8));

        ServerRequest testRequest = new ServerRequest(streamRequest);

        assertEquals("some body", testRequest.body);
    }

    @Test
    public void createHeaders_returns_headers_hash() throws IOException {
        String simulatedRequest = "OPTIONS /method_options2 HTTP/1.1\r\n" +
                "Connection: close\r\n" + "Host: 127.0.0.1:5000\r\n" +
                "User-Agent: http.rb/4.3.0\r\n" + "Content-Length: 0\r\n";
        InputStream streamRequest = new ByteArrayInputStream(simulatedRequest.getBytes(StandardCharsets.UTF_8));
        HashMap <String, String> testHash = new HashMap<>();
        testHash.put("Connection", "close");
        testHash.put("Host", "127.0.0.1:5000");
        testHash.put("User-Agent", "http.rb/4.3.0");
        testHash.put("Content-Length", "0");

        ServerRequest testRequest = new ServerRequest(streamRequest);

        assertEquals(testRequest.headers, testHash);
    }

    @Test
    public void createBody_isEmpty_if_contentLengthEqualsZero() throws IOException {
        String simulatedRequest = "OPTIONS /method_options2 HTTP/1.1\r\n" +
                "Connection: close\r\n" + "Host: 127.0.0.1:5000\r\n" +
                "User-Agent: http.rb/4.3.0\r\n" + "Content-Length: 0\r\n";
        InputStream streamRequest = new ByteArrayInputStream(simulatedRequest.getBytes(StandardCharsets.UTF_8));

        ServerRequest testRequest = new ServerRequest(streamRequest);

        assertTrue(testRequest.body.isEmpty());
    }

    @Test
    public void createBody_isNotEmpty_if_contentLengthEqualsZero() throws IOException {
        String simulatedRequest = "OPTIONS /method_options2 HTTP/1.1\r\n" +
                "Connection: close\r\n" + "Host: 127.0.0.1:5000\r\n" +
                "User-Agent: http.rb/4.3.0\r\n" + "Content-Length: 1\r\n";
        InputStream streamRequest = new ByteArrayInputStream(simulatedRequest.getBytes(StandardCharsets.UTF_8));

        ServerRequest testRequest = new ServerRequest(streamRequest);

        assertFalse(testRequest.body.isEmpty());
    }
}