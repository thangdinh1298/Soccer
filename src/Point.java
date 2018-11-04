import java.awt.*;
import java.io.Serializable;

public class Point implements Serializable{
    private int x;
    private int y;
    private int row = -1, col = -1;
    private boolean outOfBound = false;
    public final int dotDiameter = 10;

    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y, int row, int col){
        this.x = x;
        this.y = y;
        this.row = row;
        this.col = col;
    }


    public void setOutOfBound(boolean outOfBound) {
        this.outOfBound = outOfBound;
    }

    public boolean isOutOfBound() {
        return outOfBound;
    }
    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
    public int getX(){
        return x;
    }
    public  int getY(){
        return y;
    }

    public double distanceTo(Point that){
        double distance = Math.sqrt(((this.x - that.x) * (this.x - that.x)) + ((this.y - that.y) * (this.y - that.y)));
        return distance;
    }

    public void render(Graphics g){
        g.setColor(Color.BLACK);
        g.fillOval(x - dotDiameter/2,y - dotDiameter/2,dotDiameter, dotDiameter);
    }

    public void render(Graphics g, int diameter){
        g.setColor(Color.YELLOW);
        g.fillOval(x - diameter/2, y - diameter/2, diameter, diameter);
    }

    @Override
    public String toString() {
        return row + ": " + col;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != Point.class) return false;
        Point p = (Point) obj;
        return (p.getCol() == this.getCol() && p.getRow() == this.getRow());
    }
}
