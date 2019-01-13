package group13;

import group13.BaseRobot;
import group13.RobotInfo;
import group13.MyRobot;
import group13.Enemy;
import group13.EnemyDataManager;
import group13.Move;
import group13.Gun;
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
public class G13_Sub1 extends BaseRobot
{
	/*super class's variable you can use*/
	//public Enemy enemy;
	//public Map<String, Enemy> enemyMap = new HashMap<>(); 
	//public EnemyDataManager  enemyDataManager = new EnemyDataManager(enemyMap);
	//public Map<String, MyRobot> mateMap = new HashMap<>();
	//public MyRobot my = new MyRobot();
	//public Radar radar = new Radar(this, my, enemyMap);
	//public Move move = new Move(this, my, enemyMap, mateMap);
	//public BulletMapping bulletmapping = new BulletMapping(my, move, this);
	//public Gun gun = new Gun(this, my, move, enemyMap, bulletmapping);
	
	public boolean lockon;
	public String target;
	public boolean targetSet = false;
	private boolean running = true;

	@Override
    public void run() {
		super.run();

		// Robot main loop
		while(running) {
			System.out.println(" ");
			System.out.println("time:" + getTime());
			System.out.println("target :" + target);
			
			super.updateMyInfo();

			radar.execute();
			move.execute();
			gun.execute();
			execute();
		  }
	}

	@Override
	public void onScannedRobot(ScannedRobotEvent e) {
		super.onScannedRobot(e);
	
		if(!isTeammate(e.getName()) ){
			if(!targetSet && enemyDataManager.allScanned){
				setTarget();
			}

			if(targetSet && target.equals(enemy.name)){
				System.out.println("lockon!!!!!!!!");
				radar.setTarget(enemy);
				move.setTarget(enemy);
				gun.setTarget(enemy);
			}
		}
	}
	
	public void setTarget(){
		target = enemyDataManager.setTarget();
		//enemyDataManager.log();
		System.out.println("target Set!!!!!!!!! " + target);

		if(target != null){
			targetSet = true;
		}
	}

	public void onRobotDeath(RobotDeathEvent e){
		super.onRobotDeath(e);
		if(!isTeammate(e.getName())){
			setTarget();
		}
	}

	public void onWin(WinEvent e){
		running = false;
	}
	
	@Override
	public void onPaint(Graphics2D g){
		super.onPaint(g);
	}
}
