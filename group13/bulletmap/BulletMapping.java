package group13.bulletmap;

import group13.RobotInfo;
import group13.MyRobot;
import group13.bulletmap.BulletInfo;
import group13.Enemy;
import group13.EnemyDataManager;
import group13.Util;
import group13.bulletmap.*;

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

import javax.lang.model.util.ElementScanner6;
//mikata bullet mijissou. tama meityuu 
public class BulletMapping
{
    MyRobot my;
    List<BulletInfo> bulletmap = new ArrayList<BulletInfo>();
    List<BulletInfo> bulletmapfriend = new ArrayList<BulletInfo>();//mikata
    List<BulletInfo> bulletdata = new ArrayList<BulletInfo>();//history
    List<BulletInfo> bulletdatafriend = new ArrayList<BulletInfo>();//history
    public BulletMapping(MyRobot my1){
        my=my1;
    }
    //Map<String, Double> bulletmap = new HashMap<String, Double>();
    //bulletmap.put(,);
    
    public void ShootObservation(Enemy enemy,Enemy prevenemy){
        BulletInfo bullet=new BulletInfo();
        if(enemy.energy<prevenemy.energy){
            //shoot!!detect!
            //need pattern
            bullet.name=enemy.name;
            bullet.x=enemy.x;
            bullet.y=enemy.y;
            bullet.prex=prevenemy.x;
            bullet.prey=prevenemy.y;
            bullet.speed=20-3*(prevenemy.energy-enemy.energy);
            bullet.t=enemy.time;
            bullet.tprev=prevenemy.time;
            bullet.myx=my.x;
            bullet.myy=my.y;
        }
        bulletmap.add(bullet);
    }
    public void BulletDelete(){
        for(int i=0;i<=bulletmap.size()-1;i++){
            if(bulletmap.get(i).t*bulletmap.get(i).speed>Util.battleFieldWidth*1.4142){
                bulletmap.get(i).ishit=false;
                BulletInfo bullet=new BulletInfo();
                bullet=deepcopy(bulletmap.get(i));
                bulletmap.remove(i);
                i=i-1;
            }
        }
    }
    public double BulletRadiusShort(int i){
        return (bulletmap.get(i).speed*(my.time-bulletmap.get(i).t));
    }
    public double BulletRadiusLong(int i){
        return (bulletmap.get(i).speed*(my.time-bulletmap.get(i).tprev));
    }
    public double Distance(int i){
        return Math.pow(Math.pow(bulletmap.get(i).x-my.x,2)+Math.pow(bulletmap.get(i).x-my.x,2), 0.5);
    }
    public void FriendBulletMove(){
        
    }
    @SuppressWarnings("unchecked")
    public static <T> T deepcopy(T obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try{
            new ObjectOutputStream(baos).writeObject(obj);
            return (T) new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())).readObject();
        }catch(Exception e){
            return null;
        }
        
    }
    public Double CalculateAngle1(BulletInfo bullet){
        double k,X,Y;
        k=((bullet.myx-bullet.prex)*(bullet.prex-my.x)+(bullet.myy-bullet.prey)*(bullet.prey-my.y))
        /(Math.pow(bullet.myx-bullet.prex,2))+(Math.pow(bullet.myy-bullet.prey,2));
        X=k*(bullet.myx-bullet.prex);
        Y=k*(bullet.myy-bullet.prey);
        double diff_X = Math.pow(Math.pow(X+bullet.prex-bullet.prex,2)+Math.pow(Y+bullet.prey-bullet.prey,2), 0.5);
        double diff_Y = Math.pow(Math.pow(X+bullet.prex-my.x,2)+Math.pow(Y+bullet.prey-my.y,2), 0.5);
        if(X*(bullet.myy-bullet.prey)>Y*(bullet.myx-bullet.prex)){
            if(bullet.myx>0){
                return Math.atan(diff_X/diff_Y)/Math.PI*180;
            }else{
                return Math.atan(diff_X/diff_Y)/Math.PI*180 + 180;
            }
        }else{
            if(bullet.myx>0){
                return Math.atan(diff_X/diff_Y)/Math.PI*180 + 180;
            }else{
                return Math.atan(diff_X/diff_Y)/Math.PI*180;
            }
        }
    }
    public Double CalculateAngle2(BulletInfo bullet){
        double k,X,Y;
        k=((bullet.myx-bullet.x)*(bullet.x-my.x)+(bullet.myy-bullet.y)*(bullet.y-my.y))
        /(Math.pow(bullet.myx-bullet.x,2))+(Math.pow(bullet.myy-bullet.y,2));
        X=k*(bullet.myx-bullet.x);
        Y=k*(bullet.myy-bullet.y);
        double diff_X = Math.pow(Math.pow(X+bullet.x-bullet.x,2)+Math.pow(Y+bullet.y-bullet.y,2), 0.5);
        double diff_Y = Math.pow(Math.pow(X+bullet.x-my.x,2)+Math.pow(Y+bullet.y-my.y,2), 0.5);
        if(X*(bullet.myy-bullet.y)>Y*(bullet.myx-bullet.x)){
            if(bullet.myx>0){
                return Math.atan(diff_X/diff_Y)/Math.PI*180;
            }else{
                return Math.atan(diff_X/diff_Y)/Math.PI*180 + 180;
            }
        }else{
            if(bullet.myx>0){
                return Math.atan(diff_X/diff_Y)/Math.PI*180 + 180;
            }else{
                return Math.atan(diff_X/diff_Y)/Math.PI*180;
            }
        }
    }
    public void BulletHittedbyEnemy(String enemyname){
        BulletInfo bullet=new BulletInfo();
        int j=0;
        for(int i=0; i<=bulletmap.size();i++){
        //for(BulletInfo b:bulletmap){
            if(bulletmap.get(i).name==enemyname){
                if(BulletRadiusLong(i)>=Distance(i) && BulletRadiusShort(i)<=Distance(i)){
                    j=i;
                    break;
                }
            }
        }
        bullet=deepcopy(bulletmap.get(j));
        bullet.angle1=CalculateAngle1(bullet);
        bullet.angle2=CalculateAngle2(bullet);
        bullet.ishit=true;

        bulletdata.add(bullet);
    }
}

