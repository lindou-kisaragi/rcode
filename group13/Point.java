package group13;

import group13.Util;
import java.lang.*;
import java.io.Serializable;

public class Point implements Serializable{
    public double x,y;
    
    //defalt constractor
    public Point(){
    }

    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void set(Point p){
        this.x = p.x;
        this.y = p.y;
    }

    public Point add(Point p) {
        this.x += p.x;
        this.y += p.y;
        return this;
    }

    public Point diff(Point p) {
        this.x -= p.x;
        this.y -= p.y;
        return this;
    }

    public double calcDistance(Point dst) {
        return Util.calcDistance(this, dst);
    }
    public double calcRadians(Point dst) {
        return Util.calcRadians((dst.x-this.x),(dst.y-this.y));
    }

    public void log() {
        System.out.println("Point" + this.x + "," + this.y );
    }
}