/* 
 * Tester for JXabsl engine
 */  

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.xabsl.jxabsl.IntermediateCodeMalformedException;
import de.xabsl.jxabsl.action.Action;
import de.xabsl.jxabsl.action.ActionBehavior;
import de.xabsl.jxabsl.agent.Agent;
import de.xabsl.jxabsl.behavior.Behavior;
import de.xabsl.jxabsl.behavior.Option;
import de.xabsl.jxabsl.engine.Engine;
import de.xabsl.jxabsl.state.State;
import de.xabsl.jxabsl.utils.InputSource;
import de.xabsl.jxabsl.TimeFunction;

import de.xabsl.jxabslx.conversions.*;
import de.xabsl.jxabslx.engine.*;
import de.xabsl.jxabslx.io.*;
import de.xabsl.jxabslx.symbols.*;
import de.xabsl.jxabslx.utils.*;
   
// Probably needed:
import de.xabsl.jxabsl.IntermediateCodeMalformedException;
import de.xabsl.jxabsl.behavior.BasicBehavior;
import de.xabsl.jxabsl.engine.Engine;
import de.xabsl.jxabsl.parameters.EnumeratedParameter;
import de.xabsl.jxabsl.parameters.Parameters;
import de.xabsl.jxabsl.symbols.BooleanInputSymbol;
import de.xabsl.jxabsl.symbols.Enumeration;
import de.xabsl.jxabsl.utils.InputSource;
import de.xabsl.jxabslx.conversions.Conversions;
import de.xabsl.jxabslx.io.InputFromField;
import de.xabsl.jxabslx.io.InputFromMethod;
import de.xabsl.jxabslx.io.OutputToField;
import de.xabsl.jxabslx.symbols.DecimalInputSymbolImpl;
import de.xabsl.jxabslx.symbols.DecimalOutputSymbolImpl;
import de.xabsl.jxabslx.symbols.EnumeratedInputSymbolImpl;
import de.xabsl.jxabslx.symbols.JavaEnumeration;
import de.xabsl.jxabslx.utils.ScannerInputSource;

public class JXabslTester
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
    public static void test()
    {

    }


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



