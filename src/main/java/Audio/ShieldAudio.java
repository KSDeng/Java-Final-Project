package Audio;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.FileInputStream;
import java.io.IOException;

public class ShieldAudio extends MyAudio {
    private AudioStream shield;

    public ShieldAudio(){
        try {
            shield = new AudioStream(new FileInputStream(classPath + "\\audio\\shield.wav"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run(){
        AudioPlayer.player.start(shield);
    }
}
