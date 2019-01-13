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
 * G13_Leader - a robot by group13
 */
public class G13_Leader extends BaseRobot
{
	/*super class's variable you can use*/
	//public Enemy enemy;
	//public  Map<String, Enemy> enemyMap = new HashMap<>(); 
	//public EnemyDataManager  enemyDataManager = new EnemyDataManager(enemyMap);
	//public Map<String, MyRobot> mateMap = new HashMap<>();
	//public MyRobot my = new MyRobot();
	//public BulletMapping bulletmapping = new BulletMapping(my);

  	public double radarTurnAmount;

	public String target;
	public boolean targetSet = false;
	public boolean lockOn;
	public boolean islastshot = false;
	
	@Override
	public void run() {
		super.run();
		// Robot main loop
		while(true) {
		  updateMyInfo();

		System.out.println(" ");
		System.out.println("time:" + getTime());
		System.out.println("target :" + target);

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

			//antiWall !!
			if(!targetSet && e.getName().contains("Wall")){
				target = enemy.name;
				targetSet = true;
				//aw.onScannedRobot(enemy);
				radar.setTarget(enemy);
				move.setTarget(enemy);
				gun.setTarget(enemy);
				islastshot = Util.isLastShot(3,enemy.energy);
			}else if(targetSet && target.equals(enemy.name)){
				radar.setTarget(enemy);
				move.setTarget(enemy);
				gun.setTarget(enemy);
			}
		}
	}

	public void setTarget(){
		target = enemyDataManager.setTarget();
		System.out.println("target Set!!!!!!!!! " + target);

		if(target != null){
			targetSet = true;
		}
	}

	@Override
	public void onRobotDeath(RobotDeathEvent e){
		super.onRobotDeath(e);
		if(target.equals(e.getName())){
			targetSet = false;
		}
		
		if(!enemyDataManager.wallsAlive()){
			setTarget();
		}
	}

	// Paint a transparent square on top of the last scanned robot
    public void onPaint(Graphics2D g) {
		super.onPaint(g);
	}
}
