package model.game;

import java.io.Serializable;

/**
 *
 * @author DatIT
 */
public class GameAreaEncode implements Serializable{
    private int[][] background;
    private TetrisBlockEncode block;
    private int blockIndex;
    
    public GameAreaEncode() {
    }

    public GameAreaEncode(int[][] background, TetrisBlockEncode block, int blockIndex) {
        this.background = background;
        this.blockIndex = blockIndex;
        this.block = block;
    }

    public int getBlockIndex() {
        return blockIndex;
    }

    public void setBlockIndex(int blockIndex) {
        this.blockIndex = blockIndex;
    }
    
    public int[][] getBackground() {
        return background;
    }

    public void setBackground(int[][] background) {
        this.background = background;
    }

    public TetrisBlockEncode getBlock() {
        return block;
    }

    public void setBlock(TetrisBlockEncode block) {
        this.block = block;
    }
    
}
