package jxi.behaviors;

import de.xabsl.jxabsl.behavior.BasicBehavior;
import de.xabsl.jxabsl.parameters.Parameters;
import de.xabsl.jxabslx.utils.PrintStreamDebug;

import jxi.parameters.MyDecimalParameter;
import jxi.behaviors.StandardBehavior;
import jxi.connection.ConnectionHandler;

/**
 * Behavior that will call differentialDrive of UsarCommander.
 */
public class DifferentialDrive extends StandardBehavior
{
    MyDecimalParameter speed;
    MyDecimalParameter turningSpeed;

    public DifferentialDrive(String name, PrintStreamDebug myDebug, 
        ConnectionHandler usarConnection, double speedIn, double turningSpeedIn)
    {
        super(name, myDebug, usarConnection);
        this.speed = new MyDecimalParameter();
        this.speed.set(speedIn);
        myParameters.registerDecimal("differential_drive.speed", this.speed);
        this.turningSpeed = new MyDecimalParameter();
        this.turningSpeed.set(turningSpeedIn);
        myParameters.registerDecimal("differential_drive.turning_speed", this.turningSpeed);
    }
        
    @Override
    public void execute() 
    {
        System.out.println("[DIFFERENTIALDRIVE] executing");
        usarConnection.sendMessage("DIFFERENTIALDRIVE:" + this.speed.get() + "," + this.turningSpeed.get());
        System.out.println("Sending message: " +
            " DIFFERENTIALDRIVE:" + this.speed.get() + "," + this.turningSpeed.get());
    }
};
