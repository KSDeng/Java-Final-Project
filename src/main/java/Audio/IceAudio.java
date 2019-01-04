package Audio;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.FileInputStream;
import java.io.IOException;

public class IceAudio extends MyAudio {
    private AudioStream ice;

    public IceAudio(){
        try {
            ice = new AudioStream(new FileInputStream(classPath + "\\audio\\ice.wav"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run(){
        AudioPlayer.player.start(ice);
    }
}
