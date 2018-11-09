package group13;

import group13.Point;
import java.lang.*;
/**
 * MyClass - a class by (your name here)
 */
public class Util
{
    public static double battleFieldwidth;
    public static double battleFieldHeight;

    
    public static  Point calcPoint(double radians, double distance){
        double x = Math.sin(radians) * distance;
        double y = Math.cos(radians) * distance;
        return new Point(x,y);
    }

    public static Point getGrabity(Point base,Point target,double weight,double dim){
        return getGrabity(base,target,weight,dim,18); //tankwidth is 18
    }

    public static Point getGrabity(Point base,Point target,double weight,double dim, double threshold ){
        double distance = base.calcDistance(target);
        distance = (distance<threshold)?threshold:distance;
        double radians  = base.calcRadians(target);
        double force = weight/Math.pow(distance,dim);
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

    public static double calcDistance(double x, double y){
        return (x*x+y*y);
    }

    public static double calcDistance(double x, double y, double x2, double y2) {
        double distance = Math.sqrt((x2 - x) * (x2 - x) + (y2 - y) * (y2 - y));
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
