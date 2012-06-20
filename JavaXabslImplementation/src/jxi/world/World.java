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
    /** The minimum value for the WNW region of the lasersensor */
    private double robotLaserMinWNW;
    /** The minimum value for the NW region of the lasersensor */
    private double robotLaserMinNW;
    /** The minimum value for the NNW region of the lasersensor */
    private double robotLaserMinNNW;
    /** The minimum value for the N region of the lasersensor */
    private double robotLaserMinN;
    /** The minimum value for the NNE region of the lasersensor */
    private double robotLaserMinNNE;
    /** The minimum value for the NE region of the lasersensor */
    private double robotLaserMinNE;
    /** The minimum value for the ENE region of the lasersensor */
    private double robotLaserMinENE;
    /** The lowest value of all laser beams */
    private double robotLaserMin;
    /** The highest value of all laser beams */
    private double robotLaserMax;

    /** Time spent waiting with basic behavior Wait */
    private double stayed;
    /** The angle the world had the position before now */
    private double lastAngle;
    /** Ammount turned for basic behavior Drive Circle */
    private double ammount_turned;
    /** The robot's possible behaviors. When a new behavior is added to the
     * xabsl-hierarchy, it should be added in this enum as wel as the xabsl-enum
     */
    public enum Behaviors 
    {
        DRIVE_CIRCLE,
        WALK_CORRIDOR,
        UNEXISTANT_BEHAVIOR
    }
    /** Enumerated version of the robots current behavior */
    public Behaviors current_behavior;


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
    /** The setter for the current behavior. this one should always be editted
     * before adding a behavior to the xabsl-hierarchy 
     */
    public void setCurrent_behavior(String behavior)
    {
        if(behavior.equals("drive_circle"))
            current_behavior = Behaviors.DRIVE_CIRCLE;
        else if(behavior.equals("walk_corridor"))
            current_behavior = Behaviors.WALK_CORRIDOR;
    }
    public void setLaserMinWNW(double value)
    {
        this.robotLaserMinWNW = value;
    }
    public void setLaserMinNW(double value)
    {
        this.robotLaserMinNW = value;
    }
    public void setLaserMinNNW(double value)
    {
        this.robotLaserMinNNW = value;
    }
    public void setLaserMinN(double value)
    {
        this.robotLaserMinN = value;
    }
    public void setLaserMinNNE(double value)
    {
        this.robotLaserMinNNE = value;
    }
    public void setLaserMinNE(double value)
    {
        this.robotLaserMinNE = value;
    }
    public void setLaserMinENE(double value)
    {
        this.robotLaserMinENE = value;
    }
    public void setLaserMin(double value)
    {
        this.robotLaserMin = value;
    }
    public void setLaserMax(double value)
    {
        this.robotLaserMax = value;
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
    public Behaviors getCurrent_behavior()
    {
        return this.current_behavior;
    }
    public double getLaserMinWNW()
    {
        return this.robotLaserMinWNW;
    }
    public double getLaserMinNW()
    {
        return this.robotLaserMinNW;
    }
    public double getLaserMinNNW()
    {
        return this.robotLaserMinNNW;
    }
    public double getLaserMinN()
    {
        return this.robotLaserMinN;
    }
    public double getLaserMinNNE()
    {
        return this.robotLaserMinNNE;
    }
    public double getLaserMinNE()
    {
        return this.robotLaserMinNE;
    }
    public double getLaserMinENE()
    {
        return this.robotLaserMinENE;
    }
    public double getLaserMin()
    {
        return this.robotLaserMin;
    }
    public double getLaserMax()
    {
        return this.robotLaserMax;
    }

// }}}

}
