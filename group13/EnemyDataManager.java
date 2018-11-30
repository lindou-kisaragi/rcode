package group13;

import group13.RobotInfo;
import group13.MyRobot;
import group13.Enemy;
import group13.AntiWall;
import group13.Util;

import robocode.*;
import robocode.util.Utils;
import robocode.ScannedRobotEvent;

import java.awt.Color;
import java.awt.Graphics2D;
import java.lang.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MyClass - a class by (your name here)
 */
public class EnemyDataManager
{
    public  Map<String, Enemy> enemyMap = new HashMap<>(); 
    
    public  String leader;
    public  String sub1;
    public  String sub2;

   public  EnemyDataManager(Map<String, Enemy> eMap){
       enemyMap = eMap;
   }

    /*protected void createMap(){
        enemyMap = new HashMap<>();
    }*/

    public void ScannedRobot(Enemy enemy) {
        double lastScanTick = enemy.time;
        Enemy prevEnemy = enemyMap.get(enemy.name);


        if ( prevEnemy == null )  { // The first time
            setName(enemy.name);
            if ( enemy.energy > 120 ) {
                enemy.role = RobotInfo.ROLE_LEADER;
            }else if ( enemy.energy > 100 ) {
                enemy.role = RobotInfo.ROLE_DROID;
            }else {
                enemy.role = RobotInfo.ROLE_ROBOT;
            }
        }else{
            //System.out.print("(Enemydatamanager:)");
            //prevEnemy.log();
        }
        /*if ( prevEnemy != null ) {
            if ( prevEnemy.time == enemy.time ) {
                return null;
            }else if ((enemy.time-prevEnemy.timeStamp) < SCAN_STALE ) {
                enemy.role = prevEnemy.role;
                enemy.setPrev(prevEnemy);
            }*/
        
        enemyMap.put(enemy.name, enemy);
        //return enemy;
    }

    public void setName(String name) {
        if(name.contains("Leader")){
            leader = name;
        }else if(name.contains("Sub1")){
            sub1 = name;
        }else if(name.contains("Sub2")){
            sub2 = name;
        }
    }

    public void onRobotDeath(RobotDeathEvent e){
        Enemy enemy = enemyMap.get(e.getName());
		if(enemy != null){
			enemy.alive = false;
		}
    }

    public Enemy get(String name){
        return enemyMap.get(name);
    }

    public void log() {
        for(Map.Entry<String, Enemy> entry : enemyMap.entrySet()){
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    public void onPaint(Graphics2D g){
        g.setColor(Color.yellow);
        for(Map.Entry<String, Enemy> entry : enemyMap.entrySet()){
            Enemy e= entry.getValue();
            if(e.alive) g.fillRect((int)e.x-15 ,(int)e.y-15 ,30,30);            
        }
    }

    /*public boolean wallsAlive(){
        for (Map.Entry<String, Enemy> e : enemyMap.entrySet()) {
            if(enemy != null && enemy.name.contais("Wall")){
                return true;
            }
        }
        return false;
    }*/
}