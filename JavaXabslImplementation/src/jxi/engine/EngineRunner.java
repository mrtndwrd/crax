package jxi.engine;

import jxi.engine.Robot;



/* 
 * Tester for JXabsl engine
 */  

public class EngineRunner 
{  
    /** Two of the variables in the test behavior */
    public float x;
    public float y;

    /**
     *  If I understand correctly, I should create the behaviors here, as
     *  follows: The function has the name of one of the behaviors in
     *  my-basic-behaviors.xabsl, and should (I guess...) also make use of the
     *  variables in there. Or maybe those should be class variables, who
     *  knows...
     */
    //public static void test()
    //{
    //
    //}


    public static void main(String[] args)  
    {  
        System.out.println("voor maken engine\n");
        try
        {
            Robot testRobot = new Robot();
        }
        catch(Exception e)
        {
            System.out.println("Houston, we have a problem (oh yeah, I'm funny!)");
            e.printStackTrace();
        }
    }
}


