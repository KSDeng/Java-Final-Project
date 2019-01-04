package Character;

import Audio.ExplodeAudio;
import Audio.IceAudio;
import Audio.ShieldAudio;
import Audio.WinAudio;
import Enums.Direction;
import Enums.Status;
import Frame.GameObject;
import Main.GameAdmin;
import Thing.FireExplode;
import Thing.IceWave;
import Thing.Shield;
import Thing.Weapon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Fighter extends Character {
    private int [] moveCount = {0,0,0,0}, moveIndex = {0,0,0,0};
    private double transformPara;   //移动时图片切换速度的参数, 越大则图片切换越快
    private double zoomRatio = 1.0;
    private int sizeX, sizeY;
    private int atkIndex = 0;
    private Status preStatus;               //记录前一时刻状态
    private int hp, magic;                  //血量和魔法值
    private boolean ifMagicIncreasing;      //标记魔法值是否正在增加

    private boolean isArmed;        //标记是否佩戴武器
    private Weapon weapon;
    private boolean fail;

    private static Image[] standImages = null;
    private static Image[][] moveImages = null;
    private static Image[][] atkImages = null;
    private static Image shieldIcon = null;             //发动护盾技能的提示icon
    private static Image win = null, failure = null;    //游戏结束画面
    private static int shieldMagicConsume = 70;         //召唤一次护盾消耗的魔法值
    private static int fullHp = 100, fullMagic = 100;   //满血血量, 满魔法值

    static{

            shieldIcon = new Image(loader.getResource("shieldIcon.png").toString(),30,30,false,false);
            win = new Image((loader.getResource("win.png").toString()  ),600,400,false,false);
            failure = new Image((loader.getResource("failure.png").toString()),650,300,false,false);
            standImages = new Image[]{
                    new Image((loader.getResource("role1StandLeft.png").toString() )),
                    new Image((loader.getResource("role1StandRight.png").toString() )),
                    new Image((loader.getResource("role1StandUp.png").toString() )),
                    new Image((loader.getResource("role1StandDown.png").toString() ))
            };
            moveImages = new Image[][]{
                    {
                            new Image((loader.getResource("role1MoveLeft1.png").toString())),
                            new Image((loader.getResource("role1MoveLeft2.png").toString())),
                            new Image((loader.getResource("role1MoveLeft3.png").toString())),
                            new Image((loader.getResource("role1MoveLeft4.png").toString())),
                            new Image((loader.getResource("role1MoveLeft5.png").toString())),
                            new Image((loader.getResource("role1MoveLeft6.png").toString()))
                    },
                    {
                            new Image((loader.getResource("role1MoveRight1.png").toString())),
                            new Image((loader.getResource("role1MoveRight2.png").toString())),
                            new Image((loader.getResource("role1MoveRight3.png").toString())),
                            new Image((loader.getResource("role1MoveRight4.png").toString())),
                            new Image((loader.getResource("role1MoveRight5.png").toString())),
                            new Image((loader.getResource("role1MoveRight6.png").toString()))
                    },
                    {
                            new Image((loader.getResource("role1MoveUp1.png").toString())),
                            new Image((loader.getResource("role1MoveUp2.png").toString())),
                            new Image((loader.getResource("role1MoveUp3.png").toString())),
                            new Image((loader.getResource("role1MoveUp4.png").toString())),
                            new Image((loader.getResource("role1MoveUp5.png").toString())),
                            new Image((loader.getResource("role1MoveUp6.png").toString()))
                    },
                    {
                            new Image((loader.getResource("role1MoveDown1.png").toString())),
                            new Image((loader.getResource("role1MoveDown2.png").toString())),
                            new Image((loader.getResource("role1MoveDown3.png").toString())),
                            new Image((loader.getResource("role1MoveDown4.png").toString())),
                            new Image((loader.getResource("role1MoveDown5.png").toString())),
                            new Image((loader.getResource("role1MoveDown6.png").toString()))
                    }
            };

            atkImages = new Image[][]{
                    {
                            new Image((loader.getResource("role1AtkLeft1.png").toString())),
                            new Image((loader.getResource("role1AtkLeft2.png").toString())),
                            new Image((loader.getResource("role1AtkLeft3.png").toString())),
                            new Image((loader.getResource("role1AtkLeft4.png").toString())),
                            new Image((loader.getResource("role1AtkLeft5.png").toString())),
                            new Image((loader.getResource("role1AtkLeft6.png").toString())),
                            standImages[Direction.Left.ordinal()]   //ordinal返回枚举量的序数
                    },
                    {
                            new Image((loader.getResource("role1AtkRight1.png").toString())),
                            new Image((loader.getResource("role1AtkRight2.png").toString())),
                            new Image((loader.getResource("role1AtkRight3.png").toString())),
                            new Image((loader.getResource("role1AtkRight4.png").toString())),
                            new Image((loader.getResource("role1AtkRight5.png").toString())),
                            new Image((loader.getResource("role1AtkRight6.png").toString())),
                            standImages[Direction.Right.ordinal()]   //ordinal返回枚举量的序数
                    },
                    {
                            new Image((loader.getResource("role1AtkUp1.png").toString())),
                            new Image((loader.getResource("role1AtkUp2.png").toString())),
                            new Image((loader.getResource("role1AtkUp3.png").toString())),
                            new Image((loader.getResource("role1AtkUp4.png").toString())),
                            new Image((loader.getResource("role1AtkUp5.png").toString())),
                            new Image((loader.getResource("role1AtkUp6.png").toString())),
                            standImages[Direction.Up.ordinal()]   //ordinal返回枚举量的序数
                    },
                    {
                            new Image((loader.getResource("role1AtkDown1.png").toString())),
                            new Image((loader.getResource("role1AtkDown2.png").toString())),
                            new Image((loader.getResource("role1AtkDown3.png").toString())),
                            new Image((loader.getResource("role1AtkDown4.png").toString())),
                            new Image((loader.getResource("role1AtkDown5.png").toString())),
                            new Image((loader.getResource("role1AtkDown6.png").toString())),
                            standImages[Direction.Down.ordinal()]   //ordinal返回枚举量的序数
                    }

            };

    }


    public Fighter(int x, int y, int width, int height, Direction direction, boolean good, GameAdmin admin) {
        super(x, y, width, height, direction, good, admin);
        stand();
        moveSpeed = 15;
        atkTime = 80;
        transformPara = 2;
        status = Status.Standing;
        preStatus = Status.Standing;
        currentImage = standImages[direction.ordinal()];
        isArmed = false;
        hp = fullHp;magic = 0;
        ifMagicIncreasing = true;
        magicIncreaseStart();
        fail = false;
        //new ExplodeAudio();new IceAudio();new ShieldAudio();new WinAudio();
    }

    protected void move() {
        int dirIndex = direction.ordinal();
        if(direction == Direction.Left || direction == Direction.Right) transformPara = 3;
        else transformPara = 2;
        
        if(direction != preDirection){
            moveCount[dirIndex] = 0;
            moveIndex[dirIndex] = 0;
            preDirection = direction;
        }
        if(xPre != getX() || yPre != getY()){
            this.xPre = this.getX(); this.yPre = this.getY();//记录上一时刻位置
        }
        
        switch (direction){
            case Left: setX(getX()-moveSpeed);break;
            case Right:setX(getX()+moveSpeed);break;
            case Up:setY(getY()-moveSpeed);break;
            case Down:setY(getY()+moveSpeed);break;
            case Stop:break;
        }
        if(isArmed && weapon!=null)
            weapon.move(direction,moveSpeed,moveIndex[dirIndex]);
        //控制人物不能出屏幕边界
        int sizeX = getWidth(), sizeY = getHeight();
        int screenWidth = (int)this.admin.getWidth();
        int screenHeight = (int)this.admin.getHeight();
        if(getX()<0) setX(0);
        else if(getX() > screenWidth-sizeX) setX(screenWidth-sizeX);
        if(getY()<0) setY(0);
        else if(getY() > screenHeight-sizeY) setY(screenHeight-sizeY);
        
        currentImage = moveImages[dirIndex][moveIndex[dirIndex]];
        moveCount[dirIndex]++;
        if(moveCount[dirIndex] >= currentImage.getWidth()/transformPara/moveSpeed){
            moveCount[dirIndex] = 0;
            moveIndex[dirIndex]++;
            if(moveIndex[dirIndex] >= moveImages[dirIndex].length)
                moveIndex[dirIndex] = 0;
        }

    }

    protected void stand() {
        status = Status.Standing;
        currentImage = standImages[direction.ordinal()];
        if(isArmed)
            weapon.stand();
    }

    protected void stay() {
        status = Status.Standing;
        setX(xPre);setY(yPre);
    }

    public void attack(){
        if(!isArmed) {
            this.admin.showInfo("未佩戴武器，无法攻击！(I键佩戴武器)");
            return;        
        }
        status = Status.Attacking;
        preStatus = status;

    }

    public void shield(){
        if(!isArmed){
            this.admin.showInfo("没有佩戴武器，无法发动技能！");
            return;
        }
        if(magic >= shieldMagicConsume){
            //new ShieldAudio().run();
            magic -= shieldMagicConsume;
            this.admin.addObject(new Shield(getX() - 120,getY() - 120,true,this.admin));
        }
        else{
            this.admin.showInfo("没有足够的魔法值，无法召唤护盾！");
        }
    }

    //开始增加魔法值, 10s后加满
    private void magicIncreaseStart(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                magic += 1;
                if(magic >= fullMagic){
                    ifMagicIncreasing = false;
                    System.gc();cancel();
                }
            }
        },0,100);
    }

    protected void dead(){
        fail = true;
    }

    //获得体力恢复
    public void getRecover(int recover){
        hp += recover;
        if(hp>=fullHp) {
            hp = fullHp;
            this.admin.showInfo("你已恢复至满血！");
        }else{
            this.admin.showInfo("体力值恢复至"+hp+"点...");
        }
    }
    public Rectangle getRect(){
        return new Rectangle(getX(),getY(),sizeX,sizeY);
    }

    public void setWeapon(Weapon weapon){
        moveSpeed = 10;     //戴上武器之后跑得更慢
        this.weapon = weapon;
        this.weapon.setOwner(this);
        isArmed = true;
        this.weapon.appear();
    }
    public void unsetWeapon(){
        moveSpeed = 15;
        this.weapon.disappear();
        isArmed = false;
    }

    private void collidesWithObjects(List<GameObject> objects){
        for(GameObject o: objects){
            if(o!=this){
                if(o instanceof Snake && ((Snake) o).getRect().intersects(this.getRect())){
                    this.stay();((Snake) o).stay();
                }
            }
        }
    }

    public boolean isArmed() {
        return isArmed;
    }

    public void setArmed(boolean armed) {
        isArmed = armed;
    }

    public void getHit(int xHit,int yHit,int hitPower){
        //new ExplodeAudio().run();
        this.admin.addObject(new FireExplode(xHit,yHit,false,this.admin));
        hp -= hitPower;
        if(hp <= 0) dead();    
    }

    //判断并实施攻击
    private void atkAct(){
        if(this.preStatus == Status.Attacking && direction != Direction.Stop && this.isArmed){
            if(atkIndex == atkImages[direction.ordinal()].length/2){ createAtkEffect(); }
            this.weapon.setVisible(false);
            //System.out.println(atkIndex);
            if(atkIndex % 2 == 0)
                currentImage = atkImages[direction.ordinal()][atkIndex/2 ];
            atkIndex ++;
        }
        if(atkIndex >= 2*atkImages[direction.ordinal()].length - 1){
            atkIndex = 0;
            this.preStatus = Status.Standing;
            if(isArmed) {
                weapon.setVisible(true);
            }
        }
    }
    
    private void createAtkEffect(){
        //new IceAudio().run();

        switch (direction) {
            case Left: {
                IceWave left = new IceWave(getX() , getY(), direction, true,this.admin);
                this.admin.addObject(left);
                break;
            }
            case Right: {
                IceWave right = new IceWave(getX() + sizeX/2, getY(), direction, true,this.admin);
                this.admin.addObject(right);
                break;
            }
            case Up: {
                IceWave up = new IceWave(getX(), getY() , direction, true,this.admin);
                this.admin.addObject(up);
                break;
            }
            case Down: {
                IceWave down = new IceWave(getX(), getY() + sizeY, direction, true,this.admin);
                this.admin.addObject(down);
                break;
            }
        }
    }

    //private void playWinAudio(){
    //    new WinAudio().run();
    //}

    public void draw(GraphicsContext gc) {
        if(admin.getNumMonster() == 0){         //游戏胜利
            gc.drawImage(win,250,100,(int)win.getWidth(),(int)win.getHeight());
            //playWinAudio();
        }
        if(fail){                               //游戏失败
            gc.drawImage(failure,200,100,(int)failure.getWidth(),(int)failure.getHeight());
            this.admin.pause();
        }
        sizeX = (int)(currentImage.getWidth()*zoomRatio);
        sizeY = (int)(currentImage.getHeight()*zoomRatio);
        if(this.live){
            atkAct();
            gc.drawImage(currentImage,getX(),getY(),sizeX,sizeY);
            if(hp>=0 && magic>=0){
                gc.drawImage(hpImage,getX(),getY()-5,(double)hp/100.0*hpImage.getWidth(),hpImage.getHeight());
                gc.drawImage(magicImage,getX(),getY()-15,(double)magic/100.0*magicImage.getWidth(),magicImage.getHeight());
            }
        }
        //根据状态执行动作
        switch (status){
            case Moving: move();break;
            case Standing:stand();break;
            default:break;
        }
        
        collidesWithObjects(this.admin.getObjectList());
        //启动魔法恢复计时器
        if(this.magic < 100 && !ifMagicIncreasing) {
            magicIncreaseStart();
            ifMagicIncreasing = true;
        }
        //显示可以发动技能的提示图标
        if(magic >= shieldMagicConsume){
            gc.drawImage(shieldIcon,550,500,(int)shieldIcon.getWidth(),(int)shieldIcon.getHeight());
        }
    }

    public void update() {

    }
}
