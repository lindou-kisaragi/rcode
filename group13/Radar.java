package group13;

import group13.Point;
import group13.RobotInfo;
import group13.MyRobot;
import group13.Enemy;
import group13.Util;

import robocode.*;
import robocode.AdvancedRobot;
import robocode.util.Utils;

import java.io.*;
import java.lang.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Radar - a class by (t-yoppi)
 */
public class Radar{
    public  Map<String, Enemy> enemyMap = new HashMap<>();

    public TeamRobot robot;
    public MyRobot my;
    private double radarturnamount;
    public Enemy targetRobot;
    public boolean lockon = false;
    private int sign = 1;

    public Radar( TeamRobot _robot, MyRobot _my,
         Map<String, Enemy> eMap){
            robot = _robot;
            my =_my;
            enemyMap = eMap;
    }

    public void setTarget(Enemy target){
      targetRobot = new Enemy();
      targetRobot = target;
      lockon = true;
    }


    public void execute(){
        if(!lockon){
			      radarturnamount = 90;
			  }
        else{
          radarturnamount = 2 * Utils.normalRelativeAngleDegrees(my.heading + targetRobot.bearing - my.radarHeading);
        }

        if(targetRobot != null){
            Enemy enemy = enemyMap.get(targetRobot);
        }

        robot.setTurnRadarRight(radarturnamount + (30 * sign));
        sign = sign * (-1);

        /*if(lockon){
          robot.setTurnRadarRight(30 * sign);
          robot.turnRadarRight(radarturnamount);
          sign = sign * (-1);
        }*/

        lockon = false;

    }



}
