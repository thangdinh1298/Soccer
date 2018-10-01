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


    private int ballRow = 1;
    private int ballCol = 1;

    private Point[][] points = new Point[row][col];
    private ArrayList<Line>[][] lineTo= new ArrayList[row][col];
//    private ArrayList<Line> lines = new ArrayList<>();
    private ArrayList<Point> reachablePoints = new ArrayList<>();


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
        for(int i = 0; i < col; i++){
            if(i==3) i = 6;
            points[0][i].setOutOfBound(true);
            points[row-1][i].setOutOfBound(true);
        }
        initField();
        computeReachables();
    }

    public void initField(){
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
//        lines.add(new Line(points[1][0], points[11][0]));
//        lines.add(new Line(points[1][8], points[11][8]));
//        lines.add(new Line(points[1][0], points[1][3]));
//        lines.add(new Line(points[1][5], points[1][8]));
//        lines.add(new Line(points[11][0], points[11][3]));
//        lines.add(new Line(points[11][5], points[11][8]));
//        //goal above
//        lines.add(new Line(points[1][5], points[0][5]));
//        lines.add(new Line(points[1][3], points[0][3]));
//        lines.add(new Line(points[0][3], points[0][5]));
//        // goal below
//        lines.add(new Line(points[11][3], points[12][3]));
//        lines.add(new Line(points[11][5], points[12][5]));
//        lines.add(new Line(points[12][3], points[12][5]));
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
//        if(ballRow - 1 >= 0){
//            if(isReachable(points[ballRow][ballCol], ))
//            reachablePoints.add(points[ballRow-1][ballCol]);
//        }
//        if(ballRow + 1 < row){
//            if(isReachable(points[ballRow][ballCol], ))
//            reachablePoints.add(points[ballRow+1][ballCol]);
//        }
//        if(ballCol - 1 >= 0){
//            if(isReachable(points[ballRow][ballCol], ))
//            reachablePoints.add(points[ballRow][ballCol-1]);
//        }
//        if(ballCol + 1 < col){
//            if(isReachable(points[ballRow][ballCol], ))
//            reachablePoints.add(points[ballRow][ballCol+1]);
//        }
//        if(ballRow - 1 >= 0 && ballCol - 1 >= 0){
//            if(isReachable(points[ballRow][ballCol], ))
//            reachablePoints.add(points[ballRow-1][ballCol-1]);
//        }
//        if(ballRow + 1 < row && ballCol + 1 < col){
//            if(isReachable(points[ballRow][ballCol], ))
//            reachablePoints.add(points[ballRow+1][ballCol+1]);
//        }
//        if(ballRow - 1 >= 0 && ballCol + 1 < col){
//            if(isReachable(points[ballRow][ballCol], ))
//            reachablePoints.add(points[ballRow - 1][ballCol + 1]);
//        }
//        if(ballRow + 1 < row && ballCol - 1 >= 0){
//            if(isReachable(points[ballRow][ballCol], ))
//            reachablePoints.add(points[ballRow+1][ballCol-1]);
//        }
        if(ballRow == 1) {
            if(ballCol == goalCol || ballCol == goalCol - 1 || ballCol == goalCol + 1){
                reachablePoints.add(points[0][4]);
            }
        }
        if(ballRow == row - 2) {
            if(ballCol == goalCol || ballCol == goalCol - 1 || ballCol == goalCol + 1){
                reachablePoints.add(points[goalCol][4]);
            }
        }
        int topLeftRow = (ballRow-1 > 0) ? (ballRow-1):(ballRow);
        int topLeftCol = (ballCol-1 >= 0) ? (ballCol-1):(ballCol);
        int bottomRightRow = (ballRow < row) ? (ballRow+1):(ballRow);
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

    public boolean checkHit(MouseEvent e){
        boolean isValid = false;
        Point q = new Point(e.getX() - xOffset, e.getY() - yOffset);
        for(Point p: reachablePoints){
            if(q.distanceTo(p) <= ballDiameter){
                setBallPos(p.getRow(), p.getCol());
                isValid = true;
                break;
            }
        }
        if(isValid){
            computeReachables();
        }
        return isValid;
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

//        for(Line line: lines){ //render field lines
//            line.render(g);
//        }
        for(int i = 0; i < lineTo.length; i++){
            for(int j = 0; j < lineTo[0].length; j++){
                for(Line line: lineTo[i][j]){
                    line.render(g);
                }
            }
        }

        Point ball = points[this.ballRow][this.ballCol];
        g.fillOval(ball.getX() - ballDiameter/2,ball.getY() - ballDiameter/2, ballDiameter, ballDiameter);
//        Point p = new Point(80,80);
//        p.render(g);
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
