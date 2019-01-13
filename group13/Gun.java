package group13;

import group13.AntiWall;
import group13.Point;
import group13.RobotInfo;
import group13.MyRobot;
import group13.Enemy;
import group13.Util;
import group13.bulletmap.*;
import group13.Move;

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

    
    public int aimType = 0;

    public Gun(TeamRobot _robot, MyRobot _my, Move move,
        Map<String, Enemy> eMap,BulletMapping bulletMapping) {
            robot = _robot;
            my =_my;
            enemyMap = eMap;
            //bulletmapping = new BulletMapping(my, move, _robot);
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
        prepareFiring();
        doGunTurn();
        if(my.heat == 0)dofire();
        }
    }

    public void doGunTurn() {
        gunTurnAmount = calcGunTurnRadians(my, targetRobot);
        robot.setTurnGunRightRadians(gunTurnAmount);
    }

    //List<Bullet> bulletList = new ArrayList<Bullet>();
    public void dofire(){
        //for Wall bot
        //Bullet bullet=new Bullet(0,0,0,0,null,null,true,0);
        Bullet bullet=null;

        if(targetRobot.name.contains("Wall")){
            if(!antiWall.holdFire){
                bullet = robot.setFireBullet(3);
                //if(bullet != null)bulletList.add(bullet);
            }
        }else{
            if(targetRobot.distance < 200){
                power = 2.5;
            }else{
                power = 0.1;
            }
            bullet= robot.setFireBullet(power);
            //if(bullet != null)bulletList.add(bullet);
        }
        if(bullet != null && bullet.hashCode()!=0){bulletmapping.FriendBulletGenerate(power,gunTurnAmount,aimType,bullet);System.out.println(bullet);}
    }	

    public void onBulletHit(BulletHitEvent e,boolean ishit) {	
        /*Bullet hitbullet = e.getBullet();*/	
        //get bullet in bulletList that hit enemy by either ways
        Bullet bulletobj=e.getBullet();
        System.out.println(bulletobj);
        bulletmapping.InputBulletDataFriend(ishit,bulletobj);
        
        /*for(Bullet b:bulletList){	
            if(b.equals(hitbullet)){	
            System.out.println("hit!!!!!!!!!!!!!!!!!!!!!!!!!!!" + e.getName());	
            bulletList.remove(b);	
            }	
            if(b.hashCode() == (hitbullet.hashCode())){	
            System.out.println("hit!!!!!!!!!!!!!!!!!!!!!!!!!!!" + e.getName() );	
            bulletList.remove(b);	
            }
        }*/
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
        
        Point p = new Point();
        switch (aimType) {
            case AIMTYPE_PINPOINT:
                p = pinpoint();
                break;
            case AIMTYPE_CONSTANT:
                p = constant();
                break;
            case AIMTYPE_CIRCLE:
                p = circle();
                break;
            case AIMTYPE_ACCELE:
                p = accele();
                break;
            default:
                p = pinpoint();
                break;
        }
        return p;
    }

    public int setNextAimType() {
        // decide aimtype using softmax.....
        aimType = estimation.EstimationPattern(false);
        //aimType = AIMTYPE_CIRCLE;
        return aimType;
    }

    public Point pinpoint(){
        return  new Point(targetRobot.x , targetRobot.y);
    }

    public Point constant(){
        Point p = new Point(targetRobot.x, targetRobot.y);
        double t = calcTimeToReach(my, targetRobot, targetRobot.velocity, power);
        p.x = targetRobot.x + targetRobot.velocity*Math.sin(targetRobot.headingRadians)*t;
        p.y = targetRobot.y + targetRobot.velocity*Math.cos(targetRobot.headingRadians)*t;

        if(Util.outOfBattleField(p)){	
            p.x = Math.min(Math.max(18.0, p.x), Util.battleFieldWidth - 18.0);	
            p.y = Math.min(Math.max(18.0, p.y), Util.battleFieldHeight - 18.0);	
        }	
        return p;
    }

    public  double calcTimeToReach( MyRobot my, Enemy en, double v, double p){
        double t; //time to reach bullet to enemy(including turning gun)
        double s = Util.bulletSpeed(p);
        t = Util.solveEquation((v*v - s*s),
        2*v*(en.x-my.x)*Math.sin(en.headingRadians) + 2*v*(en.y-my.y)*Math.cos(en.headingRadians),
        (en.x-my.x)*(en.x-my.x)+(en.y-my.y)*(en.y-my.y) );
        return t;
    }

    List<Point> pa = new ArrayList<Point>();
    public Point accele(){
        Point p = new Point(targetRobot.x, targetRobot.y);
        int deltaTime = 0;	
        double v = targetRobot.velocity;
        pa.clear();
        while((++deltaTime) * Util.bulletSpeed(power)<	my.calcDistance(p)){	
            p.x += Math.sin(targetRobot.headingRadians) * v;	
            p.y += Math.cos(targetRobot.headingRadians) * v;	
            //System.out.println(("gun v:" + v));
            pa.add(p);
            if(v > -8 && v < 8){
                v += enemySpeedChange;
                if(v > 8)v=8;
                if(v < -8)v=-8;
            }
            if(Util.outOfBattleField(p)){	
                p.x = Math.min(Math.max(18.0, p.x), Util.battleFieldWidth - 18.0);	
                p.y = Math.min(Math.max(18.0, p.y), Util.battleFieldHeight - 18.0);	
                break;	
            }	
        }	
        return p;
    }

    List<Point> ps = new ArrayList<Point>();
    public Point circle(){	
        double enemyHeading = targetRobot.headingRadians;	
        Point p = new Point(targetRobot.x, targetRobot.y);	
        int deltaTime = 0;	
        ps.clear();	
        while((++deltaTime) * Util.bulletSpeed(power)<	my.calcDistance(p)){	
            p.x += Math.sin(enemyHeading) * targetRobot.velocity;	
            p.y += Math.cos(enemyHeading) * targetRobot.velocity;
            ps.add(new Point(p.x, p.y));	
            enemyHeading += enemyHeadingChange;	
            if(Util.outOfBattleField(p)){	
                p.x = Math.min(Math.max(18.0, p.x), Util.battleFieldWidth - 18.0);	
                p.y = Math.min(Math.max(18.0, p.y), Util.battleFieldHeight - 18.0);	
                break;	
            }	
        }	
        return p;	
    }	
    
    double enemyHeadingChange;
    double enemySpeedChange;
    private void prepareFiring(){
        enemyHeadingChange = targetRobot.headingRadians - targetRobot.prevHeadingRadians;	
        enemySpeedChange = targetRobot.velocity - targetRobot.prevVelocity;
        
        System.out.println("(Gun headingChange)" +enemyHeadingChange);
        System.out.println("(Gun speedChange)" + enemySpeedChange);
    }
        
    public void onPaint(Graphics2D g) {
        g.setColor(Color.white);
        if(targetRobot!=null)
        g.drawString("Target: " + (int)targetRobot.distance + targetRobot.name, (int)my.x - 60 ,(int)my.y - 60);
        g.setColor(Color.yellow);

        //g.drawLine((int)my.x, (int)my.y, (int)nextPoint.x, (int)nextPoint.y);
        //g.drawOval((int)nextPoint.x-5, (int)nextPoint.y-5 ,10,10);

        //predicted points by circle()	
        g.setColor(Color.red);
        for(Point p: pa){	
        //g.drawString("" + (int)ps.size(),(int)p.x-5, (int)p.y-5);	
        g.drawOval((int)p.x-5, (int)p.y-5 ,10,10);	
        }

        //bullet fired by this robot
        /*	
        for(BulletInfo b:bulletmapping.returnbulletdata()){	
            if(b.bullet.isActive())	
                g.drawOval((int)b.bullet.getX()-5, (int)b.bullet.getY()-5 ,10,10);	
        }*/
    }
}