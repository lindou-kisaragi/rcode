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
 * G13_Leader - a robot by group13
 */
public class G13_Leader extends TeamRobot
{
	private Enemy enemy;
	public  Map<String, Enemy> enemyMap = new HashMap<>(); 
	private EnemyDataManager  enemyDataManager = new EnemyDataManager(enemyMap);
	public Map<String, MyRobot> mateMap = new HashMap<>();
	//private TeamDataManager teamDataManager = new TeamDataManager();

  	public double radarTurnAmount;
    public double gunTurnAmount;

	public boolean lockOn;
	double t;
	public boolean islastshot = false;
	public MyRobot my = new MyRobot();
	
	private AntiWall aw = new AntiWall(this, my);
	public AntiWallMove awMove = new AntiWallMove(this,my,enemyMap,mateMap);
 
	public void run() {
		// Initialization of the robot should be put here
		initRound();

		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		//setAdjustRadarForRobotTurn(true);
	
		// Robot main loop
		while(true) {
		if(!lockOn){
			radarTurnAmount = 90;
		}
		  lockOn = false;

		  updateMyInfo();

		  awMove.execute();
		  awMove.log();	

		  aw.execute();

		  System.out.println("time:" + getTime());

		  setMaxVelocity(8);
		  execute();
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		updateMyInfo();

		if(!isTeammate(e.getName()) ){
			enemy = new Enemy(my, e); 

			//Enemy prevEnemy = enemyDataManager.get(enemy.name);
			Enemy prevEnemy = enemyMap.get(enemy.name);
			
			lockOn = true;

			enemyDataManager.ScannedRobot(enemy); //update enemy's info
			//enemyDataManager.log();

			//antiWall !!
			if(e.getName().contains("Wall")){
				aw.onScannedRobot(enemy);
				awMove.setTarget(enemy.name);
				islastshot = Util.isLastShot(3,enemy.energy);
				//if(!aw.holdFire)setFire(3);
				//setTurnRight(e.getBearing());
				//setAhead(e.getDistance()-100);
			}
		}

	}

	public void  initRound(){
		my.role = Enemy.ROLE_LEADER;
		my.name = getName();

		String[] teammates = getTeammates();
		if (teammates != null){
			for (String member : teammates){
				mateMap.put(member, new MyRobot());
			}
		}

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
		my.radarHeading = getRadarHeading();
		my.velocity = getVelocity();
		my.energy = getEnergy();
		my.heat = getGunHeat();
	}
	
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		back(10);
	}

	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		back(20);
	}	

	public void onRobotDeath(RobotDeathEvent e){
		enemyDataManager.onRobotDeath(e);
	}

	public void onMessageReceived(MessageEvent e){
		Serializable message = e.getMessage();

		if(message instanceof Enemy ){
			Enemy enemy = (Enemy)message;
			enemyDataManager.ScannedRobot(enemy); 
		}else if(message instanceof MyRobot){
			MyRobot mate = (MyRobot)message;
			mateMap.put(mate.name ,mate);
			//System.out.println("mate.time: " + mate.time + "my.time" +my.time);
		}
	}

	// Paint a transparent square on top of the last scanned robot
    public void onPaint(Graphics2D g) {
		try {
			aw.onPaint(g,my,enemy);		
			awMove.onPaint(g);
			enemyDataManager.onPaint(g);
		} catch (RuntimeException e) {
			//TODO: handle exception
		}
		
    g.setColor(Color.red);
	g.drawLine((int)getX(), (int)getY(), 
	(int)(Math.sin(getGunHeadingRadians()) * 800 + getX()),(int)(Math.cos(getGunHeadingRadians()) * 800 + getY())
	);

	g.setColor(Color.blue);
	g.drawLine((int)getX(), (int)getY(), 
	(int)(Math.sin(getHeadingRadians()) * 800 + getX()),(int)(Math.cos(getHeadingRadians()) * 800 + getY())
	);

	}
}
