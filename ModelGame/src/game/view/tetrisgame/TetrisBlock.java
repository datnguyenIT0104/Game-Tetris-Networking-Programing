
package game.view.tetrisgame;

import java.awt.Color;
import java.io.Serializable;
import java.util.Random;
import model.game.TetrisBlockEncode;

/**
 *
 * @author DatIT
 */
public class TetrisBlock implements Serializable{
    private int[][] shape;
    private Color color;
    private int x, y;
    
    private int[][][] shapes;
    private int currentRotation;
    private Color[] availableColors = { Color.red, Color.yellow, Color.green, Color.blue, Color.orange, new Color(102, 0, 153)};
    
    public TetrisBlock(int[][] shape) {
        this.shape = shape;
//        this.color = color;
        initShapes();
    }
    
    private void initShapes(){
        shapes =new int[4][][];
        for( int i = 0; i < 4; i++){
            int r = shape[0].length;
            int c = shape.length;
            shapes[i] = new int[r][c];
            for( int y = 0; y < r; y++){
                for(int x = 0; x < c; x++){
                    shapes[i][y][x] = shape[ c - x - 1][ y];
                }
            }
            shape = shapes[i];
        }
    }
    
    public int spawn(int gridWeight, int randomColor, int randomCurrentRotation){
        
        Random r = new Random();
        
        // neu la khoi da duoc dinh san thi se theo khoi tiep theo
        if( randomCurrentRotation == 0)
            currentRotation = r.nextInt( shapes.length);
        else
            currentRotation = randomCurrentRotation;
        
        shape = shapes[currentRotation];
        y = -getHeight();
        
        x = r.nextInt(gridWeight - getWeight());
        //x = ( gridWeight - getWeight())/2;
        
//        color = availableColors[ r.nextInt( availableColors.length)];

        color = availableColors[randomColor%6];
        return currentRotation;
    }
    public int[][] getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }
    public int getHeight(){
        return shape.length;
    }
    public int getWeight(){
        return shape[0].length;
    }

    public int getX() {
        return x;
    }
    
    public void setX( int newX){
        x = newX;
    }
    public int getY() {
        return y;
    }
    
    public void setY(int newY){
        y = newY;
    }
    public void moveDown(){ y++; }
    
    public void moveRight(){ x++; }
    
    public void moveLeft(){ x--; }
    
    public void rotate(){
        currentRotation++;
        currentRotation %= 4;
//        if( currentRotation > 3) currentRotation = 0;
        shape = shapes[currentRotation];
    }
    public void unrotate(){
        currentRotation += 3;
        currentRotation %= 4;
        shape = shapes[currentRotation];
    }
    public int getBottomEdge(){
        return y + getHeight();
    }
    
    public int getLeftEdge(){
        return x;
    }
    
    public int getRightEdge(){
        return x + getWeight();
    }
    public void encode(TetrisBlockEncode tbe){
        tbe.setShape(shape);
        tbe.setShapes(shapes);
        int c = 0;
        for (int i = 0; i < availableColors.length; i++) {
            Color item = availableColors[i];
            if( color.equals(item)){
                c = i;
                break;
            }
        }
        tbe.setColor( c);
        tbe.setX(x);
        tbe.setY(y);
    }

    public Color[] getAvailableColors() {
        return availableColors;
    }
    public void restoreGame(TetrisBlockEncode tbe){
        shape = tbe.getShape();
        shapes = tbe.getShapes();
        int c = tbe.getColor();
        color = availableColors[c];
        x = tbe.getX();
        y = tbe.getY();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                System.out.print("" + shape[i][j] + " ");
            }
            System.out.println("");
        }
    }
}
