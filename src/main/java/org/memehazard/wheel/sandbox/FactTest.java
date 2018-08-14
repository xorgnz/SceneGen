package org.memehazard.wheel.sandbox;

public class FactTest
{

    
    public static void main (String[] argv)
    {
        double p = 0.6;
        boolean b = true;
        
        
        if (b)
            p = p + (1 - p) / 2;
        else
            p = p / 2;

        System.err.println(p);
        
    }
}
