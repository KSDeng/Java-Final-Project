package Thing;

import Frame.GameObject;
import Main.GameAdmin;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class IceHit extends GameObject {
    private GameAdmin admin;
    private static Image[] images;
    private int index;
    private Image currentImage;

    static{

            images = new Image[]{
                    new Image(loader.getResource("iceHit1.png").toString()),
                    new Image(loader.getResource("iceHit2.png").toString()),
                    new Image(loader.getResource("iceHit3.png").toString()),
                    new Image(loader.getResource("iceHit4.png").toString()),
                    new Image(loader.getResource("iceHit5.png").toString()),
                    new Image(loader.getResource("iceHit6.png").toString()),
                    new Image(loader.getResource("iceHit7.png").toString())
            };
    }
    public IceHit(int x, int y, boolean good,GameAdmin admin){
        super(x,y,50,50,good);
        index = 0;
        this.admin = admin;
    }
    public void draw(GraphicsContext gc) {
        currentImage = images[index];
        gc.drawImage(currentImage,getX(),getY(),(int)currentImage.getWidth(),(int)currentImage.getHeight());
        index++;
        if(index == images.length){
            index = 0;
            this.admin.removeObject(this);
        }
    }

    public void update() {

    }
}
