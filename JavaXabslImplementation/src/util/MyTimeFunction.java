package jxi.util;
import de.xabsl.jxabsl.*;

public class MyTimeFunction implements TimeFunction
{
    /* I don't need no constructor! */
    public MyTimeFunction(){}

    public long getTime()
    {
        return System.currentTimeMillis();
    }
}
