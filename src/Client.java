import javax.swing.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class Client extends NetworkEntity{
    private String IP = "localhost";
    private int port = 5555;
    private Socket socket;
    private final int timeout = 5000;
    private Boolean yourTurn = false;
    private final int yourGoalRow = 12;
    private final int yourGoalCol = 4;
    private final int theirGoalRow = 0;
    private final int theirGoalCol = 4;

    public Client(){
        super("Client");
//        System.out.println("Specify the IP: ");
//        IP = scanner.nextLine();
//        do{
//            System.out.println("Specify the port: ");
//            port = scanner.nextInt();
//        }
//        while(port < 1 || port > 65535);
        if(!connect()) return;
//        gameWindow.canvas.render(false);
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
