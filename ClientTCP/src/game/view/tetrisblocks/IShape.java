package game.view.tetrisblocks;

import game.view.tetrisgame.TetrisBlock;

/**
 *
 * @author DatIT
 */
public class IShape extends TetrisBlock{
    
    public IShape() {
        super(new int[][]{ { 1, 1, 1, 1}});
        
    }

    @Override
    public void rotate() {
        super.rotate(); //To change body of generated methods, choose Tools | Templates.
        if( getWeight() == 1){
            setX( getX() + 1);
            setY( getY() - 1);
        }else{
            setX( getX() - 1);
            setY( getY() + 1);
        }
    }
    
}
