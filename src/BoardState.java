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
    private final int goalCol = 4;
    private final int ballDiameter = 20;


    private Boolean won = null;

    private int ballRow = 7;
    private int ballCol = 4;

    private Point[][] points = new Point[row][col];
    private ArrayList<Line>[][] lineTo= new ArrayList[row][col];
    private ArrayList<Point> reachablePoints = new ArrayList<>();

    public int getLineNum(){
        int num = 0;
        for(int i = 0; i < lineTo.length; i++){
            for(int j = 0; j < lineTo[0].length; j++){
                for(Line line: lineTo[i][j]){
                    num++;
                }
            }
        }
        return num;
    }

    public BoardState(){
        initField();
        computeReachables();
    }



    public void initField(){
        int x = startX, y = startY;
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                points[i][j] = new Point(x,y,i,j);
                x += gap;
            }
            x = startX;
            y += gap;
        }
        for(int i = 0; i < col; i++){
            if(i==3) i = 6;
            points[0][i].setOutOfBound(true);
            points[row-1][i].setOutOfBound(true);
        }
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                lineTo[i][j] = new ArrayList<Line>();
            }
        }
        Line line;
        for(int i = 1; i < row - 2; i++){
            line = new Line(points[i][0], points[i+1][0]);
            lineTo[i][0].add(line);
            lineTo[i+1][0].add(line);

            line = new Line(points[i][col-1], points[i+1][col-1]);
            lineTo[i][col-1].add(line);
            lineTo[i+1][col-1].add(line);
        }
        for(int i = 0; i < col-1; i++){
            if(i > 2 && i < 5){ //todo: hard-coded
                line = new Line(points[0][i], points[0][i+1]);
                lineTo[0][i].add(line);
                lineTo[0][i+1].add(line);

                line = new Line(points[row-1][i], points[row-1][i+1]);
                lineTo[row-1][i].add(line);
                lineTo[row-1][i+1].add(line);
            }
            else{
                line = new Line(points[1][i], points[1][i+1]);
                lineTo[1][i].add(line);
                lineTo[1][i+1].add(line);

                line = new Line(points[row-2][i], points[row-2][i+1]);
                lineTo[row-2][i].add(line);
                lineTo[row-2][i+1].add(line);
            }
            if(i == 3 || i == 5){
                 line = new Line(points[1][i], points[0][i]);
                 lineTo[1][i].add(line);
                 lineTo[0][i].add(line);

                 line = new Line(points[row-2][i], points[row-1][i]);
                 lineTo[row-2][i].add(line);
                 lineTo[row-1][i].add(line);
            }
        }
    }


    private void setBallPos(int row, int col){
        this.ballRow  = row;
        this.ballCol = col;

    }

    private boolean isReachable(Point src, Point dest){
        if(dest.isOutOfBound()) return false;
        Line line = new Line(src, dest);
        for(Line destLine: lineTo[dest.getRow()][dest.getCol()]){
            if(destLine.equals(line)) return false;
        }
        return true;
    }

    private void computeReachables(){
        reachablePoints.clear();
        if(ballRow == 1) {
            if(ballCol == goalCol || ballCol == goalCol - 1 || ballCol == goalCol + 1){
                reachablePoints.add(points[0][goalCol]);
            }
        }
        if(ballRow == row - 2) {
            if(ballCol == goalCol || ballCol == goalCol - 1 || ballCol == goalCol + 1){
                reachablePoints.add(points[row-1][goalCol]);
            }
        }
        int topLeftRow = (ballRow-1 > 0) ? (ballRow-1):(ballRow);
        int topLeftCol = (ballCol-1 >= 0) ? (ballCol-1):(ballCol);
        int bottomRightRow = (ballRow+1 < row) ? (ballRow+1):(ballRow);
        int bottomRightColumn = (ballCol+1 < col)? (ballCol+1):(ballCol);
        for(int i = topLeftRow; i <= bottomRightRow; i++){
            for(int j = topLeftCol; j <= bottomRightColumn; j++){
                if(i == ballRow && j == ballCol) continue;
                else{
                    if(isReachable(points[ballRow][ballCol], points[i][j]))
                        reachablePoints.add(points[i][j]);
                }
            }
        }
    }

    private boolean visited(int row, int col){
        return !lineTo[row][col].isEmpty();
    }

    public int checkHit(MouseEvent e){
        boolean isValid = false;
        int endTurn = 0;
        Point q = new Point(e.getX() - xOffset, e.getY() - yOffset);
        for(Point p: reachablePoints){
            if(q.distanceTo(p) <= ballDiameter){
                isValid = true;

                if(visited(p.getRow(), p.getCol())) endTurn = 1; //điểm đã được thăm, được đi tiếp
                else endTurn = -1; //điểm chưa được thăm, hết lượt

                Line line = new Line(points[ballRow][ballCol], p);
                lineTo[ballRow][ballCol].add(line);
                lineTo[p.getRow()][p.getCol()].add(line);

                setBallPos(p.getRow(), p.getCol());

                if(p.equals(points[0][4])){
                    won = false;
                }
                else if(p.equals(points[row-1][4])){
                    won = true;
                }
                break;
            }
        }
        if(isValid == true && won == null){ //chỉ tính lại các điểm xung quanh nếu chọn được bước đi hợp lệ
            computeReachables();
        }
        return endTurn;
    }

    public Boolean isGameOver(){
        return won;
    }

    public void render(Graphics g, boolean myTurn){
        for(Point[] pa: points){ //render the points in the field
            for(Point p: pa){
                if(!p.isOutOfBound())
                p.render(g);
            }
        }
        if(myTurn) { //render valid spots
            for(Point p: reachablePoints){
                p.render(g, ballDiameter);
            }
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for(int i = 0; i < lineTo.length; i++){
            for(int j = 0; j < lineTo[0].length; j++){
                for(Line line: lineTo[i][j]){
                    line.render(g);
                }
            }
        }

        Point ball = points[this.ballRow][this.ballCol];
        g.fillOval(ball.getX() - ballDiameter/2,ball.getY() - ballDiameter/2, ballDiameter, ballDiameter);
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
        public void render(Graphics g, Color color){
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT
                    , BasicStroke.JOIN_BEVEL, 0, new float[]{9},
                    0));
        }
        @Override
        public boolean equals(Object obj) {
            if(obj.getClass() != Line.class) return false;
            else {
                Line that = (Line) obj;
                return (this.a.equals(that.a) && this.b.equals(that.b)) ||
                        (this.a.equals(that.b) && this.b.equals(that.a));
            }
        }
    }

    public int getHeight(){
        return this.row;
    }
    public int getWidth(){
        return this.col;
    }
}
