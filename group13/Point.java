package group13;

import group13.Util;
import java.lang.*;

public class Point
{
    public double x,y;

    //defalt constractor
    public Point(){
    }

    public Point(double x, double y){
        this.x = x;
        this.y = y;
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
        return Util.calcDistance(this.x,this.y,dst.x,dst.y);
    }
    public double calcRadians(Point dst) {
        return Util.calcRadians((dst.x-this.x),(dst.y-this.y));
    }
}