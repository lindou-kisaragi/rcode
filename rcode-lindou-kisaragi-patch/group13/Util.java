package group13;

import group13.Point;
import java.lang.*;
/**
 * MyClass - a class by (your name here)
 */
public class Util
{
    public static int enemies;
    public static double battleFieldWidth;
    public static double battleFieldHeight;

    public static boolean outOfBattleField(Point p){
        if(	p.x < 18.0 || p.y < 18.0 || p.x > battleFieldWidth - 18.0 || p.y > battleFieldHeight - 18.0){
            return true;
        }
        return false;
    }
    
    public static  Point calcPoint(double radians, double distance){
        double x = Math.sin(radians) * distance;
        double y = Math.cos(radians) * distance;
        return new Point(x,y);
    }

    public static Point getRepulsion(Point base,Point target,double weight,double dim){
        return getRepulsion(base,target,weight,dim,18); //tankwidth is 18
    }

    public static Point getRepulsion(Point base,Point target,double weight,double dim, double threshold ){
        double distance = base.calcDistance(target);
        distance = (distance<threshold)?threshold:distance;
        double radians  = base.calcRadians(target);

        double force = weight/Math.pow(distance/10,dim);

        return calcPoint(radians, force);
    }   

    public static Point getGravity(Point base,Point target,double weight,double dim){
        return getGravity(base,target,weight,dim,0); //tankwidth is 18
    }

    //
    public static Point getGravity(Point base,Point target,double weight,double dim, double threshold ){
        double distance = base.calcDistance(target);
        distance = (distance<threshold)?threshold:distance;
        double radians  = base.calcRadians(target);

        double force = Math.pow(distance,dim) * 10/ weight;

        return calcPoint(radians, force);
    }


    //convert two point to degree
    public static double pointToDegree(double srcX, double srcY, double dstX, double dstY){
        double diff_X = dstX - srcX;
        double diff_Y = dstY - srcY;
        if(diff_Y > 0){
            return Math.atan(diff_X/diff_Y)/Math.PI*180;
        }else {
            return Math.atan(diff_X/diff_Y)/Math.PI*180 + 180;
        }
    }

    //convert two point to radian
    public static double pointToRadian(double srcX, double srcY, double dstX, double dstY){
        double diff_X = dstX - srcX;
        double diff_Y = dstY - srcY;
        if(diff_Y > 0){
            return Math.atan(diff_X/diff_Y);
        }else {
            return Math.atan(diff_X/diff_Y) + Math.PI;
        }
    }

    public static double calcDistance(Point p){
        return (p.x*p.x + p.y*p.y);
    }

    public static double calcDistance(Point p1, Point p2) {
        double distance = Math.sqrt((p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y));
        return  distance;
    }

    public static double calcDistance(double x1, double y1, double x2, double y2) {
        double distance = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 -y1));
        return  distance;
    }

    public static double calcRadians(double x, double y){
        if ( y == 0 ) {
            return (x > 0) ? Math.PI / 2 : Math.PI / -2;
        }else if ( y > 0 ) {
            return Math.atan(x/y);
        }else {
            return Math.atan(x/y) - Math.PI;            
        }
    }

    public static double calcTurnRadians(double src,double dst){
        double diffRadians = (dst - src) % (2*Math.PI);
        if (diffRadians > Math.PI) {
            diffRadians = diffRadians - 2*Math.PI;
        } else if (diffRadians < -Math.PI) {
            diffRadians = diffRadians + 2*Math.PI;
        }
        return diffRadians;
    }

    public static double calcTurnTime(double turnAmount, double velocity) {
        double turnspeed = 10 - 0.75 * Math.abs(velocity);
        return (Math.abs(turnAmount)/turnspeed);
    }

    public static double calcRunTime(double distance, double velocity) {
        velocity = Math.abs(velocity);
        return (distance/velocity);
    }

    public static double solveEquation(double a, double b, double c){
        double ans1 =( -b + Math.sqrt(b*b - 4*a*c))/(2*a);
        double ans2 =( -b - Math.sqrt(b*b - 4*a*c))/(2*a);
        //System.out.println("a:" + a + "b:" + b + "c:" + c);  
        //System.out.println("ans1:" + ans1 + "ans2:" + ans2);

        //note: either of answer must be minus
        if(ans1 > ans2) return ans1;
        if(ans2 > ans1) return ans2;
        return 0;
    }

    public static double bulletSpeed(double power){
        return (20 - 3 * power);
    }

    public static boolean isLastShot(double power ,double energy){
        double damage = 0;
        if(power > 1.0){
            damage = 6 * power -2;
        }else {
            damage = 4 * power;
        }
        return damage >= energy ? true : false;
    }
}
