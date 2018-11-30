package appendix;

import robocode.*;
import java.util.*;

public class AntiGravTeam extends TeamRobot {

	final double PI = Math.PI;			

	LinerEnemyRad target = new LinerEnemyRad();	
    Hashtable targets = new Hashtable();
    Hashtable team = new Hashtable(); 

	Direction movDir = new Direction();			
	Direction sweepDir = new Direction();		
	Lib help = new Lib();							

	double firePower;       					
	double sweepKaku = PI;						

	public void run() {

		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);

		turnRadarRightRadians(2*PI);			
		while(true) {
			doAntiGravMove();			
			doScanner();			
			doGun();				
			if (firePower != 0 ) {fire(firePower);}
			execute();				
		}
	}

	void doAntiGravMove() {
   		double xforce = 0;
	    double yforce = 0;
	    double force;
	    double ang;
	    GravPoint p;
		Enemy en;
        Enumeration e = targets.elements();
        Enumeration ee = team.elements();

		while (e.hasMoreElements()) {
    	    en = (Enemy)e.nextElement();
			if (en.live) {
				final double ENEMYFORCE = -200;
				p = new GravPoint(en.x,en.y, ENEMYFORCE);
				force = p.power/Math.pow(help.getRange(getX(),getY(),p.x,p.y),2);
		        ang = help.normalAngle(PI/2 - Math.atan2(getY() - p.y, getX() - p.x)); 
		        xforce += Math.sin(ang) * force;
		        yforce += Math.cos(ang) * force;
			}
        }
        
        while (ee.hasMoreElements()) {
    	    en = (Enemy)ee.nextElement();
			if (en.live) {
				final double ENEMYFORCE = -1200;
				p = new GravPoint(en.x,en.y, ENEMYFORCE);
				force = p.power/Math.pow(help.getRange(getX(),getY(),p.x,p.y),2);
		        ang = help.normalAngle(PI/2 - Math.atan2(getY() - p.y, getX() - p.x)); 
		        xforce += Math.sin(ang) * force;
		        yforce += Math.cos(ang) * force;
			}
        }

	    final double WALLFORCE = 10000;
	    xforce += WALLFORCE/Math.pow(help.getRange(getX(), getY(), getBattleFieldWidth(), getY()), 3);
	    xforce -= WALLFORCE/Math.pow(help.getRange(getX(), getY(), 0, getY()), 3);
	    yforce += WALLFORCE/Math.pow(help.getRange(getX(), getY(), getX(), getBattleFieldHeight()), 3);
	    yforce -= WALLFORCE/Math.pow(help.getRange(getX(), getY(), getX(), 0), 3);
	    	
		double kaku = getHeadingRadians() + Math.atan2(yforce, xforce) - PI/2;
		int dir;
		if (kaku > PI/2) {
	        kaku -= PI;
	        dir = -1;
	    }
	    else if (kaku < -PI/2) {
	        kaku += PI;
	        dir = -1;
	    }
	    else {
	        dir = 1;
	    }

	    double changeInEnergy = target.previousEnergy - target.energy;
	    if (changeInEnergy != 0) {
	    	double moveDistance;
		if (target.distance > 400) {
			moveDistance = 100;
		} else {
			moveDistance = 300;
		}
	    setTurnRightRadians(kaku);
	    setAhead(moveDistance * dir);
	    }

	}
			
	void doScanner() {
		setTurnRadarLeftRadians(2*PI);
	}

	void doGun() {

		firePower = 0;	

		if (target.setNextXY(getX(), getY()) == true) {
			double nextX = target.nextX - getX();
			double nextY = target.nextY - getY();
			double kaiten = PI/2 - Math.atan2(nextY, nextX);
			setTurnGunRightRadians(help.normalAngle(kaiten - getGunHeadingRadians()));
			
			if (target.nextX > 0 
				&& target.nextY > 0 
				&& target.nextX < getBattleFieldWidth() 
				&& target.nextY < getBattleFieldHeight()) {
					firePower = 1;
			}
		}
	}
	
	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {

		double absbearing_rad = (getHeadingRadians()+e.getBearingRadians())%(2*PI);

        LinerEnemyRad en;
        
        if (isTeammate(e.getName()) && team.containsKey(e.getName())) {
            en = (LinerEnemyRad)team.get(e.getName());
        } else if ( isTeammate(e.getName()) ){
            en = new LinerEnemyRad();
			team.put(e.getName(),en);
        } else if (targets.containsKey(e.getName())) {
			en = (LinerEnemyRad)targets.get(e.getName());
		} else {
			en = new LinerEnemyRad();
			targets.put(e.getName(),en);
		}

		en.name = e.getName();
		en.x = getX()+Math.sin(absbearing_rad)*e.getDistance();
		en.y = getY()+Math.cos(absbearing_rad)*e.getDistance();
		en.bearing = e.getBearingRadians();
		en.head = e.getHeadingRadians();
		en.checkTime = getTime();			
		en.speed = e.getVelocity();
		en.distance = e.getDistance();

		en.live = true;
		if ((en.distance < target.distance)||(target.live == false)) {
			if (!(isTeammate(en.name))){
				target = en;
			}
		}

		en.setEnergy(e.getEnergy());		
	}

	public void onHitByBullet(HitByBulletEvent e) {
		turnRadarRight(getHeading() + e.getBearing() - getRadarHeading());
	}

	public void onRobotDeath(RobotDeathEvent e) {
		Enemy en = (Enemy)targets.get(e.getName());
		en.live = false;		
	}	

}
