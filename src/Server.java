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
                int endTurn = gameWindow.Loop();
                try {
                    Boolean ourTurnEnd;
                    if(endTurn == -1){
                        ourTurnEnd = true;
                        yourTurn = false;
                    }
                    else{
                        ourTurnEnd = false;
                    }
                    oos.writeObject(gameWindow.getData());
//                    System.out.println("Sent board has: " + gameWindow.getData().getLineNum());
                    oos.writeObject(ourTurnEnd);
                    oos.reset();
//                    System.out.println("Client's turn");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("connection disrupted");
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
                gameWindow.canvas.render(false);
                try {
                    gameWindow.canvas.setBoardState((BoardState) ois.readObject());
//                    System.out.println("Received board state " + gameWindow.canvas.getBoardState().getLineNum());
                    Boolean theirTurnEnd =(Boolean) ois.readObject();
//                    System.out.println("Their turn end: " + theirTurnEnd);
                    if(theirTurnEnd == true) yourTurn = true;
                    if(gameWindow.isGameOver() != (null)){
                        if(gameWindow.isGameOver() == true){
                            System.out.println("Server wins");
                        }
                        else{
                            System.out.println("Client wins");
                        }
                        break;
                    }
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
