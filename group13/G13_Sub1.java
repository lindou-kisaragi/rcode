package group13;

import group13.RobotInfo;
import group13.MyRobot;
import group13.Enemy;
import group13.EnemyDataManager;
import group13.AntiWall;
import group13.AntiWallMove;
import group13.Util;

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
 * G13_Sub1 - a robot by (your name here)
 */
public class G13_Sub1 extends TeamRobot
{
	/**
	 * run: G13_Sub1's default behavior
	 */
	//public Enemy enemy;
	public  Map<String, Enemy> enemyMap = new HashMap<>(); 
	private EnemyDataManager  enemyDataManager = new EnemyDataManager(enemyMap);

	private double radarTurnAmount;
	private MyRobot my = new MyRobot();

	private AntiWall aw = new AntiWall(this, my);
	//private AntiWallMove awMove = new AntiWallMove(this,enemyMap);

    public void run() {
        setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
        setAdjustRadarForRobotTurn(true);
        while(true) {

		updateMyInfo();
			aw.execute();
			
            execute();
    }
	
}
	public void onScannedRobot(ScannedRobotEvent e) {
		updateMyInfo();

		if(!isTeammate(e.getName()) ){
		Enemy enemy = new Enemy(my, e); 

		if(e.getName().contains("Wall")){
			aw.onScannedRobot(enemy);
		}

		enemyDataManager.ScannedRobot(enemy); //update enemy's info
		enemyDataManager.log();
		setTurnRight(e.getBearing()); 
		setAhead(e.getDistance() - 200);

		broadcastMessage(enemy);
		}
	}

	public void updateMyInfo(){
		my.time =getTime();
		my.x = getX();
		my.y = getY();
		my.heading = getHeading();
		my.headingRadians = getHeadingRadians();
		my.gunHeading = getGunHeading();
		my.radarHeading = getRadarHeading();
		my.velocity = getVelocity();
		my.energy = getEnergy();
		my.heat = getGunHeat();
		broadcastMessage(my);
	}
	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		back(10);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		back(20);
	}	

	@Override
    public void broadcastMessage(Serializable s ){
        try {
            super.broadcastMessage(s);
        } catch (IOException ex) {

        }
	}

}
