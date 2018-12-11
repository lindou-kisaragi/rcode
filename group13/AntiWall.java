package group13;

import group13.G13_Leader;
import group13.Util;
import group13.RobotInfo;
import group13.Enemy;

import robocode.*;
import robocode.AdvancedRobot;
import robocode.util.Utils;

import java.lang.*;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * AntiWall - a class by (your name here)
 */
public class AntiWall
{
    private TeamRobot robot;
    private MyRobot my;

    public static int WALL_MOVETYPE;
    private static int WALL_STOPCOUNT = 0;
    private static final int WALL_MOVETYPE_STOP = 0;
    private static final int WALL_MOVETYPE_CONSTANT = 1;
    private static final int WALL_MOVETYPE_ACCELE = 2;

    private  double t;
    public  boolean holdFire;
    public  int NextCorner;
    //next corner that wall bot will reach 
    //0:(0,0) 1:() 2:() 3()

    private boolean lockOn;
  	private double radarTurnAmount;
    private double gunTurnAmount;

    public AntiWall(){
    }

    public AntiWall(TeamRobot _robot, MyRobot _my){
        robot = _robot;
        my =_my;
    }

    public void execute(){
        if(!lockOn){
			radarTurnAmount = 90;
		}
		  lockOn = false;
        robot.setTurnRadarRight(radarTurnAmount);
        robot.setTurnGunRight(gunTurnAmount);
    }

    public void onScannedRobot(Enemy enemy){
        lockOn = true;
        radarTurnAmount = Utils.normalRelativeAngleDegrees(my.heading + enemy.bearing - my.radarHeading);
        gunTurnAmount = prepareFire(my,enemy);
        if(!holdFire)robot.setFire(3);
    }

    //This is CORE method!!!
    //calculate enemy's future point and set My robot's gunTurnAmount
    public double prepareFire(MyRobot my,Enemy enemy){
        calcMovetype(enemy.velocity);

		t = calcTimeToReach(my ,enemy, enemy.headingRadians);
		double postX = postEnemyPositionX(enemy, t);
		double postY = postEnemyPositionY(enemy, t);
        double bulletRadian = calcGunTurnRadian(my ,enemy ,t);	

        int aiming = aimingCorner(postX,postY);
        System.out.println("aim at :" + aiming);
        if( WALL_MOVETYPE != 0 && aiming!= -1 && !hitAtCorner(enemy,3,t)){
            System.out.println("corner: false !!!!!");
            holdFire = true;
        }else{
            holdFire = false;
        }

        double gunTurnAmount;
		// //decide gun turn amount according to wall's move type
        if(WALL_MOVETYPE==1 || WALL_MOVETYPE==2){
            gunTurnAmount = Utils.normalRelativeAngleDegrees(Util.pointToDegree(my.x,my.y,postX, postY) - my.gunHeading);
            //gunTurnAmount = Utils.normalRelativeAngleRadians(Util.pointToRadian(my.x, my.y, postX, postY) - my.headingRadians);
		}else{
            gunTurnAmount = Utils.normalRelativeAngleDegrees(my.heading + enemy.bearing - my.gunHeading);
        }
        return gunTurnAmount;
    }

    public int setNextCorner(double movingRadian){
        if(movingRadian == Math.PI*3/2) NextCorner =0;
        if(movingRadian == 0) NextCorner =1;
        if(movingRadian == Math.PI/2) NextCorner = 2;
        if(movingRadian == Math.PI) NextCorner =3;
        
        return NextCorner;
    }

    private int aimingCorner(double x,double y){
        if(Util.calcDistance(x,y,0,0)<50)return 0;
        if(Util.calcDistance(x,y,0,800)<50)return 1;
        if(Util.calcDistance(x,y,800,800)<50)return 2;
        if(Util.calcDistance(x,y,800,0)<50)return 3;
        return -1;
    }

    public boolean hitAtCorner(Enemy enemy, double power, double t){
        double cornerX,cornerY;
        double d_corner;
        double speed;
        int next = setNextCorner(enemy.headingRadians);
        switch (next) {
            case 0:
                cornerX = 18;
                cornerY = 18;
                break;
            case 1:
                cornerX = 18;
                cornerY = 800-18;
                break;
            case 2:
                cornerX = 800-18;
                cornerY = 800-18;
                break;
            case 3:
                cornerX = 800-18;
                cornerY = 18;
            default:
                cornerX = 18;
                cornerY = 18;
                break;
        }
        d_corner = Util.calcDistance(enemy.x,enemy.y,cornerX,cornerY);

        speed = 20- 3*power;
        //note: when wall bot turn 90 degree, it takes 9 seconds
        if(d_corner/speed < (t+9))return false;
        else return true;
    }

    public void calcMovetype(double v){
        if(v==0){
            WALL_STOPCOUNT++;
            if(WALL_STOPCOUNT >= 10){
            WALL_MOVETYPE = WALL_MOVETYPE_STOP;
            }
            //note: wall bot sometimes stop and keep firering
            //      to enemy that can be continuously scanned by wall bot's radar
        }
        if(v>=1 && v<=7){
            WALL_STOPCOUNT= 0;
            WALL_MOVETYPE = WALL_MOVETYPE_ACCELE;
        }
        if(v==8){
            WALL_STOPCOUNT=0;
            WALL_MOVETYPE = WALL_MOVETYPE_CONSTANT; 
        }
    }

    //time to reach predict point of shooting bullet
    public  double calcTimeToReach
    ( MyRobot my,Enemy en,double movingRadian){
        double t; //time to reach bullet to enemy(including turning gun)
        t = solveEquation(57,
        16*(my.x-en.x)*Math.sin(movingRadian) + 16*(my.y-en.y)*Math.cos(movingRadian),
        -(my.x-en.x)*(my.x-en.x)-(my.y-en.y)*(my.y-en.y) );
        return t;
    }

    public  double calcGunTurnRadian
    (MyRobot my ,Enemy en,double t){
        double bx,by; //vector of bullet
        bx = (en.x - my.x + 8*Math.sin(en.headingRadians)*t)/t;
        by = (en.y - my.y + 8*Math.cos(en.headingRadians)*t)/t;   

        double bulletRadian;
        bulletRadian = Math.atan(by/bx) + Math.PI/4;
        //bulletRadian = Math.asin(Math.sqrt(11)/by);
        return bulletRadian;
    }

    //enemy's position x moving by constant velocity
    public double postEnemyPositionX(Enemy enemy, double t){
        double tmp = enemy.x + 8*Math.sin(enemy.headingRadians)*t;

        if(tmp < 0) return 0;
        if(tmp < 800){return tmp;}
        else{return 800;}
    }

    //enemy's position y moving by constant velocity
    public double postEnemyPositionY(Enemy enemy, double t){
        double tmp = enemy.y + 8*Math.cos(enemy.headingRadians)*t;

        if(tmp < 0) return 0;
        if(tmp < 800){return tmp;}
        else{return 800;}
    }

    //solve equation
    public double solveEquation(double a, double b, double c){
        double ans1 =( -b + Math.sqrt(b*b - 4*a*c))/(2*a);
        double ans2 =( -b - Math.sqrt(b*b - 4*a*c))/(2*a);
        //System.out.println("a:" + a + "b:" + b + "c:" + c);  
        //System.out.println("ans1:" + ans1 + "ans2:" + ans2);

        //note: either of answer must be minus
        if(ans1 > ans2) return ans1;
        if(ans2 > ans1) return ans2;
        return 0;
    }

    public void onPaint(Graphics2D g, MyRobot my, Enemy enemy){
        g.setColor(Color.green);
        
        //draw line to wall bot's future point
        g.drawLine((int)my.x, (int)my.y, 
	        (int)(postEnemyPositionX(enemy,t )),
	        (int)(postEnemyPositionY(enemy,t ))
	    );
    }
}
