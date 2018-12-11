package group13.bulletmap;

import group13.RobotInfo;
import group13.MyRobot;
import group13.Enemy;
import group13.EnemyDataManager;
import group13.Util;
import group13.bulletmap.BulletInfo;
import group13.bulletmap.BulletMapping;
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


public class Estimation{
    BulletMapping bulletmapping;
    public Estimation(BulletMapping bulletMapping){
        bulletmapping=bulletMapping;
    }
    public int EstimationPattern(Boolean isenemy){//isenemy: teki no bullet ka douka 
        double x1,x2,x3;//1~pattern kakuritu
        double answer,omikuji;
        x1=Softmax(1, isenemy);
        x2=Softmax(2, isenemy);
        x3=Softmax(3, isenemy);
        omikuji=Math.random();
        if(omikuji<x1){
            return 1;
        }else if(omikuji<x1+x2){
            return 2;
        }else{
            return 3;
        }
    }
    public double Softmax(int pattern,boolean isenemy){
        //xa ha kakuritu;
        double xa,x1,x2,x3;//1~pattern kakuritu
        double alpha=0;//keisuu//tyousetu hituyou!!!!!!!!!
        double f;//softmax kansuu
        xa=Sigmoid(pattern, isenemy);
        x1=Sigmoid(1, isenemy);
        x2=Sigmoid(2, isenemy);
        x3=Sigmoid(3, isenemy);
        f=Math.exp(xa/alpha)/(Math.exp(x1/alpha)+Math.exp(x2/alpha)+Math.exp(x3/alpha));
        return f;
    }
    public double Sigmoid(int pattern,boolean isenemy){
        //double x[];//hitotu no pattern no subete no bullet data.0 or 1
        double xa=0;//sigmoid
        int ishit;
        List<BulletInfo> x;
        if(isenemy){
            x=bulletmapping.returnbulletdata();
        }else{
            x=bulletmapping.returnbulletdatafriend();
        }
        
        for(int i=0;i<x.size();i++){//i ha jikan ni shita houga iikamo.
            if(x.get(i).pattern==pattern){
                if(x.get(i).ishit==true){
                    ishit=1;
                }else{
                    ishit=0;
                }
                xa=xa+1/2*ishit*(Math.tanh((x.size()-i+1.5)/7)+1);//tyousetu hituyou    
            }
        }
        return xa;
    }
    /*
    public double EstimationAngleEnemy(int i,int pattern,double accel,double speed,double angle){//mikansei
        List<BulletInfo> a=bulletmapping.returnbulletmap();
        if(pattern==1){//tousoku
            
        }else if(pattern==2){//toukasoku

        }else{//other

        }
        
    }*/
}