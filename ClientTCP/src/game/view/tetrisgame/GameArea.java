
package game.view.tetrisgame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;
import javax.swing.JPanel;
import game.view.tetrisblocks.*;
import java.util.ArrayList;

/**
 *
 * @author DatIT
 */
public class GameArea extends JPanel{
    private int gridRows;
    private int gridColumns;
    private int gridCellSize;
    private Color[][] background;
    
    
    private TetrisBlock block;
    private TetrisBlock[] blocks;
    private int[] random;
    private SoundTetris soundTetris;
    
    public GameArea( JPanel placeHolder, int column){
//        placeHolder.setVisible(false);
        this.setBounds( placeHolder.getBounds());
        this.setBackground( placeHolder.getBackground());
        this.setBorder(placeHolder.getBorder());
        
        // tính các giá trị của lưới ô
        gridColumns = column;
        gridCellSize = this.getBounds().width/ gridColumns;
        gridRows = this.getBounds().height/ gridCellSize;
        // khoi tao am thanh
        soundTetris = new SoundTetris();

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
//        createRamdomBlock();
    }
    
    public void initBackgroundArray(){
        background =new Color[gridRows][gridColumns];
    }
    public void spawnBlock(int index, int randomCurrentRotation){// sinh các khối
        
        block = blocks[ random[index]];
        block.spawn(gridColumns, index, randomCurrentRotation);
    }
    
    public boolean isBlockOutOfBound(){
        if( block.getY() < 0){
            block = null;
            return true;
        }
        return false;
    }
    
    public boolean moveBlockDown(){// di chuyển khối
        if( !checkBottom()){
            
            return false;
        }
        block.moveDown();
        repaint();// vẽ lại
        return true;
    }
    
    public void moveBlockRight(){// di chuyển sang phải nếu còn di chuyển đk
        if(  block == null ) return;
        if( !checkRight() ) return;
        
        block.moveRight();
        repaint();
    }
    
    public void moveBlockLeft(){
        if(  block == null ) return;
        if( !checkLeft()) return;
        
        block.moveLeft();
        repaint();
    }
    
    public void dropBlock(){// rơi khối
        if(  block == null ) return;
//        while( checkBottom() == true ){
//            block.moveDown();
//        }
        if(checkBottom())
            block.moveDown();
        repaint();
    }
    
    public void rotateBlock(){
        if( block == null) return;
        int oldX = block.getX();
        int oldY = block.getY();
        block.rotate();
        
        
        // điều chỉnh vị trí k vượt qua biên
        if( block.getX() < 0) block.setX(0);
        if( block.getRightEdge() >= gridColumns) block.setX( gridColumns - block.getWeight());
        if( block.getBottomEdge() >= gridRows) block.setY( gridRows - block.getHeight());
        
        // kiem tra xem co cham vao khoi backgroud k
        int[][] shape = block.getShape();
        int w = block.getWeight();
        int h = block.getHeight();
        
        boolean overlap = false;
        for(int row = 0; row < h; row++){
            
            for(int col = 0; col < w; col++){
                int x = col + block.getX();
                int y = row + block.getY();
                if( y < 0) break;
                if( background[y][x] != null){
                    overlap = true;
                    break;
                }
            }
            if( overlap)
                break;
        }
        if( overlap){
            block.setX(oldX);
            block.setY(oldY);
            block.unrotate();
        }
        
        if( block.getX() < 0) block.setX(0);
        if( block.getRightEdge() >= gridColumns) block.setX( gridColumns - block.getWeight());
        if( block.getBottomEdge() >= gridRows) block.setY( gridRows - block.getHeight());
        
        repaint();
    }
    private boolean checkBottom(){// kiếm tra xem đã đến đáy hoặc background hay chưa
        if( block.getBottomEdge() == gridRows ) return false;
        
        int[][] shape = block.getShape();
        int w = block.getWeight();
        int h = block.getHeight();
        for( int col = 0; col < w; col++){
            for( int row = h - 1; row >= 0; row--){
                if( shape[row][col] != 0){
                    int x = col + block.getX();
                    int y = row + block.getY() + 1;
                    if( y < 0) break;
                    if( x > gridColumns) break;
                    if( background[y][x] != null) return false;
                    break;
                }
            }
        }
        return true;
    }
    
    private boolean checkLeft(){// kiêm tra đã ở cạnh trái hay chưa
        if( block.getLeftEdge() == 0) return false;
        
        int[][] shape = block.getShape();
        int w = block.getWeight();
        int h = block.getHeight();
        for( int row = 0; row < h; row++){
            for( int col = 0; col < w; col++){
                if( shape[row][col] != 0){
                    int x = col + block.getX()-1;
                    int y = row + block.getY();
                    if( y < 0 ) break;
                    if( background[y][x] != null) return false;
                    break;
                }
            }
        }
        return true;
    }
    
    private boolean checkRight(){// kiểm tra đã ở cạnh phải hay chưa
        if( block.getRightEdge() == gridColumns) return false;
        
        int[][] shape = block.getShape();
        int w = block.getWeight();
        int h = block.getHeight();
        for( int row = 0; row < h; row++){
            for( int col = w - 1; col >= 0; col--){
                if( shape[row][col] != 0){
                    int x = col + block.getX()+1;
                    int y = row + block.getY();
                    if( y < 0 ) break;
                    if( background[y][x] != null) return false;
                    break;
                }
            }
        }
        return true;
    }
    
    private void drawBlock(Graphics g){// vẽ các khối
        int h = block.getHeight();
        int w = block.getWeight();
        
        Color c = block.getColor();
        
        int[][] shape = block.getShape();
        
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if( shape[i][j] == 1){
                    int x = ( block.getX() + j)*gridCellSize;
                    int y = ( block.getY()+ i)*gridCellSize;
                    
                    drawGridSqaure(g, c, x, y);
                }
            }
        }
    }
    
    
    private void drawBackground(Graphics g){// vẽ khối cố định
        Color color;
        
        for( int r = 0; r < gridRows; r++){
            for( int c = 0; c < gridColumns; c++){
                color = background[r][c];
                if( color != null){
                    int x = c*gridCellSize;
                    int y = r*gridCellSize;
                
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
    
    public void moveBlockToBackground(){// chuyển khối thành khối tĩnh
        int[][] shape = block.getShape();
        int h = block.getHeight();
        int w = block.getWeight();
        Color color = block.getColor();
        
        int xPos = block.getX();
        int yPos = block.getY();
        
        for(int r = 0; r < h; r++){
            for( int c = 0; c < w; c++){
                if( shape[r][c]== 1){
                    background[r + yPos][ c + xPos] = color;
                }
            }
        }
    }
    
    public int clearLines(){
        boolean lineFilled;
        int lineCleared = 0;
        for( int r = gridRows - 1 ; r >= 0; r--){
            lineFilled = true;
            for( int c = 0; c < gridColumns; c++ ){
                if( background[r][c] == null){
                    lineFilled = false;
                    break;
                }
            }
            if( lineFilled){
                lineCleared++;
                clearLine(r);
                shiftDown(r);
                clearLine(0);
                repaint();
                r++;
            }
        }
        if( lineCleared > 0)
            soundTetris.playClearSound();
        return lineCleared;
    }
    
    public void clearLine(int r){
        for( int c = 0; c < gridColumns; c++){
            background[r][c] = null;
        }
    }
    
    public void shiftDown(int r){// chuyển các khối xuống khi xóa dòng
        for( int row = r; row >= 1; row--){
            for( int col = 0; col < gridColumns; col++){
                background[row][col] = background[row-1][col];
            }
        }
    }
    
    public void drawGameOver(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        Font font = new Font("Serif", Font.BOLD, 35);
        g2d.setFont(font);
        g2d.drawString("Game Over!", gridColumns/2 + 10,gridRows /3 + 90);
    }

    public int[] getRandom() {
        return random;
    }

    public void setRandom(int[] random) {
        this.random = random;
    }
    public void gameFinish(){
        soundTetris.playFinishSound();
    }
    @Override
    protected void paintComponent(Graphics g){
        if( block == null){
            drawGameOver(g);
            return;
        }
//        if( block == null) return;
        super.paintComponent(g);
        drawBackground(g);
        drawBlock(g);
        
    }
}
