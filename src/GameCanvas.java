import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class GameCanvas extends JPanel {

    private final int WIDTH = 600;
    private final int HEIGHT = 800;

    private BoardState boardState = null;

    private BufferedImage backBuffer;
    private BufferedImage img =  null;
    private Graphics graphics;
    public GameCanvas(BoardState boardState){
        this.boardState = boardState;
        this.backBuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        graphics = backBuffer.getGraphics();
        graphics.setColor(Color.darkGray);
    }

    public void setBoardState(BoardState boardState){
        this.boardState = boardState;
    }

    public BoardState getBoardState(){
        return this.boardState;
    }

    public int getClick(MouseEvent e){
        return boardState.checkHit(e);
    }



    public void render(boolean myTurn){
        try{
            img = ImageIO.read(new File("assets/back.jpg"));
        } catch (IOException e){
            e.printStackTrace();
        }
        graphics.drawImage(img, 0, 0, null);
        boardState.render(graphics, myTurn);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(backBuffer, 0, 0, null);
    }
}
