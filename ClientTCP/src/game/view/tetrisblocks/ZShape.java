package game.view.tetrisblocks;

import game.view.tetrisgame.TetrisBlock;

/**
 *
 * @author DatIT
 */
public class ZShape extends TetrisBlock{

    public ZShape() {
        super(new int[][]{
            {1, 1, 0},
            {0, 1, 1}
        });
    }
    
}
