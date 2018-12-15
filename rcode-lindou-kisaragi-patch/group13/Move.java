package group13;

import group13.Point;
import group13.RobotInfo;
import group13.MyRobot;
import group13.Enemy;
import group13.Util;

import robocode.*;
import robocode.AdvancedRobot;
import robocode.util.Utils;

import java.io.*;
import java.lang.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Move - a class by (your name here)
 */
public class Move
{
    //weight
    protected  double WALL_WEIGHT            = 7000;
    protected  double WALL_DIM               = 2.7;
    protected  double ENEMY_WEIGHT           = 3000;
    protected  double ENEMY_DIM              = 2;
    protected  double MATE_WEIGHT            = 5000;
    protected  double MATE_DIM               = 2;
    protected  double BULLET_WEIGHT          = 10000;
    protected  double BULLET_DIM = 4;

    public  Map<String, Enemy> enemyMap = new HashMap<>(); 
    public  Map<String, MyRobot> mateMap = new HashMap<>(); 

    public TeamRobot robot;
    public MyRobot my;
    private double turnAmount;
    private double aheadAmount;
    public String target;
    public Enemy targetRobot = new Enemy();
    Point dst;

    public Move( TeamRobot _robot, MyRobot _my,
         Map<String, Enemy> eMap, Map<String, MyRobot>mMap ){
            robot = _robot;
            my =_my;
            enemyMap = eMap;
            mateMap = mMap;
    }

    public void setTarget(Enemy target){
        targetRobot = target;
    }

    public void execute(){
        goPoint();
    }

    //antiGrabityMove
    public Point setDestination() {
        Point dst = new Point(my.x,my.y);

        //target 
        if(targetRobot != null){
            dst.add(Util.getGravity(new Point(my.x ,my.y) , new Point(targetRobot.x, targetRobot.y), 5000, 2));
        }

        //dst.diff(Util.getRepulsion(my, new Point(Util.battleFieldWidth/2,Util.battleFieldHeight/2), 10000,WALL_DIM,1));
        
        //Wall
        dst.diff(Util.getRepulsion(new Point(my.x, my.y), new Point(Util.battleFieldWidth,my.y), WALL_WEIGHT,WALL_DIM,1));
        dst.diff(Util.getRepulsion(new Point(my.x, my.y), new Point(0,my.y), WALL_WEIGHT,WALL_DIM,1));
        dst.diff(Util.getRepulsion(new Point(my.x, my.y), new Point(my.x,Util.battleFieldHeight), WALL_WEIGHT,WALL_DIM,1));
        dst.diff(Util.getRepulsion(new Point(my.x, my.y), new Point(my.x,0), WALL_WEIGHT,WALL_DIM,1));

        //teammates
        for (Map.Entry<String, MyRobot> e : mateMap.entrySet()) {
            dst.diff(Util.getRepulsion(new Point(my.x, my.y) ,e.getValue(), MATE_WEIGHT,MATE_DIM,1));
        }
        
        //enemies
        for (Map.Entry<String, Enemy> e : enemyMap.entrySet()) {
            if(!e.getKey().equals(target)){ //except target
                Enemy en = e.getValue();
                if(en.alive)
                dst.diff(Util.getRepulsion(new Point(my.x, my.y) ,e.getValue(), ENEMY_WEIGHT,ENEMY_DIM,1));
            }
        }
        return dst;
    }

    public int turnTo;

    public void goPoint(){
        dst = setDestination();
        double dstRadians = my.calcRadians(dst);
        turnAmount = calcAbsTurnRadians(dstRadians);

        double dstDistance = Util.calcDistance(my, dst);
        aheadAmount = dstDistance;
        aheadAmount *= turnTo;

        double turnTIme = Util.calcTurnTime(turnAmount, my.velocity);
        double runTime = Util.calcRunTime(dstDistance, my.velocity);
        if(runTime <= turnTIme){
            aheadAmount = 0;
        }

        //System.out.println("(aheadAmount)" + aheadAmount + "(turnTo)" + turnTo);

        robot.setTurnRightRadians(turnAmount);
        robot.setAhead(aheadAmount);
    }

    public double calcAbsTurnRadians(double dstRadians) {
        double aheadTurnRadians = Util.calcTurnRadians(my.headingRadians,dstRadians);
        double backTurnRadians  = Util.calcTurnRadians(my.headingRadians,dstRadians-Math.PI);

        //System.out.println("1:" + aheadTurnRadians + "2:" +my.calcRadians(dst));
        //my.headingRadians.calcRadians(dstRadians);

        if ( Math.abs(aheadTurnRadians) < Math.abs(backTurnRadians)) { // ahead
            turnTo = 1;
            return aheadTurnRadians;
        }else { // back
            turnTo = -1;
            return backTurnRadians;
        }
    }

    public void log() {
        for(Map.Entry<String, Enemy> entry : enemyMap.entrySet()){
            System.out.println(" (Move): "  +entry.getKey() + entry.getValue());
        }
    }

    public void onPaint(Graphics2D g){
        g.setColor(Color.green);
        //destination
        g.drawOval((int)dst.x-5, (int)dst.y-5 ,10,10);

        try {
            //target bot's position
            //g.fillRect((int)targetRobot.x,(int)targetRobot.y,30,30);            
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}
