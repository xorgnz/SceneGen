package org.memehazard.wheel.core;

public class WheelException extends Exception
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;


    public WheelException(String msg)
    {
        super(msg);
    }


    public WheelException(String msg, Throwable t)
    {
        super(msg, t);
    }

}
