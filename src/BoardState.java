import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;

public class BoardState implements Serializable{
    private final int row = 13;
    private final int col = 9;
    private final int startX = 80;
    private final int startY = 80;
    private final int gap = 50;
    private final int xOffset = 3;
    private final int yOffset = 25;

    private final int ballDiameter = 20;


    private int ballRow = 6;
    private int ballCol = 4;

    public Point[][] points = new Point[row][col];
    private ArrayList<Line> lines = new ArrayList<>();
    private ArrayList<Point> surroundingPoints = new ArrayList<>();


    public BoardState(){
        int x = startX, y = startY;
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                points[i][j] = new Point(x,y,i,j);
                x += gap;
            }
            x = startX;
            y += gap;
        }
        initField();
        computeSurround();
    }

    public void initField(){
        lines.add(new Line(points[1][0], points[11][0]));
        lines.add(new Line(points[1][8], points[11][8]));
        lines.add(new Line(points[1][0], points[1][3]));
        lines.add(new Line(points[1][5], points[1][8]));
        lines.add(new Line(points[11][0], points[11][3]));
        lines.add(new Line(points[11][5], points[11][8]));
        //goal above
        lines.add(new Line(points[1][5], points[0][5]));
        lines.add(new Line(points[1][3], points[0][3]));
        lines.add(new Line(points[0][3], points[0][5]));
        // goal below
        lines.add(new Line(points[11][3], points[12][3]));
        lines.add(new Line(points[11][5], points[12][5]));
        lines.add(new Line(points[12][3], points[12][5]));
    }


    private void setBallPos(int row, int col){
        this.ballRow  = row;
        this.ballCol = col;

    }

    private void computeSurround(){
        surroundingPoints.clear();
        if(ballRow - 1 >= 0) surroundingPoints.add(points[ballRow-1][ballCol]);
        if(ballRow + 1 < row) surroundingPoints.add(points[ballRow+1][ballCol]);
        if(ballCol - 1 >= 0) surroundingPoints.add(points[ballRow][ballCol-1]);
        if(ballCol + 1 < col) surroundingPoints.add(points[ballRow][ballCol+1]);
        if(ballRow - 1 >= 0 && ballCol - 1 >= 0) surroundingPoints.add(points[ballRow-1][ballCol-1]);
        if(ballRow + 1 < row && ballCol + 1 < col) surroundingPoints.add(points[ballRow+1][ballCol+1]);
        if(ballRow - 1 >= 0 && ballCol + 1 < col) surroundingPoints.add(points[ballRow - 1][ballCol + 1]);
        if(ballRow + 1 < row && ballCol - 1 >= 0) surroundingPoints.add(points[ballRow+1][ballCol-1]);
    }

    public boolean checkHit(MouseEvent e){
        boolean isValid = false;
        Point q = new Point(e.getX() - xOffset, e.getY() - yOffset);
        for(Point p: surroundingPoints){
            if(q.distanceTo(p) <= ballDiameter){ //todo: fix hitbox
                setBallPos(p.getRow(), p.getCol());
                isValid = true;
                break;
            }
        }
        if(isValid){
            computeSurround();
        }
        return isValid;
    }

    public void render(Graphics g, boolean myTurn){
        for(Point[] pa: points){
            for(Point p: pa){
                p.render(g);
            }
        }
        if(myTurn) {
            for(Point p: surroundingPoints){
                p.render(g, ballDiameter);
            }
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for(Line line: lines){
            line.render(g);
        }

        Point ball = points[this.ballRow][this.ballCol];
        g.fillOval(ball.getX() - ballDiameter/2,ball.getY() - ballDiameter/2, ballDiameter, ballDiameter);
        Point p = new Point(80,80);
        p.render(g);
    }


    public class Line implements Serializable{
        private Point a;
        private Point b;
        public Line(Point a, Point b){
            this.a = a;
            this.b = b;
        }
        public void render(Graphics g){
            g.setColor(Color.BLACK);
            g.drawLine(a.getX(),a.getY(),b.getX(),b.getY());
        }
    }

    public int getHeight(){
        return this.row;
    }
    public int getWidth(){
        return this.col;
    }
}
