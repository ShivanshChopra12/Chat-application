import java.io.*;
import java.net.*;

class Server{

    ServerSocket server; //ServerSocket is a java.net class provides system-independent implementation of the server side of a client/server socket connection.
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    //Constructor
    public Server(){
        try {
            server = new ServerSocket(7777); //defining port
            System.out.println("server is ready to to accept connection");
            System.out.println("waiting...");
            socket=server.accept();

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void startReading(){
        // thread-read karke deta rahega
        Runnable r1=()->{  //Using Lambda function
            System.out.println("reader started..");

            try {
                while(true){
                
                    String msg = br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("Client terminated the chat");

                        socket.close();
                        break;
                    }
                    System.out.println("Client : "+msg);
                
                
            }
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("Connection Closed");
            }
    
        };

        new Thread(r1).start();
    }

    public void startWriting(){
        // thread- data ko user se lega and then send karega client tak
        Runnable r2 = () ->{
            System.out.println("Writer Started");

            try {
                while(!socket.isClosed()){
                
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine(); 
                    out.println(content);
                    out.flush();

                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }

                }
                System.out.println("Connection Closed");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("this is server.. going to start server");
        new Server();
    }
}
