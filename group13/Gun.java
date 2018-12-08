package group13;

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
    public String target;
    public Enemy targetRobot = new Enemy();

    public TeamRobot robot;
    public MyRobot my;

    private double gunTurnAmount;
    private double power;
    
    public static final int AIMTYPE_PINPOINT = 0;
    public static final int AIMTYPE_CONSTANT = 1;

    public BulletMapping bulletmapping;
    public Estimation estimation=new Estimation();
    public Gun(TeamRobot _robot, MyRobot _my,
        Map<String, Enemy> eMap,BulletMapping bulletMapping) {
            robot = _robot;
            my =_my;
            enemyMap = eMap;
            bulletmapping=bulletMapping;
    }

    public void setTarget(String name){
        target = name;
        targetRobot = enemyMap.get(target);
    }

    public void execute() {
        int pattern;
        pattern=estimation.EstimationPattern(false);
        doGunTurn();
        dofire();
        bulletmapping.FriendBulletGenerate(power,gunTurnAmount,pattern);
    }

    public void doGunTurn() {
        gunTurnAmount = calcGunTurnRadians(my, targetRobot);
        robot.setTurnRight(gunTurnAmount);
    }

    public void dofire(){
        // do something

        robot.setFire(power);
    }

    public double calcGunTurnRadians(MyRobot my, Enemy en){
        double amount = 0;
        Point nextPoint = new Point();
        nextPoint = setNextPoint();        
        //do something

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
}