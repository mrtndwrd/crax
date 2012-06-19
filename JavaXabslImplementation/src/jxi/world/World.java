package jxi.world;

/** 
 * The internal world representation of everything that is sent through by UsarCommander 
 * For now only one robot is assumed. A lot of class variables should be changed to arrays if * several robots are used. For example robotX should be an array
 */
public class World
{
    /** The robot's x position */
    private double robotX;
    /** The robot's y position */
    private double robotY;
    /** The robot's yaw (angle on z-axis) */
    private double robotYaw;
    /** Time spent waiting with basic behavior Wait */
    private double stayed;
    /** The angle the world had the position before now */
    private double lastAngle;
    /** Ammount turned for basic behavior Drive Circle */
    private double ammount_turned;
    /** The robot's current behavior */
    private String behavior;

    /** Constructor */
    public World()
    {
        System.out.println("[WORLD] new world representation created");
    }

// Setters {{{
    public void setX(double x)
    {
        this.robotX = x;
    }
    
    public void setY(double y)
    {
        this.robotY = y;
    }

    public void setYaw(double yaw)
    {
        this.robotYaw = yaw;
    }
    public void setStayed(double stayed)
    {
        this.stayed = stayed;
    }
    public void setAmmount_turned(double turned)
    {
        this.ammount_turned = turned;
    }
    public void setLastAngle(double angle)
    {
        this.lastAngle = angle;
    }
    public void setBehavior(String behavior)
    {
        this.behavior = behavior;
    }

// }}}

// Getters {{{
    public double getX()
    {
        return robotX;
    }
    public double getY()
    {
        return robotY;
    }
    public double getYaw()
    {
        return robotYaw;
    }
    public double getStayed()
    {
        return stayed;
    }
    public double getAmmount_turned()
    {
        return ammount_turned;
    }
    public double getLastAngle()
    {
        return this.lastAngle;
    }
    public String getBehavior()
    {
        return this.behavior;
    }

// }}}

}
