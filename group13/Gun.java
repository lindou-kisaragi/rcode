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


    private AntiWall antiWall = new AntiWall();

    public BulletMapping bulletmapping;
    public Estimation estimation;

    public int aimType = 0;

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
        doGunTurn();
        dofire();
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
        System.out.println(bullet);
        bulletmapping.FriendBulletGenerate(power,gunTurnAmount,aimType,bullet);
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
        aimType = setNextAimType();
        System.out.println(aimType);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        Point p = new Point();
        switch (aimType) {
            case AIMTYPE_PINPOINT:
                p = pinpoint();
                break;
            case AIMTYPE_CONSTANT:
                p = constant();
                break;
            default:
                p = pinpoint();
                break;
        }

        return p;
    }

    public int setNextAimType() {
        int aimType = 1;
        // decide aimtype using softmax.....
        pattern = estimation.EstimationPattern(false);
        return aimType;
    }

    private Point pinpoint(){
        return  new Point(targetRobot.x , targetRobot.y);
    }

    private Point constant(){
        double t = calcTimeToReach(my, targetRobot, targetRobot.velocity, 2.5);
        double x = targetRobot.x + targetRobot.velocity*Math.sin(targetRobot.headingRadians)*t;
        double y = targetRobot.y + targetRobot.velocity*Math.cos(targetRobot.headingRadians)*t;
        return new Point(x,y);
    }

    public  double calcTimeToReach
    ( MyRobot my, Enemy en, double v, double p){
        double t; //time to reach bullet to enemy(including turning gun)
        double s = Util.bulletSpeed(p);
        t = Util.solveEquation((v*v - s*s),
        2*v*(en.x-my.x)*Math.sin(en.headingRadians) + 2*v*(en.y-my.y)*Math.cos(en.headingRadians),
        (en.x-my.x)*(en.x-my.x)+(en.y-my.y)*(en.y-my.y) );
        return t;
    }

    public void onPaint(Graphics2D g) {
        g.setColor(Color.white);
        if(targetRobot!=null)
        g.drawString("Target: " + (int)targetRobot.distance + targetRobot.name, (int)my.x - 60 ,(int)my.y - 60);
        g.setColor(Color.red);
        g.drawOval((int)nextPoint.x-5, (int)nextPoint.y-5 ,10,10);
    }
}