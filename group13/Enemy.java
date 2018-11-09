package group13;

import group13.RobotInfo;
import robocode.*;
import robocode.Robot;

import java.lang.*;

/**
 * EnemyClass - a class by (your name here)
 */
public class Enemy extends RobotInfo
{
    static public final int ROLE_UNKNOWN = 0;
    static public final int ROLE_DROID   = 1;
    static public final int ROLE_ROBOT   = 2;
    static public final int ROLE_LEADER  = 3;

    public double bearing;
    public double distance;
    public boolean scanned;

    public Enemy(RobotInfo my, ScannedRobotEvent e) {
        //super(); // default constractor
        this.time = e.getTime();
        this.name = e.getName();
        double bearingRadians = (bearing + my.headingRadians)%(Math.PI*2);
        
        double angle = Math.toRadians((my.heading + e.getBearing()) % 360);
 
        double scannedX = (int)(my.x+ Math.sin(angle) * e.getDistance());
        double scannedY = (int)(my.y + Math.cos(angle) * e.getDistance());
        
        this.x = scannedX;
        this.y = scannedY;
        this.bearing = e.getBearing();
        this.distance = e.getDistance();
        this.heading = e.getHeading();
        this.headingRadians = e.getHeadingRadians();
        this.velocity = e.getVelocity();
        this.energy = e.getEnergy();
        this.scanned = true;
        this.role   = ROLE_UNKNOWN;
        this.heat = 0;
    }
}
