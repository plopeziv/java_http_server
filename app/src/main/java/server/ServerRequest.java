package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class ServerRequest {
    BufferedReader in;

    HashMap<String, String> requestLine;
    HashMap<String, String> headers;
    String body;
    String requestLines;

    public ServerRequest(BufferedReader in) throws IOException {

            this.in = in;

            this.requestLines = this.assembleRequest();

            requestLine = this.createRequestLine(requestLines);
            headers  = this.createHeaders(requestLines);
            body = this.createBody(this.headers);

    }

    private String assembleRequest() throws IOException {
        StringBuilder requestLines = new StringBuilder();
        String requestLine = in.readLine();

        while (requestLine != null && !requestLine.isEmpty()) {
            requestLines.append(requestLine).append("\r\n");
            requestLine = in.readLine();
        }

        return requestLines.toString();
    }

    private HashMap<String, String> createRequestLine(String requestLines){
        HashMap<String, String> requestLine = new HashMap<>();

        String[] stringRequestLine = requestLines.split("\r\n");
        stringRequestLine = stringRequestLine[0].split(" ");

        requestLine.put("Method", stringRequestLine[0]);
        requestLine.put("Path",stringRequestLine[1]);
        requestLine.put("HTTPVersion", stringRequestLine[2]);

        return requestLine;
    }

    private HashMap<String, String> createHeaders(String requestLines){
        HashMap<String, String> headers = new LinkedHashMap<>();

        String[] stringHeaders = requestLines.split("\r\n");

        for (int i = 1; i < stringHeaders.length; i++){
            String[] linePair = stringHeaders[i].split(":", 2);
            String headerValue = linePair[1];
            headers.put(linePair[0], headerValue.trim());
        }

        return headers;
    }

    private String createBody(HashMap<String, String> headers){
        String var = headers.get("Content-Length");
        if (Integer.parseInt(var.trim()) != 0){
            return "Hello World";
        } else{
            return "";
        }
    }

}