package Audio;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.FileInputStream;
import java.io.IOException;

public class ExplodeAudio extends MyAudio{
    private AudioStream explode;

    public ExplodeAudio(){
        try {
            explode = new AudioStream(new FileInputStream(classPath + "\\audio\\explode.wav"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run(){
        AudioPlayer.player.start(explode);
    }
}
