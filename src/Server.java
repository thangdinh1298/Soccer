import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends NetworkEntity{
    private String IP;
    private final int port = 5555;
    private Socket socket;
    private ServerSocket serverSocket;
    private Boolean yourTurn = true;
    private final int yourGoalRow = 0;
    private final int yourGoalCol = 4;
    private final int theirGoalRow = 12;
    private final int theirGoalCol = 4;


    public Server(){
        super("Server");
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
                if(checkWinCondition(yourTurn)){
                    gameWindow.canvas.render(false);
                    break;
                }
            }
            else{
                yourTurn = super.runAndExpectData();
                if(yourTurn == null){
                    break;
                }
                if(checkWinCondition(yourTurn)){
                    gameWindow.canvas.render(false);
                    break;
                }
            }
        }
    }

    private boolean checkWinCondition(Boolean yourTurn){
        JPanel p = new JPanel();
        JLabel l = new JLabel();
        boolean ended = false;
        if(gameWindow.getBallRow() == yourGoalRow && gameWindow.getBallCol() == yourGoalCol) {
            l = new JLabel("You win");
            ended = true;
        }
        else if(gameWindow.getBallRow() == theirGoalRow && gameWindow.getBallCol() == theirGoalCol){
            l = new JLabel("You lose");
            ended = true;
        }
        if(yourTurn == true && gameWindow.getNumReachablePoints() == 0){
            l = new JLabel("You lose");
            ended = true;
        }
        else if(yourTurn == false && gameWindow.getNumReachablePoints() == 0){
            l = new JLabel("You win");
            ended =  true;
        }
        if(ended == true){
            p.add(l);
            gameWindow.add(p);
            gameWindow.setVisible(true);
        }
        return ended;
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
