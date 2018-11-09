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
 * G13_Sub2 - a robot by (your name here)
 */
public class G13_Sub2 extends TeamRobot
{
	/**
	 * run: G13_Sub2's default behavior
	 */
	private EnemyDataManager  enemyDataManager= new EnemyDataManager();
	private double radarTurnAmount;
	private Enemy enemy;
	private MyRobot my = new MyRobot();

    public void run() {
        setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
        setAdjustRadarForRobotTurn(true);
        while(true) {

		updateMyInfo();
            radarTurnAmount = 90;
            setTurnRadarRight(radarTurnAmount);
            execute();
    }
	
}
	public void onScannedRobot(ScannedRobotEvent e) {
		enemy = new Enemy(my, e); 
		enemyDataManager.ScannedRobot(enemy); //update enemy's info
        enemyDataManager.log();
	}
	
	public void updateMyInfo(){
		my.x = getX();
		my.y = getY();
		my.heading = getHeading();
		my.headingRadians = getHeadingRadians();
		my.gunHeading = getGunHeading();
		my.velocity = getVelocity();
		my.energy = getEnergy();
		my.heat = getGunHeat();
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
}
