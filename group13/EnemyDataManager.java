package group13;

import group13.RobotInfo;
import group13.MyRobot;
import group13.Enemy;
import group13.AntiWall;
import group13.Util;

import robocode.*;
import robocode.util.Utils;
import robocode.ScannedRobotEvent;

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
    public  Map<String, Enemy> enemyMap = new HashMap<>(); ;
    
    /*protected void createMap(){
        enemyMap = new HashMap<>();
    }*/

    protected void ScannedRobot(Enemy enemy) {
        double lastScanTick = enemy.time;
        Enemy prevEnemy = enemyMap.get(enemy.name);

        if ( prevEnemy == null )  { // Tbhe first time
            if ( enemy.energy > 120 ) {
                enemy.role = Enemy.ROLE_LEADER;
            }else if ( enemy.energy > 100 ) {
                enemy.role = Enemy.ROLE_DROID;
            }else {
                enemy.role = Enemy.ROLE_ROBOT;
            }
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

    /*public boolean wallsAlive(){
        for (Map.Entry<String, Enemy> e : enemyMap.entrySet()) {
            if(enemy != null && enemy.name.contais("Wall")){
                return true;
            }
        }
        return false;
    }*/
}