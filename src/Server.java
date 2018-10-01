import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private String IP;
    private final int port = 5555;
    private static ObjectInputStream ois;
    private static ObjectOutputStream oos;
    private Socket socket;
    private ServerSocket serverSocket;
    private boolean yourTurn = true;

    public Server(){
        if(create()) System.out.println("successfully created the server");
        listenForConnection();
        GameWindow gameWindow = new GameWindow();
        while(true){
            if(yourTurn){
                gameWindow.Loop();
                try {
                    oos.writeObject(gameWindow.getData());
                    yourTurn = false;
//                    System.out.println("Client's turn");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("connection disrupted");
                    break;
                }
            }
            else{
                gameWindow.canvas.render(false);
                try {
                    gameWindow.canvas.setBoardState((BoardState) ois.readObject());
                    yourTurn = true;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                continue;
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

    void listenForConnection(){
        while(true){
            try{
                socket = serverSocket.accept();
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e){
                continue;
            }
            System.out.println("Connection established with: " + socket.getInetAddress());
            break;
        }
    }


    public static void main(String[] args) {
        Server serv1 = new Server();
    }
}
