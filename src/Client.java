import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class Client {
    private String IP = "localhost";
    private int port = 5555;
    private Scanner scanner =  new Scanner(System.in);
    private static ObjectInputStream ois;
    private static ObjectOutputStream oos;
    private Socket socket;
    private final int timeout = 5000;
    private boolean yourTurn = false;

    public Client(){
//        System.out.println("Specify the IP: ");
//        IP = scanner.nextLine();
//        do{
//            System.out.println("Specify the port: ");
//            port = scanner.nextInt();
//        }
//        while(port < 1 || port > 65535);
        if(!connect()) return;
        GameWindow gameWindow = new GameWindow();
        gameWindow.canvas.render(false);
        while(true){
            if(yourTurn){
                gameWindow.Loop();
                try {
                    oos.writeObject(gameWindow.getData());
                    yourTurn = false;
//                    System.out.println("server's turn");
                } catch (IOException e) {
//                    e.printStackTrace();
                    System.out.println("connection disrupted");
                    break;
                }
            }
            else{
                gameWindow.canvas.render(false);
                //todo: listen for communication;
                try {
                    gameWindow.canvas.setBoardState((BoardState) ois.readObject());
                    yourTurn = true;
                    yourTurn = true;
                } catch (IOException e) {
//                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
                }
                continue;
            }
        }

    }

    private boolean connect(){
        try{
            socket = new Socket();
            SocketAddress ska = new InetSocketAddress(IP, port);
            socket.connect(ska, timeout);
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (SocketTimeoutException e){
            System.out.println("Connection timed out");
            return false;
        } catch (IOException e){
            System.out.println("Unable to connect to server");
            return false;
        }
        System.out.println("Successfully connected to the server: "+IP);
        return true;
    }


    public static void main(String[] args) {
        Client cli1 = new Client();
    }
}