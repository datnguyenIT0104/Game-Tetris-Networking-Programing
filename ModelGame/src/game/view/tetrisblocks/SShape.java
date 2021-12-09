package game.view.tetrisblocks;

import game.view.tetrisgame.TetrisBlock;

/**
 *
 * @author DatIT
 */
public class SShape extends TetrisBlock{

    public SShape() {
        super(new int[][]{
            {0, 1, 1},
            {1, 1, 0}
        });
    }
    
}
