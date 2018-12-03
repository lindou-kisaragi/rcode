package group13;

import group13.Point;
import java.lang.*;

/**
 * MyClass - a class by (your name here)
 */
public class RobotInfo extends Point
{
    static public final int ROLE_UNKNOWN = 0;
    static public final int ROLE_DROID   = 1;
    static public final int ROLE_ROBOT   = 2;
    static public final int ROLE_LEADER  = 3;

    public long time;
    public String name;
    public int role;
    public double angle;
    public double heading;
    public double headingRadians;
    public double velocity;
    public double energy;
    public double heat;
}
