package game.view.tetrisgame;

import game.view.tetrisblocks.IShape;
import game.view.tetrisblocks.JShape;
import game.view.tetrisblocks.LShape;
import game.view.tetrisblocks.OShape;
import game.view.tetrisblocks.SShape;
import game.view.tetrisblocks.TShape;
import game.view.tetrisblocks.ZShape;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author DatIT
 */
public class BlockArea extends JPanel{

    private int gridCellSize;
    private int gridColumns;
    private int gridRows;
    private TetrisBlock block ;
    
    private TetrisBlock[] blocks;
    private int[] random;
    
    public BlockArea(JPanel placeHolder, int column) {
//        placeHolder.setVisible(false);
        this.setBounds(placeHolder.getBounds());
        this.setBackground(placeHolder.getBackground());
        this.setBorder(placeHolder.getBorder());
        
        gridColumns = column;
        gridCellSize = this.getBounds().width/ gridColumns;
        gridRows = this.getBounds().height/ gridCellSize;
        
        
        blocks = new TetrisBlock[]{
                                        new IShape(),
                                        new LShape(),
                                        new JShape(),
                                        new OShape(),
                                        new SShape(),
                                        new TShape(),
                                        new ZShape()
        };
        random = new int[10000];
        
    }
    public int spawnBlock(int index){
        int currentRotate;
        block = blocks[ random[index]];
        currentRotate = block.spawn(gridColumns, index, 0);
        repaint();
        
        return currentRotate;
    }
    public void drawBlock(Graphics g){
        int h = block.getHeight();
        int w = block.getWeight();
        int[][] shape = block.getShape();
        Color color = block.getColor();
        
        for( int row = 0; row < h; row++){
            for (int col = 0; col < w; col++) {
                if (shape[row][col] == 1) {
                    int x = ( (gridColumns - block.getWeight())/2 + col)*gridCellSize;// o giua
                    int y = (  row + 1)*gridCellSize;
                    
                    drawGridSqaure(g, color, x, y);
                }
            }
        }
    }
    private void drawGridSqaure(Graphics g, Color color, int x, int y){// vẽ ô vuông
        g.setColor(color);
        g.fillRect(x , y, gridCellSize, gridCellSize);
        g.setColor(Color.black);
        g.drawRect( x , y, gridCellSize, gridCellSize);
    }
    public int[] getRandom() {
        return random;
    }

    public void setRandom(int[] random) {
        this.random = random;
    }
    @Override
    protected void paintComponent(Graphics g){
        if( block == null)
            return;
        super.paintComponent(g);
        
        drawBlock(g);
    }
}
