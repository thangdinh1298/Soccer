import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameWindow extends JFrame {

    private final int WIDTH = 600;
    private final int HEIGHT = 800;
    private boolean endTurn = false;


    GameCanvas canvas;
    public GameWindow(){
        super("Paper Soccer");
        BoardState initState = new BoardState();
        canvas = new GameCanvas(initState);
        this.setSize(WIDTH, HEIGHT);
        this.setResizable(false);
        this.setContentPane(canvas);
        this.setVisible(true);


        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                endTurn = canvas.getClick(e);
//                System.out.println("clicked");
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public void Loop(){
        endTurn = false; // endturn is toggle to true if a valid mouseclick event is made
        while(endTurn == false){
            canvas.render(true);
        }

    }

    public BoardState getData(){
        return this.canvas.getBoardState();
    }
    public static void main(String[] args) {

    }


}
