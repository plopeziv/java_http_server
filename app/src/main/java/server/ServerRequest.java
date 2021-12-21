package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class ServerRequest {
    BufferedReader in;

    HashMap requestLine;
    HashMap headers;
    String body;

    public ServerRequest(BufferedReader in) throws IOException {

            this.in = in;

            String requestLines = this.assembleRequest();

            requestLine = this.createRequestLine(requestLines);
            headers  = this.createHeaders(requestLines);
            body = this.createBody(this.headers);

    }

    public String assembleRequest() throws IOException {
        String requestLines = "";
        String requestLine = in.readLine();

        while (!requestLine.isEmpty()) {
            requestLines += requestLine + "\r\n";
            requestLine = in.readLine();
        }

        return requestLines;
    }

    public HashMap<String, String> createRequestLine(String requestLines){
        HashMap<String, String> requestLine = new HashMap<>();

        String[] stringRequestLine = requestLines.split("\r\n");
        stringRequestLine = stringRequestLine[0].split(" ");

        requestLine.put("Method", stringRequestLine[0]);
        requestLine.put("Path",stringRequestLine[1]);
        requestLine.put("HTTPVersion", stringRequestLine[2]);

        return requestLine;
    }

    public HashMap<String, String> createHeaders(String requestLines){
        HashMap<String, String> headers = new HashMap<>();

        String[] stringHeaders = requestLines.split("\r\n");

        for (int i = 1; i < stringHeaders.length; i++){
            String[] linePair = stringHeaders[i].split(":");
            headers.put(linePair[0], linePair[1]);
        }

        return headers;
    }

    public String createBody(HashMap headers){
        String var = (String) headers.get("Content-Length");
        if (Integer.parseInt(var.trim()) != 0){
            return "This is a body";
        } else{
            return "";
        }
    }

}