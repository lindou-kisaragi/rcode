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
	//private TeamDataManager teamDataManager = new TeamDataManager();

  	public double radarTurnAmount;
    public double gunTurnAmount;

	public boolean lockOn;
	double t;
	public boolean islastshot = false;
	private MyRobot my = new MyRobot();
	
	private AntiWall aw = new AntiWall(this, my);
	private AntiWallMove awMove = new AntiWallMove(this,enemyMap);
 
	public void run() {
		// Initialization of the robot should be put here
		initRound();

		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
	
		// Robot main loop
		while(true) {
		if(!lockOn){
			radarTurnAmount = 90;
		}
		  lockOn = false;

		  updateMyInfo();
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
        	Enemy prevEnemy = enemyDataManager.get(enemy.name);
			lockOn = true;

			//antiWall !!
			if(e.getName().contains("Wall")){
			aw.onScannedRobot(enemy);
			//radarTurnAmount = 2 * Utils.normalRelativeAngleDegrees(my.heading+ enemy.bearing - getRadarHeading());
			//gunTurnAmount = aw.prepareFire(my,enemy);

				islastshot = Util.isLastShot(3,enemy.energy);
				//if(!aw.holdFire)setFire(3);
			}

			setTurnRight(e.getBearing()); 
			setAhead(e.getDistance() - 200);
			enemyDataManager.ScannedRobot(enemy); //update enemy's info
			enemyDataManager.log();
		}

		/*System.out.println(getTime() + "/" +
		                   " name :" + enemy.name+
						   " position :" + enemy.x + "," + enemy.y +
		                   " energy :" + enemy.energy +
						   " bearing :" + enemy.bearing +
						   " distance :" + enemy.distance +
                           " heading :" +  enemy.headingRadians+
                           " velocity :" + enemy.velocity + 
						   " time :" + t
		);*/
	}

	public void  initRound(){
		my.role = Enemy.ROLE_LEADER;
		my.name = getName();
	}

	public void updateMyInfo(){
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

		}
	}

	// Paint a transparent square on top of the last scanned robot
    public void onPaint(Graphics2D g) {
		try {
			aw.onPaint(g,my,enemy);		
		} catch (RuntimeException e) {
			//TODO: handle exception

		}
		
    // Set the paint color to a red half transparent color
    g.setColor(Color.red);
	
    // Draw a line from our robot to the scanned robot
    //g.drawLine(enemy.x, enemy.y, (int)getX(), (int)getY());
	// Draw a filled square on top of the scanned robot that covers it
    g.fillRect((int)enemy.x - 20, (int)enemy.y - 20, 40, 40);
	g.drawLine((int)getX(), (int)getY(), 
	(int)(Math.sin(getGunHeadingRadians()) * 800 + getX()),(int)(Math.cos(getGunHeadingRadians()) * 800 + getY())
	);

	g.setColor(Color.blue);
	g.drawLine((int)getX(), (int)getY(), 
	(int)(Math.sin(getHeadingRadians()) * 800 + getX()),(int)(Math.cos(getHeadingRadians()) * 800 + getY())
	);

	}
}
