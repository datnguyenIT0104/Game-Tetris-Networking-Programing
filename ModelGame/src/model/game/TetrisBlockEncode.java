package model.game;

import java.io.Serializable;

/**
 *
 * @author DatIT
 */
public class TetrisBlockEncode implements Serializable{
    private int[][] shape;
    private int color;
    private int x, y;
    
    private int[][][] shapes;
    private int[] availableColor = { 0, 1, 2, 3, 4, 5};

    public TetrisBlockEncode() {
    }

    public TetrisBlockEncode(int[][] shape, int color, int x, int y, int[][][] shapes) {
        this.shape = shape;
        this.color = color;
        this.x = x;
        this.y = y;
        this.shapes = shapes;
    }

    public int[][] getShape() {
        return shape;
    }

    public void setShape(int[][] shape) {
        this.shape = shape;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int[][][] getShapes() {
        return shapes;
    }

    public void setShapes(int[][][] shapes) {
        this.shapes = shapes;
    }

    public int[] getAvailableColor() {
        return availableColor;
    }

    public void setAvailableColor(int[] availableColor) {
        this.availableColor = availableColor;
    }
    
}
