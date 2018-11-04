import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class NetworkEntity {
    protected static ObjectInputStream ois;
    protected static ObjectOutputStream oos;
    protected GameWindow gameWindow;
    public NetworkEntity(String title){
        gameWindow =  new GameWindow();
        gameWindow.setTitle(title);
    }

    public Boolean runAndSendState(){
        int endTurn = gameWindow.Loop();
        try {
            Boolean ourTurnEnd;
            if(endTurn == -1){
                ourTurnEnd = true;
            }
            else{
                ourTurnEnd = false;
            }
            oos.writeObject(gameWindow.getData());
            oos.writeObject(ourTurnEnd);
            oos.reset();
            return !ourTurnEnd; // returns true if it is still your turn
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("connection disrupted");
        }
        return null;
    }

    // returns true if their turn has ended, false if not and null if error occurred
    public Boolean runAndExpectData(){
        gameWindow.canvas.render(false); //render spectator's view
        Boolean theirTurnEnd = null;
        try {
            gameWindow.canvas.setBoardState((BoardState) ois.readObject());
            theirTurnEnd = (Boolean) ois.readObject();
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            System.out.println("Error occurred");
        }
        return theirTurnEnd;
    }
}
