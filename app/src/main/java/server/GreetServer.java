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
    private InputStream in;

    int portNumber;
    HashMap<String, Route> routeMap;


    public GreetServer(int port){
        this.portNumber = port;
        this.routeMap =  new RouteMap().startupMap;
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);

        while (true){
            try(Socket clientSocket = serverSocket.accept()){
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = clientSocket.getInputStream();

                ServerRequest myRequest = new ServerRequest(in);

                System.out.println("\r\n");
                System.out.println("===== Request  =====");
                System.out.println(myRequest.requestLine);
                System.out.println(myRequest.headers);
                System.out.println(myRequest.body);
                System.out.println("===== Response =====");

                ServerResponse myResponse = new ServerResponse(myRequest, this.routeMap);
                System.out.print(myResponse.response);

                out.printf(myResponse.response);

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
}
