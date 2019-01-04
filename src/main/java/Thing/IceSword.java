package Thing;

import Character.Snake;
import Animation.MyAnimation;
import Animation.WeaponAnimation;
import Enums.Direction;
import Enums.Status;
import Frame.GameObject;
import Main.GameAdmin;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class IceSword extends Weapon {
    private Direction direction;
    private Image currentImage;
    private double zoomRatio = 1.0;
    private int locX , locY ;
    private int sizeX, sizeY;
    private int preX, preY;
    private static Image[] standImages = null;
    private static Image[][] moveImages = null;
    public static Image[][] atkImages = null;
    private static int[][] standModify = null;
    private static int[][][] moveModify = null;
    public static int[][][] atkModify = null;

    public IceSword(int x, int y, Direction direction, boolean good,GameAdmin admin){
        super(x,y,50,50,good,admin);
        this.direction = direction;
        this.currentImage = standImages[direction.ordinal()];
        locX = x;locY = y;
        setVisible(false);
    }
    //初始化各种形象
    static{
            standImages = new Image[]{
                    new Image(loader.getResource("iceSwordStandLeft.png").toString()),
                    new Image(loader.getResource("iceSwordStandRight.png").toString()),
                    new Image(loader.getResource("iceSwordStandUp.png").toString()),
                    new Image(loader.getResource("iceSwordStandDown.png").toString())
            };
            moveImages = new Image[][]{
                    {
                            new Image(loader.getResource("iceSwordMoveLeft1.png").toString()),
                            new Image(loader.getResource("iceSwordMoveLeft2.png").toString()),
                            new Image(loader.getResource("iceSwordMoveLeft3.png").toString()),
                            new Image(loader.getResource("iceSwordMoveLeft4.png").toString()),
                            new Image(loader.getResource("iceSwordMoveLeft5.png").toString()),
                            new Image(loader.getResource("iceSwordMoveLeft6.png").toString())
                    },
                    {
                            new Image(loader.getResource("iceSwordMoveRight1.png").toString()),
                            new Image(loader.getResource("iceSwordMoveRight2.png").toString()),
                            new Image(loader.getResource("iceSwordMoveRight3.png").toString()),
                            new Image(loader.getResource("iceSwordMoveRight4.png").toString()),
                            new Image(loader.getResource("iceSwordMoveRight5.png").toString()),
                            new Image(loader.getResource("iceSwordMoveRight6.png").toString())
                    },
                    {
                            new Image(loader.getResource("iceSwordMoveUp1.png").toString()),
                            new Image(loader.getResource("iceSwordMoveUp2.png").toString()),
                            new Image(loader.getResource("iceSwordMoveUp3.png").toString()),
                            new Image(loader.getResource("iceSwordMoveUp4.png").toString()),
                            new Image(loader.getResource("iceSwordMoveUp5.png").toString()),
                            new Image(loader.getResource("iceSwordMoveUp6.png").toString())
                    },
                    {
                            new Image(loader.getResource("iceSwordMoveDown1.png").toString()),
                            new Image(loader.getResource("iceSwordMoveDown2.png").toString()),
                            new Image(loader.getResource("iceSwordMoveDown3.png").toString()),
                            new Image(loader.getResource("iceSwordMoveDown4.png").toString()),
                            new Image(loader.getResource("iceSwordMoveDown5.png").toString()),
                            new Image(loader.getResource("iceSwordMoveDown6.png").toString())
                    }
            };
            atkImages = new Image[][]{
                    {
                            new Image(loader.getResource("iceSwordAtkLeft1.png").toString()),
                            new Image(loader.getResource("iceSwordAtkLeft2.png").toString()),
                            new Image(loader.getResource("iceSwordAtkLeft3.png").toString()),
                            new Image(loader.getResource("iceSwordAtkLeft4.png").toString()),
                            new Image(loader.getResource("iceSwordAtkLeft5.png").toString()),
                            new Image(loader.getResource("iceSwordAtkLeft6.png").toString()),
                            new Image(loader.getResource("iceSwordAtkLeft4.png").toString()),
                            new Image(loader.getResource("iceSwordAtkLeft2.png").toString()),
                            standImages[Direction.Left.ordinal()]
                    },
                    {
                            new Image(loader.getResource("iceSwordAtkRight1.png").toString()),
                            new Image(loader.getResource("iceSwordAtkRight2.png").toString()),
                            new Image(loader.getResource("iceSwordAtkRight3.png").toString()),
                            new Image(loader.getResource("iceSwordAtkRight4.png").toString()),
                            new Image(loader.getResource("iceSwordAtkRight5.png").toString()),
                            new Image(loader.getResource("iceSwordAtkRight6.png").toString()),
                            new Image(loader.getResource("iceSwordAtkRight4.png").toString()),
                            new Image(loader.getResource("iceSwordAtkRight2.png").toString()),
                            standImages[Direction.Right.ordinal()]
                    },
                    {
                            new Image(loader.getResource("iceSwordAtkUp1.png").toString()),
                            new Image(loader.getResource("iceSwordAtkUp2.png").toString()),
                            new Image(loader.getResource("iceSwordAtkUp3.png").toString()),
                            new Image(loader.getResource("iceSwordAtkUp4.png").toString()),
                            new Image(loader.getResource("iceSwordAtkUp5.png").toString()),
                            new Image(loader.getResource("iceSwordAtkUp6.png").toString()),
                            new Image(loader.getResource("iceSwordAtkUp4.png").toString()),
                            new Image(loader.getResource("iceSwordAtkUp2.png").toString()),
                            standImages[Direction.Up.ordinal()]
                    },
                    {
                            new Image(loader.getResource("iceSwordAtkDown1.png").toString()),
                            new Image(loader.getResource("iceSwordAtkDown2.png").toString()),
                            new Image(loader.getResource("iceSwordAtkDown3.png").toString()),
                            new Image(loader.getResource("iceSwordAtkDown4.png").toString()),
                            new Image(loader.getResource("iceSwordAtkDown5.png").toString()),
                            new Image(loader.getResource("iceSwordAtkDown6.png").toString()),
                            new Image(loader.getResource("iceSwordAtkDown4.png").toString()),
                            new Image(loader.getResource("iceSwordAtkDown2.png").toString()),
                            standImages[Direction.Down.ordinal()]
                    }
            };

    }

    //初始化坐标修正矩阵
    static{
        standModify = new int[][]{
                new int[]{-27,0},
                new int[]{-27,0},
                new int[]{2,33},
                new int[]{-37,-20}
        };
        moveModify = new int[][][]{
                new int[][]{
                        {10,-5},{-5,-5},{-5,-8},{0,0},{10,-3},{10,-3}
                },
                new int[][]{
                        {5,-5},{10,-5},{20,-5},{10,0},{0,0},{10,-5}
                },
                new int[][]{
                        {-12,45},{-2,40},{-2,40},{0,45},{-3,50},{-8,48}
                },
                new int[][]{
                        {-50,-25},{-60,-28},{-60,-25},{-60,-20},{-60,-23},{-57,-25}
                }
        };
        atkModify = new int[][][]{
                new int[][]{
                        {-5,20},{-3,18},{30,30},{23,13},{-28,-25},{30,-15},{23,13},{-3,18},{-27,0}
                },
                new int[][]{
                        {-30,25},{-83,18},{-24,30},{-20,10},{25,-25},{17,-15},{-20,10},{-83,18},{-27,0}
                },
                new int[][]{
                        {22,17},{-63,37},{-28,52},{-20,47},{-80,-23},{-82,-7},{-20,47},{-63,37},{2,33}
                },
                new int[][]{
                        {-75,45},{-80,-20},{-90,-10},{-80,40},{-30,-22},{38,-22},{-80,40},{-80,-20},{-37,-20}
                }
        };
    }


    public void stand() {
        currentImage = standImages[direction.ordinal()];
        locX = this.owner.getX()+standModify[direction.ordinal()][0];
        locY = this.owner.getY()+standModify[direction.ordinal()][1];
    }

    public void stay() {
        setX(preX);setY(preY);
    }


    public void move(Direction direction, int speed, int imageIndex) {
        preX = getX();preY = getY();
        currentImage = moveImages[direction.ordinal()][imageIndex];
        int xModify = moveModify[direction.ordinal()][imageIndex][0];
        int yModify = moveModify[direction.ordinal()][imageIndex][1];
        switch (direction){
            case Left:{
                setX(getX()-speed);
                break;
            }
            case Right:{
                setX(getX()+speed);
                break;
            }
            case Up:{
                setY(getY()-speed);
                break;
            }
            case Down:{
                setY(getY()+speed);
                break;
            }
        }
        locX = this.owner.getX()+xModify; locY = this.owner.getY()+yModify;
    }

    public MyAnimation attack(Direction direction, int time){
        return new WeaponAnimation(this.admin.getGraphicsContext2D(),this.owner.getX(),this.owner.getY(),
                time,atkImages[direction.ordinal()],atkModify[direction.ordinal()]);
    }

    public void collidesWithObjects(List<GameObject> objects) {
        for(int i=0;i<objects.size();i++){
            GameObject o = objects.get(i);
            if(o!=this){
                if(o instanceof Snake && ((Snake) o).getRect().intersects(this.getRect())){
                    this.stay();
                }
            }
        }
    }
    public Rectangle getRect(){
        return new Rectangle(locX,locY,sizeX,sizeY);
    }

    @Override
    public void appear(){
        this.setVisible(true);
        if(owner!=null && owner.getStatus() == Status.Standing){
            setX(this.owner.getX());
            setY(this.owner.getY());
            locX = getX() + standModify[owner.getDirection().ordinal()][0];
            locY = getY() + standModify[owner.getDirection().ordinal()][1];
        }
    }
    @Override
    public void disappear(){
        this.setVisible(false);
        this.owner = null;
    }
    @Override
    public void draw(GraphicsContext gc) {
        sizeX = (int)(currentImage.getWidth()*zoomRatio);
        sizeY = (int)(currentImage.getHeight()*zoomRatio);
        gc.drawImage(currentImage,locX,locY,sizeX,sizeY);

        collidesWithObjects(this.admin.getObjectList());
    }

    @Override
    public void update() {

    }
}
