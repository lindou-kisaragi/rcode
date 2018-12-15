package group13;

import group13.RobotInfo;
import group13.MyRobot;
import group13.Enemy;
import group13.EnemyDataManager;
import group13.AntiWall;
import group13.AntiWallMove;
import group13.Util;
import group13.bulletmap.*;

import robocode.*;
import robocode.util.Utils;
import robocode.ScannedRobotEvent;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * BaseRobot 
 */
public class BaseRobot extends TeamRobot
{
	public Enemy enemy;
	public  Map<String, Enemy> enemyMap = new HashMap<>(); 
	public EnemyDataManager  enemyDataManager = new EnemyDataManager(enemyMap);
	public Map<String, MyRobot> mateMap = new HashMap<>();
	public MyRobot my = new MyRobot();
	
	public BulletMapping bulletmapping = new BulletMapping(my);
	
	public void run() {
		// Initialization of the robot should be put here

		setBodyColor(Color.white);
		setGunColor(Color.white);
		setRadarColor(Color.white);
		initRound();

		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		updateMyInfo();

		if(!isTeammate(e.getName()) ){
			enemy = new Enemy(my, e); 
			Enemy prevEnemy = enemyMap.get(enemy.name);
			//bulletmapping.ShootObservation(enemy, prevEnemy);
            enemyDataManager.ScannedRobot(enemy); //update enemy's info
            broadcastMessage(enemy);
		}
	}

	public void  initRound(){
		my.name = getName();
        if(my.name.contains("Leader")){
            my.role = RobotInfo.ROLE_LEADER;
        }else{
            my.role = RobotInfo.ROLE_ROBOT;
        }

		String[] teammates = getTeammates();
		if (teammates != null){
			for (String member : teammates){
				mateMap.put(member, new MyRobot());
			}
		}
		Util.enemies = getOthers() - 2;
		Util.battleFieldWidth = getBattleFieldWidth();
		Util.battleFieldHeight = getBattleFieldHeight();
	}

	public void updateMyInfo(){
		my.time = getTime();
		my.x = getX();
		my.y = getY();
		my.heading = getHeading();
		my.headingRadians = getHeadingRadians();
		my.gunHeading = getGunHeading();
		my.gunHeadingRadians = getGunHeadingRadians();
		my.radarHeading = getRadarHeading();
		my.velocity = getVelocity();
		my.energy = getEnergy();
        my.heat = getGunHeat();
        broadcastMessage(my);
	}
	
	public void onHitByBullet(HitByBulletEvent e){
			bulletmapping.BulletHittedbyEnemy(e.getName());
	}

	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		back(20);
	}	

	public void onRobotDeath(RobotDeathEvent e){
		if(!isTeammate(e.getName()) ){
			enemyDataManager.onRobotDeath(e);
		}
	}

	public void onMessageReceived(MessageEvent e){
		Serializable message = e.getMessage();

		if(message instanceof Enemy ){
			Enemy enemy = (Enemy)message;
			enemyDataManager.ScannedRobot(enemy); 
		}else if(message instanceof MyRobot){
			MyRobot mate = (MyRobot)message;
			mateMap.put(mate.name ,mate);
		}
    }
    
    @Override
    public void broadcastMessage(Serializable s ){
        try {
            super.broadcastMessage(s);
        } catch (IOException ex) {

        }
	}

	// Paint a transparent square on top of the last scanned robot
    public void onPaint(Graphics2D g) {
		try {
			//enemyDataManager.onPaint(g);
		} catch (RuntimeException e) {
			//TODO: handle exception
		}
		
		//Gun heading
        g.setColor(Color.red);
	    g.drawLine((int)getX(), (int)getY(), 
	    (int)(Math.sin(getGunHeadingRadians()) * 800 + getX()),(int)(Math.cos(getGunHeadingRadians()) * 800 + getY())
	    );

		//heading
	    g.setColor(Color.cyan);
	    g.drawLine((int)getX(), (int)getY(), 
	    (int)(Math.sin(getHeadingRadians()) * 800 + getX()),(int)(Math.cos(getHeadingRadians()) * 800 + getY())
	    );

	}
}
