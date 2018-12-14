package group13;

import group13.AntiWall;
import group13.Point;
import group13.RobotInfo;
import group13.MyRobot;
import group13.Enemy;
import group13.Util;
import group13.bulletmap.*;

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

public class Gun{
    public  Map<String, Enemy> enemyMap = new HashMap<>(); 
    public Enemy targetRobot;

    public TeamRobot robot;
    public MyRobot my;

    public Point nextPoint = new Point();
    private double gunTurnAmount;
    private double power;
    
    public static final int AIMTYPE_PINPOINT = 0;
    public static final int AIMTYPE_CONSTANT = 1;
    public static final int AIMTYPE_ACCELE = 2;
    public static final int AIMTYPE_CIRCLE = 3;
    private AntiWall antiWall = new AntiWall();

    public BulletMapping bulletmapping;
    public Estimation estimation;

    public Gun(TeamRobot _robot, MyRobot _my,
        Map<String, Enemy> eMap,BulletMapping bulletMapping) {
            robot = _robot;
            my =_my;
            enemyMap = eMap;
            bulletmapping = new BulletMapping(my);
            bulletmapping = bulletMapping;
            estimation=new Estimation(bulletmapping);
    }

    public void setTarget(Enemy target){
        //target = name;
        targetRobot = new Enemy();
        targetRobot = target;
    }

    public void execute() {
        if(targetRobot != null){
        int pattern;
        pattern = estimation.EstimationPattern(false);
        doGunTurn();
        dofire();
        bulletmapping.FriendBulletGenerate(power,gunTurnAmount,pattern);
        }
    }

    public void doGunTurn() {
        gunTurnAmount = calcGunTurnRadians(my, targetRobot);
        robot.setTurnGunRightRadians(gunTurnAmount);
    }

    public void dofire(){
        // do something
        if(targetRobot.name.contains("Wall")){
            if(!antiWall.holdFire)robot.setFire(3);
        }else{   
            if(targetRobot.distance < 200){
                power = 2.5;
            }else{
                power = 0.1;
            }
            robot.setFire(power);
        }
    }

    public double calcGunTurnRadians(MyRobot my, Enemy en){
        double amount = 0;
        if(en.name.contains("Wall")){
            amount = antiWall.prepareFire(my, en) * Math.PI / 360;
        }else{
        nextPoint = setNextPoint();
        amount = Util.calcTurnRadians(my.gunHeadingRadians, my.calcRadians(nextPoint));
        }
        return amount;
    }

    public Point setNextPoint(){
        int aimType = 0;
        aimType = setNextAimType();
        Point nextPoint = new Point();
        switch (aimType) {
            case AIMTYPE_PINPOINT:
                nextPoint = pinpoint();
                break;
            case AIMTYPE_CONSTANT:
                //nextPoint = constant();
                break;
            case AIMTYPE_ACCELE:
                //nextPoint = Accele();
                break;
            case AIMTYPE_CIRCLE:
                //nextPoint = Circle();
                break;
            default:
                nextPoint = pinpoint();
                break;
        }

        return nextPoint;
    }

    public int setNextAimType() {
        int aimType = 0;
        // decide aimtype using softmax.....

        return aimType;
    }

    private Point pinpoint(){
        return  new Point(targetRobot.x , targetRobot.y);
    }

    /*
    private Point constant(){
    }*/
    /*
    private Point Accele(){

    }*/

    /*
    private Point Circle(){  
    }*/

    public void onPaint(Graphics2D g) {
        g.setColor(Color.white);
        if(targetRobot!=null)
		g.drawString("Target: " + (int)targetRobot.distance + targetRobot.name, (int)my.x - 60 ,(int)my.y - 60);
    }
}