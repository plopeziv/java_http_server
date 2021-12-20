/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package server;
import java.net.*;
import java.io.*;

public class GreetServer implements Runnable {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    int portNumber;

    public GreetServer(int port){
        this.portNumber = port;
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);

        while (true){
            try(Socket clientSocket = serverSocket.accept()){
                System.out.println("Client socket " + clientSocket.toString());

                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                StringBuilder requestLines = new StringBuilder();

                String requestLine;
                requestLine = in.readLine();
                while (!requestLine.isEmpty()) {
                    requestLines.append(requestLine + "\r\n");
                    requestLine = in.readLine();
                }

                System.out.println("\r\n");
                System.out.println("===== Request  =====");
                System.out.println(requestLines);

                System.out.println("===== Response =====");
                String returnString = this.returnMessageCreator(requestLines.toString());
                System.out.println(returnString);
                System.out.println("\r\n");

                out.println(returnString);

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

    private String returnMessageCreator (String requestLines) {
        String lineBreak [] = requestLines.split("\r\n");
        String lineOne [] = lineBreak[0].split(" ");
        String response;

        switch(lineOne[0]){
            case "GET":
                switch(lineOne[1]) {
                    case "/redirect":
                        response  =  lineOne[2] + " 301 Moved Permanently\r\n";
                        response = response.concat("Location: http://0.0.0.0:5000/simple_get\r\n");
                        return response;

                    case "/head_request":
                        response  = lineOne[2] + " 405 Method Not Allowed\r\n";
                        response = response.concat("Allow: HEAD, OPTIONS\r\n");
                        return response;

                    case "/simple_get":
                        response  = lineOne[2] + " 200 OK\r\n";
                        return response;

                    case "/simple_get_with_body":
                        response = lineOne[2] + " 200  OK\r\n";
                        response = response.concat("Content-Type: text\r\n");
                        response = response.concat("\r\n");
                        response = response.concat("\"Hello world\"");
                        return response;

                    case "/not_found_resource":
                        response = lineOne[2] + " 404 Not Found\r\n";
                        return response;

                    default:
                        response = "Unexpected value: " + lineOne[1] + "\r\n";
                        return response;
                }

            case "POST":
                switch(lineOne[1]){
                    case "/echo_body":
                        response = lineOne[2] + " 200 OK\r\n";
                        response = response.concat("\r\n");
                        response  = response.concat("This will echo the body\r\n");
                        return response;

                    default:
                        response = "Unexpected value: " + lineOne[1] + "\r\n";
                        return response;
                }

            case  "HEAD":
                switch (lineOne[1]){
                    case "/simple_get":
                        response = lineOne[2] + " 200 OK\r\n";
                        return response;

                    case "/head_request":
                        response = lineOne[2] + " 200 OK\r\n";
                        return response;

                    default:
                        response = "Unexpected value: " + lineOne[1] +"\r\n";
                        return response;
                }
                
            case "OPTIONS":
                switch(lineOne[1]){
                    case "/method_options":
                        response = lineOne[2] + " 200 OK\r\n";
                        response = response.concat("Allow: GET, HEAD, OPTIONS\r\n");
                        return response;

                    case "/method_options2":
                        response = lineOne[2] + " 200 OK\r\n";
                        response = response.concat("Allow: GET, HEAD, OPTIONS, PUT, POST\r\n");
                        return response;

                    default:
                        response = "Unexpected value: " + lineOne[1] +"\r\n";
                        return response;
                }

            default:
                response = "Unexpected value: " + lineOne[0] +"\r\n";
                return response;
        }

    }
}
