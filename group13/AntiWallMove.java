package group13;

import group13.Point;
import group13.RobotInfo;
import group13.MyRobot;
import group13.Enemy;

import robocode.*;
import robocode.AdvancedRobot;
import robocode.util.Utils;

import java.lang.*;
import java.awt.Color;
import java.awt.Graphics2D;
/**
 * AntiWallMove - a class by (your name here)
 */
public class AntiWallMove
{
    protected static final double DEFAULT_WALL_WEIGHT = 500;
    protected static final double DEFAULT_WALL_DIM = 1.8;
    protected static final double DEFAULT_ENEMY_WEIGHT = 400;
    protected static final double DEFAULT_ENEMY_DIM = 1.8;
    protected static final double DEFAULT_MATE_WEIGHT = 600;
    protected static final double DEFAULT_MATE_DIM = 1.6;
    protected static final double DEFAULT_G_WEIGHT = 10;
    protected static final double DEFAULT_G_DIM = 1.1;
    protected static final double DEFAULT_GT_WEIGHT = 100;
    protected static final double DEFAULT_GT_DIM = 2.8;
    protected static final long   DEFAULT_G_EXPIRE = 5;
    protected static final long   DEFAULT_G_DISTANCE_THRESHIOLD = 20;
    protected static final double DEFAULT_LOCKON_APPROACH = 12;
    protected static final long   DEFAULT_BULLET_PROSPECT_TIME = 12;
    protected static final double DEFAULT_BULLET_WEIGHT = 1000;
    protected static final double DEFAULT_BULLET_DIM = 4;

    protected  double WALL_WEIGHT            = DEFAULT_WALL_WEIGHT;
    protected  double WALL_DIM               = DEFAULT_WALL_DIM;
    protected  double ENEMY_WEIGHT           = DEFAULT_ENEMY_WEIGHT;
    protected  double ENEMY_DIM              = DEFAULT_ENEMY_DIM;
    protected  double MATE_WEIGHT            = DEFAULT_MATE_WEIGHT;
    protected  double MATE_DIM               = DEFAULT_MATE_DIM;
    protected  double G_WEIGHT               = DEFAULT_G_WEIGHT;
    protected  double G_DIM                  = DEFAULT_G_DIM;
    protected  double GT_WEIGHT              = DEFAULT_GT_WEIGHT;
    protected  double GT_DIM                 = DEFAULT_GT_DIM;
    protected  long   G_EXPIRE               = DEFAULT_G_EXPIRE;
    protected  long   G_DISTANCE_THRESHIOLD  = DEFAULT_G_DISTANCE_THRESHIOLD;
    protected  double LOCKON_APPROACH        = DEFAULT_LOCKON_APPROACH;
    protected  long   BULLET_PROSPECT_TIME   = DEFAULT_BULLET_PROSPECT_TIME;
    protected  double BULLET_WEIGHT          = DEFAULT_BULLET_WEIGHT;
    protected  double BULLET_DIM = DEFAULT_BULLET_DIM;

    public void execute(AdvancedRobot robot){
        //set系を使う
    }

    public void move(MyRobot my, Enemy enemy){

    }

    public void calcFire(Enemy enemy, MyRobot my){
        
    }

    //set destination selected by antigravity 
    //public Point antiGravMove(){
    //}

    public void onPaint(Graphics2D g, MyRobot my, Enemy enemy){
        g.setColor(Color.green);
    }
}
