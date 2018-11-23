package group13;

import group13.BaseRobot;
import group13.RobotInfo;
import group13.MyRobot;
import group13.Enemy;
import group13.EnemyDataManager;
import group13.Move;
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
public class G13_Sub2 extends BaseRobot
{
	/*super class's variable you can use*/
	//public Enemy enemy;
	//public  Map<String, Enemy> enemyMap = new HashMap<>(); 
	//public EnemyDataManager  enemyDataManager = new EnemyDataManager(enemyMap);
	//public MyRobot my = new MyRobot();
	
	private double radarTurnAmount;

	private Move move = new Move(this, my, enemyMap, mateMap);
	public boolean lockon;
	public String target;

	@Override
    public void run() {
		super.run();
		// Robot main loop
		while(true) {
			if(!lockon){
				radarTurnAmount = 90;
			}

			System.out.println(" ");
			System.out.println("time:" + getTime());

			super.updateMyInfo();
			setTurnRadarRight(radarTurnAmount);
			lockon =false;

			move.execute();
			execute();
		  }
	}

	
	@Override
	public void onScannedRobot(ScannedRobotEvent e) {
		super.onScannedRobot(e);
		setTarget();
		if(!isTeammate(e.getName()) ){
			System.out.println(enemy.name);
			System.out.println("bearing" + e.getBearing());
			if(target.equals(enemy.name)){
			System.out.println("lockon!!!!!!!!");
			lockon = true;
			radarTurnAmount = 2 * Utils.normalRelativeAngleDegrees(my.heading + enemy.bearing - my.radarHeading);
			}
		}
	}

	public void setTarget(){
		target = enemyDataManager.leader;
		if(target != null)
		move.setTarget(target);
	}

	public void searchEnemy(String target) {
		
	}

	@Override
	public void onPaint(Graphics2D g){
		super.onPaint(g);
	}
	
}
