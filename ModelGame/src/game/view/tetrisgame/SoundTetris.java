package game.view.tetrisgame;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author DatIT
 */
public class SoundTetris {
    private String soundFile = "tetrissounds" + File.separator;
    private String clearSound =soundFile + "clear.wav";
    private String finishSound = soundFile +  "success.wav";
    
    private Clip clear, finish;

    public SoundTetris() {
        
        try {
            clear = AudioSystem.getClip();
            finish = AudioSystem.getClip();
            
            clear.open(AudioSystem.getAudioInputStream( new File(clearSound).getAbsoluteFile()));
            finish.open(AudioSystem.getAudioInputStream(new File(finishSound).getAbsoluteFile()));
        } catch (LineUnavailableException ex) {
            Logger.getLogger(SoundTetris.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(SoundTetris.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SoundTetris.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void playClearSound(){
        clear.setFramePosition(0);
        clear.start();
    }
    public void playFinishSound(){
        finish.setFramePosition(0);
        finish.start();
    }
}
