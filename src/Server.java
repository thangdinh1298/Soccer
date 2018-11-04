import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends NetworkEntity{
    private String IP;
    private final int port = 5555;
    private Socket socket;
    private ServerSocket serverSocket;
    private Boolean yourTurn = true;

    public Server(){
        super();
        if(!create()){
            System.out.println("Could not create server");
            return;
        }
        System.out.println("successfully created the server");
        if(!listenForConnection()){
            System.out.println("Could not find any connections");
            return;
        }
        while(true){
            if(yourTurn){
                yourTurn = super.runAndSendState();
                if(yourTurn == null){
                    break;
                }
                if(gameWindow.isGameOver() !=(null)) {
                    if(gameWindow.isGameOver()== true){
                        System.out.println("Server wins");
                    }
                    else{
                        System.out.println("Client wins");
                    }
                    break;
                }
            }
            else{
                yourTurn = super.runAndExpectData();
                if(yourTurn == null){
                    break;
                }
                if(gameWindow.isGameOver() != (null)){
                    if(gameWindow.isGameOver() == true){
                        System.out.println("Server wins");
                    }
                    else{
                        System.out.println("Client wins");
                    }
                    break;
                }

            }
        }
    }

    private boolean create(){
        try{
            serverSocket = new ServerSocket(port);
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean listenForConnection(){
//        while(true){
            try{
                socket = serverSocket.accept();
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e){
//                continue;
                return false;
            }
            System.out.println("Connection established with: " + socket.getInetAddress());
//            break;
            return true;
//        }
    }


    public static void main(String[] args) {
        Server serv1 = new Server();
    }
}
