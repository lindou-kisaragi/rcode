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
    public boolean allScanned = false;
    private boolean writeEnable = true;

    public  EnemyDataManager(Map<String, Enemy> eMap){
       enemyMap = eMap;
    }

    public void ScannedRobot(Enemy enemy) {
        double lastScanTick = enemy.time;
        Enemy prevEnemy = enemyMap.get(enemy.name);

        if ( prevEnemy == null )  { // The first time
            setName(enemy.name);
            enemy.alive = true;
            if ( enemy.energy > 120 ) {
                enemy.role = RobotInfo.ROLE_LEADER;
            }else if ( enemy.energy > 100 ) {
                enemy.role = RobotInfo.ROLE_DROID;
            }else {
                enemy.role = RobotInfo.ROLE_ROBOT;
            }
        }else{
            if(enemyMap.size() == Util.enemies){
                allScanned = true;
            }
            if(prevEnemy.alive){
                enemy.alive = true;
            }else{
                enemy.alive = false;
            }
        }
        
        if(writeEnable){
            enemyMap.put(enemy.name, enemy);
        }
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

    public String setTarget(){
        double energy1 = -1;
        double energy2 = -1;
        String name = new String();

        for(Map.Entry<String, Enemy> entry : enemyMap.entrySet()){
            Enemy enemy = entry.getValue();
            if(enemy.alive){
                name = entry.getKey();
                if(name.equals(leader)){
                    return leader;
                }
                if(name.equals(sub1) ){
                    energy1 = enemy.energy;
                }
                if(name.equals(sub2)){
                    energy2 = enemy.energy;
                }
            }
        }
        log();

        if(energy1 < 0 && energy2 < 0){
            return name;
        }
        else if(energy1 < 0 && energy2 > 0){
            return sub2;
        }else if(energy2 < 0 && energy1 >0){
            return sub1;
        }else if(energy1 >= energy2){
            return sub2;
        }else{
            return sub1;
        }
    }

    public void onRobotDeath(RobotDeathEvent e){
        Enemy enemy = enemyMap.get(e.getName());
        writeEnable = false;
        enemy.alive = false;
        System.out.println("(EnemyDataManager) robot death " + e.getName());
        enemyMap.put(enemy.name, enemy);
        writeEnable = true;
        log();
        /*
		if(enemy != null){
            enemyMap.remove(enemy.name);
		}*/
    }

    public Enemy get(String name){
        return enemyMap.get(name);
    }

    public void log() {
        System.out.println("Number of Enemy: " + enemyMap.size());
        for(Map.Entry<String, Enemy> entry : enemyMap.entrySet()){
            System.out.println(entry.getKey() + " : " + entry.getValue());
            System.out.print("   ");
            entry.getValue().log();
        }
    }

    public void onPaint(Graphics2D g){
        g.setColor(Color.yellow);
        for(Map.Entry<String, Enemy> entry : enemyMap.entrySet()){
            Enemy e = entry.getValue();
            if(e.alive){
                 g.fillRect((int)e.x-15 ,(int)e.y-15 ,30,30);     
            }       
        }
    }

    public boolean wallsAlive(){
        for (Map.Entry<String, Enemy> entry : enemyMap.entrySet()) {
            Enemy e = entry.getValue();
            if(e.alive && e.name.contains("Wall")){
                return true;
            }     
        }
        return false;
    }
}