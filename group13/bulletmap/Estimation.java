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

import java.util.Objects;

public class Estimation{
    BulletMapping bulletmapping;
    List<BulletInfo> xlog=new ArrayList<BulletInfo>();
    public Estimation(BulletMapping bulletMapping){
        bulletmapping=bulletMapping;
    }
    public int EstimationPattern(Boolean isenemy){//isenemy: teki no bullet ka douka 
        double x0,x1,x2,x3;//1~pattern kakuritu
        double answer,omikuji;
        x0=Softmax(0, isenemy);
        x1=Softmax(1, isenemy);
        x2=Softmax(2, isenemy);
        x3=Softmax(3, isenemy);
        omikuji=Math.random()*(x0+x1+x2+x3);
        if(x0==1000||x0==0)return 0;
        if(x1==1000||x1==0)return 1;
        if(x2==1000||x2==0)return 2;
        if(x3==1000||x3==0)return 3;
        if(omikuji<x0){
            return 0;
        }else if(omikuji<x0+x1){
            return 1;
        }else if(omikuji<x0+x1+x2){
            return 2;
        }else{
            return 3;
        }
    }
    public double Softmax(int pattern,boolean isenemy){
        //xa ha kakuritu;
        double xa,x0,x1,x2,x3;//1~pattern kakuritu
        double alpha=0.1;//keisuu//tyousetu hituyou!!!!!!!!!
        double f;//softmax kansuu
        xa=Sigmoid(pattern, isenemy);
        x0=Sigmoid(0, isenemy);
        x1=Sigmoid(1, isenemy);
        x2=Sigmoid(2, isenemy);
        x3=Sigmoid(3, isenemy);
        System.out.println("x0: " + x0);
        System.out.println("x1: " + x1);
        System.out.println("x2: " + x2);
        System.out.println("x3: " + x3);
        if(x0==1000||x1==1000||x2==1000||x3==1000)return 1000;
        f=Math.exp(xa/alpha)/(Math.exp(x0/alpha))+(Math.exp(x1/alpha)+Math.exp(x2/alpha)+Math.exp(x3/alpha));
        return f;
    }
    public double Sigmoid(int pattern,boolean isenemy){
        //double x[];//hitotu no pattern no subete no bullet data.0 or 1
        double xa=0;//sigmoid
        int ishit;
        List<BulletInfo> x=new ArrayList<BulletInfo>();
        if(isenemy){
            x=bulletmapping.returnbulletdata();
        }else{
            x=bulletmapping.returnbulletdatafriend();
        }
        //xlog=x;
        //System.out.println("fuuuuuuuuuuuuuuuuuuuuuuuckkkkkkkkkkkkkkkkkkkkkkk");
        //System.out.println(x);
        //try{
            if(x==null)return 1000;
            else if( x.size()==0)return 1000;
            else{
                //System.out.println(x.size());
            for(int i=0;i<x.size();i++){//i ha jikan ni shita houga iikamo.
                if(x.get(i)==null){
                    return 1000;
                }
                if(x.get(i).pattern==pattern){
                    System.out.println(x.get(i).ishit);
                    if(x.get(i).ishit==true){
                        ishit=1;
                    }else{
                        ishit=0;
                    }
                    xa=xa+0.5*ishit*(Math.tanh((x.size()-(double)i+1.5)/7.0)+1.0);//tyousetu hituyou
                    //System.out.println(xa);    
                }
            }   
            }
        //}
        //catch(NullPointerException e){
        //    System.err.println(x);
        //}
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