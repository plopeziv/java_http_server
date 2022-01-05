/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package server;

import java.net.*;
import java.util.*;
import java.io.*;

public class GreetServer implements Runnable {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    int portNumber;
    HashMap routeList;


    public GreetServer(int port){
        this.portNumber = port;
        this.routeList =  this.constructRouteList();
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);

        while (true){
            try(Socket clientSocket = serverSocket.accept()){
                System.out.println("Client socket " + clientSocket.toString());

                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                ServerRequest myRequest = new ServerRequest(in);

                System.out.println("\r\n");
                System.out.println("===== Request  =====");
                System.out.println(myRequest.requestLine);
                System.out.println(myRequest.headers);
                System.out.println(myRequest.body);
//
                System.out.println("===== Response =====");

                ServerResponse myResponse = new ServerResponse(myRequest, this.routeList);
                System.out.println(myResponse.response);
//
                out.println(myResponse.response);

            }
        }
    }

    public void run() {
        try {
            System.out.println("Starting Server on port " + this.portNumber);
            this.start(this.portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

    private HashMap<String, RouteObject> constructRouteList() {
        HashMap<String, RouteObject> startupList = new HashMap<>();

        ArrayList<String> simpleGetMethods = new ArrayList<>();
        simpleGetMethods.add("GET");
        simpleGetMethods.add("HEAD");
        simpleGetMethods.add("OPTIONS");
        routeInterface simpleGet  = (String firstLine, String headers, String body) ->{
            return firstLine + "\r\n" + headers + "\r\n\r\n";
        };

        startupList.put("/simple_get", new RouteObject( "/simple_get", simpleGetMethods, simpleGet));


        ArrayList<String> simpleGetWithBodyMethods = new ArrayList<>();
        simpleGetWithBodyMethods.add("GET");
        simpleGetWithBodyMethods.add("HEAD");
        simpleGetWithBodyMethods.add("OPTIONS");
        routeInterface simpleGetWithBody = (String firstLine, String headers, String body) -> {
            return firstLine + "\r\n" + headers + "\r\n\r\n" + body;
        };

        startupList.put("/simple_get_with_body", new RouteObject("/simple_get_with_body",
                simpleGetWithBodyMethods, simpleGetWithBody));

        return startupList;
    }

}
