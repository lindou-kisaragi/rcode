package group13.bulletmap;

import group13.RobotInfo;
import group13.MyRobot;
import group13.Enemy;
import group13.EnemyDataManager;
import group13.Util;

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

public class BulletInfo implements Serializable{
    public String name;
    public double speed;
    public double angle1,angle2;//atarumade wakaranai//angle1 to angle2 ha gosa wo shimesu.//radian!!
    public long tprev,t;
    public double prex , prey,x,y;
    public double myx,myy;
    public int pattern;//1:tousoku 2:toukasoku 3:other
    public boolean ishit;//yosou ga seikai ka
    public Bullet bullet;//mikata nomi
}